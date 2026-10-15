package com.sakethh.tao_aat

import android.content.Context
import androidx.room3.Room
import androidx.test.core.app.ApplicationProvider
import com.sakethh.tao_aat.dao.AttributionDao
import com.sakethh.tao_aat.data.LocalDatabase
import com.sakethh.tao_aat.data.repository.AttributionRepoImpl
import com.sakethh.tao_aat.domain.Attribution
import com.sakethh.tao_aat.domain.TimeProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

class AttributionRepoTest {
    private lateinit var db: LocalDatabase
    private lateinit var dao: AttributionDao
    private val repos = mutableListOf<AttributionRepoImpl>()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, LocalDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.attributionDao
        repos.clear()
    }

    @After
    fun teardown() {
        repos.forEach { it.shutdownDaemons() }
        db.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun TestScope.repo(
        sessionId: String = "session-1",
        shouldFail: () -> Boolean = { false },
    ): AttributionRepoImpl {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val timeProvider = object : TimeProvider {
            override fun currentTimeMillis(): Long = testScheduler.currentTime
        }
        return AttributionRepoImpl(
            currentSessionId = sessionId,
            attributionDao = dao,
            coroutineDispatcher = dispatcher,
            shouldFailReporting = shouldFail,
            timeProvider = timeProvider
        ).also { repos.add(it) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun TestScope.awaitUntilSettled(
        timeoutMs: Long = 10_000,
        condition: suspend () -> Boolean,
    ) {
        val deadline = System.currentTimeMillis() + timeoutMs
        while (System.currentTimeMillis() < deadline) {
            advanceTimeBy(1_000.milliseconds)
            advanceUntilIdle()
            if (condition()) return
            withContext(Dispatchers.IO) { delay(10.milliseconds) }
        }
        error("Condition not met within ${timeoutMs}ms, rows: ${dao.getAllAttributions().first()}")
    }

    @Test
    fun addAttribution_endsProcessed() = runTest {
        val repo = repo()

        backgroundScope.launch {
            repo.addAttribution(Attribution.Type.ADD_TO_CART).collect()
        }

        awaitUntilSettled {
            dao.getAllAttributions().first().singleOrNull()?.status == Attribution.Status.PROCESSED
        }

        val row = dao.getAllAttributions().first().single()
        Assert.assertEquals(1, dao.getAllAttributions().first().size)
        Assert.assertEquals(Attribution.Type.ADD_TO_CART, row.type)
        Assert.assertEquals("session-1", row.sessionId)
        Assert.assertTrue(row.insertedAt >= 0)
        Assert.assertEquals(Attribution.Status.PROCESSED, row.status)
    }

    @Test
    fun failedIngestion_retriesUntilProcessed() = runTest {
        var attempts = 0
        val repo = repo(shouldFail = {
            attempts++
            attempts == 1
        })

        backgroundScope.launch {
            repo.addAttribution(Attribution.Type.PURCHASE).collect()
        }

        awaitUntilSettled {
            dao.getAllAttributions().first().singleOrNull()?.status == Attribution.Status.PROCESSED
        }

        val row = dao.getAllAttributions().first().single()
        Assert.assertEquals(Attribution.Status.PROCESSED, row.status)
        // proves that a failure occurred and a retry was successfully triggered
        Assert.assertEquals(2, attempts)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun stuckProcessing_recoversOnRelaunch() = runTest {
        val stuckRow = Attribution(
            type = Attribution.Type.INSTALL,
            status = Attribution.Status.PROCESSING,
            insertedAt = testScheduler.currentTime,
            sessionId = "session-1",
            nextRetryAt = null
        )
        dao.addAttribution(stuckRow)

        // simulates app relaunch
        repo()

        awaitUntilSettled {
            dao.getAllAttributions().first().singleOrNull()?.status == Attribution.Status.PROCESSED
        }

        val row = dao.getAllAttributions().first().single()
        Assert.assertEquals(Attribution.Status.PROCESSED, row.status)
    }
}