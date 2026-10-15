package com.sakethh.tao_aat.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.sakethh.tao_aat.domain.Attribution
import kotlinx.coroutines.flow.Flow

@Dao
interface AttributionDao {
    @Insert
    suspend fun addAttribution(attribution: Attribution): Long

    @Update
    suspend fun updateAttribution(attribution: Attribution)

    @Query("SELECT EXISTS(SELECT 1 FROM ad_attribution WHERE type = 'INSTALL')")
    suspend fun isInstalled(): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM ad_attribution WHERE type = 'VISIT' AND sessionId = :currentSessionId)")
    suspend fun didVisited(currentSessionId: String): Boolean

    @Query("SELECT * FROM ad_attribution WHERE status = 'RETRY' AND nextRetryAt IS NOT NULL AND nextRetryAt <= :currentTime")
    suspend fun getFailedAttributionOps(currentTime: Long): List<Attribution>

    @Query("SELECT * FROM ad_attribution WHERE status = 'PROCESSING'")
    suspend fun getPendingAttributionOps(): List<Attribution>

    @Query("SELECT * FROM ad_attribution")
    fun getAllAttributions(): Flow<List<Attribution>>

    @Query("DELETE FROM ad_attribution")
    suspend fun dropAllRows()
}