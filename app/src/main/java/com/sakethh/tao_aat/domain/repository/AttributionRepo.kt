package com.sakethh.tao_aat.domain.repository

import com.sakethh.tao_aat.domain.Attribution
import kotlinx.coroutines.flow.Flow

interface AttributionRepo {
    suspend fun addAttribution(attribution: Attribution): Flow<Attribution.Status>
}