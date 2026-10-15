package com.sakethh.tao_aat.domain.repository

import com.sakethh.tao_aat.domain.Attribution
import kotlinx.coroutines.flow.Flow

interface AttributionRepo {
    fun addAttribution(type: Attribution.Type): Flow<Attribution.Status>

    suspend fun dropAllRows()

    fun getAllAttributions(): Flow<List<Attribution>>

    fun shutdownDaemons()

    fun relaunchDaemons()

    fun getTotalProcessedAttributionsCount(): Flow<Int>
    fun getTotalUniqueVisitCount(): Flow<Int>
    fun getTotalCountByType(type: Attribution.Type): Flow<Int>
}