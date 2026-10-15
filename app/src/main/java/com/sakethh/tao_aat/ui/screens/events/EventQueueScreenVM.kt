package com.sakethh.tao_aat.ui.screens.events

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.tao_aat.domain.Attribution
import com.sakethh.tao_aat.domain.repository.AttributionRepo
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import org.json.JSONArray

class EventQueueScreenVM(private val attributionRepo: AttributionRepo) : ViewModel() {
    val eventQueueScreenState = attributionRepo.getAllAttributions().transform { attributions ->
        emit(
            EventQueueScreenState(
                allEvents = attributions, retryingEvents = attributions.filter { attribution ->
                    attribution.status == Attribution.Status.RETRY
                })
        )
    }.stateIn(
        initialValue = EventQueueScreenState(
            allEvents = emptyList(), retryingEvents = emptyList()
        )
    )

    init {
        viewModelScope.launch {
            if (attributionRepo.getAllAttributions().first().isEmpty()) {
                Log.d("TAO-AAT", "dropAndAddAttributeRows")
                dropAndAddAttributeRows()
            }
        }
    }

    private var dropAndAddAttributeRowsJob: Job? = null

    val json = """
                [
                  { "type": "INSTALL" },
                  { "type": "VISIT" },
                  { "type": "ADD_TO_CART" },
                  { "type": "VISIT" },
                  { "type": "ADD_TO_CART" },
                  { "type": "PURCHASE" },
                  { "type": "INSTALL" },
                  { "type": "VISIT" },
                  { "type": "ADD_TO_CART" },
                  { "type": "PURCHASE" },
                  { "type": "VISIT" },
                  { "type": "ADD_TO_CART" },
                  { "type": "PURCHASE" },
                  { "type": "INSTALL" },
                  { "type": "ADD_TO_CART" },
                  { "type": "PURCHASE" },
                  { "type": "VISIT" },
                  { "type": "PURCHASE" }
                ]
            """.trimIndent()

    private val jsonArr = JSONArray(json)

    // 12 valid ops
    val attributions by lazy {
        buildList {
            var currIndex = -1
            while (true) {
                try {
                    val typeStr = jsonArr.getJSONObject(++currIndex).get("type").toString()
                    add(Attribution.Type.valueOf(typeStr))
                } catch (_: Exception) {
                    break
                }
            }
        }
    }

    fun dropAndAddAttributeRows() {
        dropAndAddAttributeRowsJob?.cancel()
        dropAndAddAttributeRowsJob = viewModelScope.launch {
            attributionRepo.shutdownDaemons()
            attributionRepo.dropAllRows()
            attributionRepo.relaunchDaemons()
            attributions.forEach { type ->
                attributionRepo.addAttribution(type).collect()

                // fire and forget, spec doesn't mention this explicitly
                // attributionRepo.addAttribution(type).launchIn(viewModelScope)
            }
        }
    }

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> {
        return this.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = initialValue
        )
    }
}