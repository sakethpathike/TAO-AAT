package com.sakethh.tao_aat.data

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.sakethh.tao_aat.dao.AttributionDao
import com.sakethh.tao_aat.domain.Attribution

@Database(entities = [Attribution::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract val attributionDao: AttributionDao
}