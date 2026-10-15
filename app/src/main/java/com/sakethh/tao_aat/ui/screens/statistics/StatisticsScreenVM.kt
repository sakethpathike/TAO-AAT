package com.sakethh.tao_aat.ui.screens.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.tao_aat.domain.Attribution
import com.sakethh.tao_aat.domain.repository.AttributionRepo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.milliseconds

class StatisticsScreenVM(attributionRepo: AttributionRepo) : ViewModel() {
    val statisticsScreenState = combine(
        attributionRepo.getTotalProcessedAttributionsCount(),
        attributionRepo.getTotalUniqueVisitCount(),
        attributionRepo.getTotalCountByType(Attribution.Type.INSTALL),
        attributionRepo.getTotalCountByType(Attribution.Type.VISIT),
        attributionRepo.getTotalCountByType(Attribution.Type.ADD_TO_CART),
        attributionRepo.getTotalCountByType(Attribution.Type.PURCHASE),
    ) { (totalAttributionsCount, totalUniqueVisitCount, installCount, visitCount, addToCartCount, purchaseCount) ->
        StatisticsScreenState(
            stats = listOf(
                TypeStat(
                    type = Attribution.Type.INSTALL, count = installCount
                ),
                TypeStat(
                    type = Attribution.Type.VISIT, count = visitCount
                ),
                TypeStat(
                    type = Attribution.Type.ADD_TO_CART, count = addToCartCount
                ),
                TypeStat(
                    type = Attribution.Type.PURCHASE, count = purchaseCount
                ),
            ),
            totalEventsProcessed = totalAttributionsCount,
            totalUniqueVisits = totalUniqueVisitCount
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000.milliseconds),
        initialValue = StatisticsScreenState(
            stats = emptyList(), totalEventsProcessed = 0, totalUniqueVisits = 0
        )
    )

    operator fun Array<Int>.component6() = this[5]
}