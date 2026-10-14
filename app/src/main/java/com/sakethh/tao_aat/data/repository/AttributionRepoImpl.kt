package com.sakethh.tao_aat.data.repository

import com.sakethh.tao_aat.dao.AttributionDao
import com.sakethh.tao_aat.domain.Attribution
import com.sakethh.tao_aat.domain.repository.AttributionRepo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class AttributionRepoImpl(
    private val currentSessionId: String,
    private val attributionDao: AttributionDao,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AttributionRepo {
    private val repoScope = CoroutineScope(SupervisorJob() + coroutineDispatcher)

    fun shutdownDaemons() {
        repoScope.cancel()
    }

    init {
        repoScope.launch {
            processPendingAttributionOps()
        }

        repoScope.launch {
            retryFailedOpsDaemon()
        }
    }

    /** status = 'PROCESSING'*/
    private suspend fun processPendingAttributionOps() {
        attributionDao.getPendingAttributionOps().forEach { attribution ->
            repoScope.launch {
                processAttribute(attribution).collect()
            }
        }
    }

    private suspend fun retryFailedOpsDaemon() = supervisorScope {
        while (true) {
            val failedOps =
                attributionDao.getFailedAttributionOps(currentTime = System.currentTimeMillis())

            if (failedOps.isEmpty()) {
                delay(1.seconds)
                continue
            }

            failedOps.forEach { attribution ->
                // flip to PROCESSING since we don't want this to be in the next iteration
                attributionDao.updateAttribution(
                    attribution.copy(status = Attribution.Status.PROCESSING)
                )
                this@supervisorScope.launch {
                    processAttribute(attribution).collect()
                }
            }
        }
    }

    override suspend fun addAttribution(attribution: Attribution): Flow<Attribution.Status> = flow {
        // this will retrieve the installation status every time there is an INSTALL type, instead
        // we can use a lazy initialization from the upstream and use it across the usage,
        // for this example case, this should be fine
        if (attribution.type == Attribution.Type.INSTALL && attributionDao.isInstalled()) return@flow

        if (attribution.type == Attribution.Type.VISIT && attributionDao.didVisited(currentSessionId)) return@flow

        val rowId =
            attributionDao.addAttribution(attribution.copy(status = Attribution.Status.PROCESSING))

        emitAll(processAttribute(attribution.copy(id = rowId)))
    }

    private fun getRandomDelayMs() = (1000L..5000L).random().milliseconds

    private fun processAttribute(attribution: Attribution) = flow {
        emit(Attribution.Status.PROCESSING)
        try {
            delay(getRandomDelayMs())
            if (Random.nextDouble() < 0.2) throw Exception()

            attributionDao.updateAttribution(
                attribution.copy(
                    id = attribution.id, status = Attribution.Status.PROCESSED
                )
            )
            emit(Attribution.Status.PROCESSED)
        } catch (ce: CancellationException) {
            throw ce
        } catch (_: Exception) {
            val retryTime = System.currentTimeMillis() + getRandomDelayMs().inWholeMilliseconds
            // we do emit RETRY (to the UI or whoever's the caller is)
            // since the RETRY status is collected by #retryFailedOpsDaemon which retires the op
            attributionDao.updateAttribution(
                attribution.copy(
                    id = attribution.id, status = Attribution.Status.RETRY, nextRetryAt = retryTime
                )
            )
            emit(Attribution.Status.RETRY)
        }
    }
}