package com.sakethh.tao_aat.di

import com.sakethh.tao_aat.AATApp
import com.sakethh.tao_aat.data.LocalDatabase
import com.sakethh.tao_aat.data.repository.AttributionRepoImpl
import com.sakethh.tao_aat.domain.TimeProvider
import kotlinx.coroutines.Dispatchers

class DIContainer(val database: LocalDatabase) {
    companion object {
        val attributionRepo by lazy {
            AttributionRepoImpl(
                currentSessionId = AATApp.CURR_SESSION_ID,
                attributionDao = getCurrentInstance().database.attributionDao,
                coroutineDispatcher = Dispatchers.IO,
                timeProvider = object : TimeProvider {
                    override fun currentTimeMillis(): Long {
                        return System.currentTimeMillis()
                    }
                }
            )
        }

        private var currDIContainer: DIContainer? = null

        fun getCurrentInstance(): DIContainer {
            return currDIContainer ?: error("DIContainer must be initialized")
        }

        fun initialize(diContainer: DIContainer) {
            if (currDIContainer == null) {
                currDIContainer = diContainer
            }
        }
    }
}