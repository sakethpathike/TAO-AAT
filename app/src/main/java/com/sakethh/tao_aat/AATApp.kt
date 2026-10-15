package com.sakethh.tao_aat

import android.app.Application
import androidx.room3.Room
import com.sakethh.tao_aat.data.LocalDatabase
import com.sakethh.tao_aat.di.DIContainer
import kotlinx.coroutines.Dispatchers
import java.util.UUID

class AATApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DIContainer.initialize(
            diContainer = DIContainer(database = run {
                val dbFile = applicationContext.getDatabasePath(LocalDatabase.NAME)
                Room.databaseBuilder<LocalDatabase>(
                    applicationContext,
                    name = dbFile.absolutePath,
                ).setQueryCoroutineContext(Dispatchers.IO).build()
            })
        )
    }

    companion object {
        val CURR_SESSION_ID = UUID.randomUUID().toString()
    }
}