package com.sakethh.tao_aat.data.repository

import com.sakethh.tao_aat.dao.AttributionDao
import com.sakethh.tao_aat.domain.Attribution
import com.sakethh.tao_aat.domain.TimeProvider
import com.sakethh.tao_aat.domain.repository.AttributionRepo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class AttributionRepoImpl(
    private val currentSessionId: String,
    private val attributionDao: AttributionDao,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val shouldFailReporting: () -> Boolean = { Random.nextDouble() < 0.2 },
    private val timeProvider: TimeProvider
) : AttributionRepo {
    private val repoScope = CoroutineScope(SupervisorJob() + coroutineDispatcher)

    override fun shutdownDaemons() {
        repoScope.coroutineContext.cancelChildren()
    }

    override fun relaunchDaemons() {
        shutdownDaemons()

        repoScope.launch {
            processPendingAttributionOps()
        }

        repoScope.launch {
            retryFailedOpsDaemon()
        }
    }

    init {
        relaunchDaemons()
    }

    /** status = 'PROCESSING'*/
    private suspend fun processPendingAttributionOps() {
        println("processPendingAttributionOps")
        attributionDao.getPendingAttributionOps().forEach { attribution ->
            repoScope.launch {
                processAttribute(attribution).collect()
            }
        }
    }

    private suspend fun retryFailedOpsDaemon() = supervisorScope {
        while (true) {
            if (!isActive) break

            val failedOps =
                attributionDao.getFailedAttributionOps(currentTime = timeProvider.currentTimeMillis())

            if (failedOps.isEmpty()) {
                delay(1.seconds)
                continue
            }
            println("retryFailedOpsDaemon")
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

    override fun addAttribution(type: Attribution.Type): Flow<Attribution.Status> = flow {
        // this will retrieve the installation status every time there is an INSTALL type, instead
        // we can use a lazy initialization from the upstream and use it across the usage,
        // for this example case, this should be fine
        if (type == Attribution.Type.INSTALL && attributionDao.isInstalled()) return@flow

        if (type == Attribution.Type.VISIT && attributionDao.didVisited(currentSessionId)) return@flow

        val attribution = Attribution(
            type = type,
            status = Attribution.Status.PROCESSING,
            insertedAt = timeProvider.currentTimeMillis(),
            sessionId = currentSessionId,
            nextRetryAt = null
        )

        val rowId = attributionDao.addAttribution(attribution)

        emitAll(processAttribute(attribution.copy(id = rowId)))
    }

    override fun getAllAttributions(): Flow<List<Attribution>> {
        return attributionDao.getAllAttributions()
    }

    private fun getRandomDelayMs() = (1000L..5000L).random().milliseconds

    private fun processAttribute(attribution: Attribution) = flow {
        emit(Attribution.Status.PROCESSING)
        try {
            delay(getRandomDelayMs())
            if (shouldFailReporting()) throw Exception()

            attributionDao.updateAttribution(
                attribution.copy(
                    id = attribution.id, status = Attribution.Status.PROCESSED
                )
            )
            emit(Attribution.Status.PROCESSED)
        } catch (ce: CancellationException) {
            throw ce
        } catch (_: Exception) {
            val retryTime = timeProvider.currentTimeMillis() + getRandomDelayMs().inWholeMilliseconds
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

    override suspend fun dropAllRows() {
        attributionDao.dropAllRows()
    }

    override fun getTotalProcessedAttributionsCount(): Flow<Int> {
        return attributionDao.getProcessedAttributionsCount()
    }

    override fun getTotalUniqueVisitCount(): Flow<Int> {
        return attributionDao.getUniqueVisitCount()
    }

    override fun getTotalCountByType(type: Attribution.Type): Flow<Int> {
        return attributionDao.getCountByProcessedType(type)
    }
}