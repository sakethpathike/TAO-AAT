package com.sakethh.tao_aat.ui.screens.statistics

import androidx.compose.runtime.Immutable
import com.sakethh.tao_aat.domain.Attribution

@Immutable
data class StatisticsScreenState(
    val stats: List<TypeStat>,
    val totalEventsProcessed: Int,
    val totalUniqueVisits: Int
)

data class TypeStat(
    val type: Attribution.Type,
    val count: Int
)
