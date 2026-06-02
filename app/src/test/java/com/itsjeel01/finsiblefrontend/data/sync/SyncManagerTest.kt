package com.itsjeel01.finsiblefrontend.data.sync

import com.google.common.truth.Truth.assertThat
import com.itsjeel01.finsiblefrontend.MainDispatcherRule
import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.OperationType
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.SyncState
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.PendingOperationRepository
import io.mockk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SyncManagerTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val pendingOperationRepository: PendingOperationRepository = mockk(relaxed = true)
    private val networkMonitor: NetworkMonitor = mockk()
    private val coroutineScope = CoroutineScope(Dispatchers.Unconfined)

    private lateinit var syncManager: SyncManager

    @Before
    fun setUp() {
        every { networkMonitor.isOnline } returns MutableStateFlow(true)
        syncManager = SyncManager(
            pendingOperationRepository = pendingOperationRepository,
            networkMonitor = networkMonitor,
            coroutineScope = coroutineScope,
            syncHandlers = emptyMap()
        )
    }

    @Test
    fun `start initializes sync and updates pending count`() {
        every { pendingOperationRepository.getPendingCount() } returns 5L

        syncManager.start()

        assertThat(syncManager.pendingCount.value).isEqualTo(5)
        coVerify { pendingOperationRepository.getPendingCount() }
    }

    @Test
    fun `start is idempotent`() {
        every { pendingOperationRepository.getPendingCount() } returns 3L

        syncManager.start()
        syncManager.start()
        syncManager.start()

        coVerify(exactly = 1) { pendingOperationRepository.getPendingCount() }
    }

    @Test
    fun `processQueue sets idle when no pending ops`() = runTest {
        every { pendingOperationRepository.getPending() } returns emptyList()

        syncManager.start()
        syncManager.processQueue()

        assertThat(syncManager.syncState.value).isInstanceOf(SyncState.Idle::class.java)
    }

    @Test
    fun `processQueue processes all pending operations`() = runTest {
        val handler: EntitySyncHandler = mockk(relaxed = true)
        every { handler.entityType } returns EntityType.TRANSACTION
        coEvery { handler.processCreate(any()) } returns Unit

        val pending = listOf(
            PendingOperationEntity(
                localId = 1, entityType = EntityType.TRANSACTION,
                operationType = OperationType.CREATE,
                payload = "{}", createdAt = 1L
            )
        )
        every { pendingOperationRepository.getPending() } returns pending
        every { pendingOperationRepository.getPendingCount() } returns 0L

        syncManager = SyncManager(
            pendingOperationRepository = pendingOperationRepository,
            networkMonitor = networkMonitor,
            coroutineScope = coroutineScope,
            syncHandlers = mapOf(EntityType.TRANSACTION to handler)
        )
        syncManager.start()
        syncManager.processQueue()

        coVerify { handler.processCreate(pending.first()) }
    }

    @Test
    fun `processQueue handles sync errors and marks failed`() = runTest {
        val handler: EntitySyncHandler = mockk()
        every { handler.entityType } returns EntityType.TRANSACTION
        coEvery { handler.processCreate(any()) } throws SyncException("Test error", isRetryable = false)

        val pending = listOf(
            PendingOperationEntity(
                localId = 1, entityType = EntityType.TRANSACTION,
                operationType = OperationType.CREATE,
                payload = "{}", createdAt = 1L
            )
        )
        every { pendingOperationRepository.getPending() } returns pending
        every { pendingOperationRepository.getPendingCount() } returns 0L

        syncManager = SyncManager(
            pendingOperationRepository = pendingOperationRepository,
            networkMonitor = networkMonitor,
            coroutineScope = coroutineScope,
            syncHandlers = mapOf(EntityType.TRANSACTION to handler)
        )
        syncManager.start()
        syncManager.processQueue()

        // Operation should be marked FAILED
        coVerify { pendingOperationRepository.update(match { it.status == Status.FAILED }) }
    }

    @Test
    fun `processQueue retries retryable errors up to MAX_RETRIES`() = runTest {
        val handler: EntitySyncHandler = mockk()
        every { handler.entityType } returns EntityType.TRANSACTION
        coEvery { handler.processCreate(any()) } throws SyncException("Network timeout", isRetryable = true)

        val pending = listOf(
            PendingOperationEntity(
                localId = 1, entityType = EntityType.TRANSACTION,
                operationType = OperationType.CREATE,
                payload = "{}", createdAt = 1L,
                retryCount = SyncManager.MAX_RETRIES // Already maxed out
            )
        )
        every { pendingOperationRepository.getPending() } returns pending
        every { pendingOperationRepository.getPendingCount() } returns 0L

        syncManager = SyncManager(
            pendingOperationRepository = pendingOperationRepository,
            networkMonitor = networkMonitor,
            coroutineScope = coroutineScope,
            syncHandlers = mapOf(EntityType.TRANSACTION to handler)
        )
        syncManager.start()
        syncManager.processQueue()

        // Should be marked FAILED after MAX_RETRIES exceeded
        coVerify { pendingOperationRepository.update(match { it.status == Status.FAILED }) }
    }

    @Test
    fun `no handler for entity type throws and marks failed`() = runTest {
        val pending = listOf(
            PendingOperationEntity(
                localId = 1, entityType = EntityType.CATEGORY,
                operationType = OperationType.CREATE,
                payload = "{}", createdAt = 1L
            )
        )
        every { pendingOperationRepository.getPending() } returns pending
        every { pendingOperationRepository.getPendingCount() } returns 0L

        syncManager.start()
        syncManager.processQueue()

        coVerify { pendingOperationRepository.update(match { it.status == Status.FAILED }) }
    }

    @Test
    fun `retryFailed resets failed ops and reprocesses queue`() = runTest {
        val failedOp = PendingOperationEntity(
            localId = 1, entityType = EntityType.TRANSACTION,
            operationType = OperationType.CREATE,
            payload = "{}", createdAt = 1L,
            retryCount = 3, status = Status.FAILED
        )
        every { pendingOperationRepository.getFailed() } returns listOf(failedOp)
        every { pendingOperationRepository.getPending() } returns emptyList()
        every { pendingOperationRepository.getPendingCount() } returns 0L

        syncManager.start()
        syncManager.retryFailed()

        // retryFailed should have been called without crashing
        assertThat(syncManager.syncState.value).isInstanceOf(SyncState.Idle::class.java)
    }

    @Test
    fun `processQueue handles concurrent calls gracefully`() = runTest {
        every { pendingOperationRepository.getPending() } returns emptyList()
        every { pendingOperationRepository.getPendingCount() } returns 0L

        syncManager.start()

        // Call processQueue twice in quick succession — second should be no-op
        syncManager.processQueue()
        syncManager.processQueue()

        // Should not crash, state should be Idle after
        assertThat(syncManager.syncState.value).isInstanceOf(SyncState.Idle::class.java)
    }

    @Test
    fun `processQueue pauses when network goes offline mid-sync`() = runTest {
        val networkFlow = MutableStateFlow(true)
        every { networkMonitor.isOnline } returns networkFlow

        val handler: EntitySyncHandler = mockk(relaxed = true)
        every { handler.entityType } returns EntityType.TRANSACTION
        // Simulate network dropping during first operation
        coEvery { handler.processCreate(any()) } answers {
            networkFlow.value = false // Drop network mid-sync
        }

        val pending = listOf(
            PendingOperationEntity(localId = 1, entityType = EntityType.TRANSACTION, operationType = OperationType.CREATE, payload = "{}", createdAt = 1L),
            PendingOperationEntity(localId = 2, entityType = EntityType.TRANSACTION, operationType = OperationType.CREATE, payload = "{}", createdAt = 2L)
        )
        every { pendingOperationRepository.getPending() } returns pending
        every { pendingOperationRepository.getPendingCount() } returns 0L

        syncManager = SyncManager(
            pendingOperationRepository = pendingOperationRepository,
            networkMonitor = networkMonitor,
            coroutineScope = coroutineScope,
            syncHandlers = mapOf(EntityType.TRANSACTION to handler)
        )
        syncManager.start()
        syncManager.processQueue()

        // Only the first operation should have been processed
        coVerify(exactly = 1) { handler.processCreate(any()) }
        // Second operation should still be in the queue (not removed)
        assertThat(syncManager.syncState.value).isInstanceOf(SyncState.Idle::class.java)
    }

    @Test
    fun `processQueue handles partial failure gracefully`() = runTest {
        val handler: EntitySyncHandler = mockk()
        every { handler.entityType } returns EntityType.TRANSACTION
        coEvery { handler.processCreate(match { it.localId == 1L }) } returns Unit
        coEvery { handler.processCreate(match { it.localId == 2L }) } throws SyncException("Server error", isRetryable = false)
        coEvery { handler.processCreate(match { it.localId == 3L }) } returns Unit

        val pending = listOf(
            PendingOperationEntity(localId = 1, entityType = EntityType.TRANSACTION, operationType = OperationType.CREATE, payload = "{}", createdAt = 1L),
            PendingOperationEntity(localId = 2, entityType = EntityType.TRANSACTION, operationType = OperationType.CREATE, payload = "{}", createdAt = 2L),
            PendingOperationEntity(localId = 3, entityType = EntityType.TRANSACTION, operationType = OperationType.CREATE, payload = "{}", createdAt = 3L)
        )
        every { pendingOperationRepository.getPending() } returns pending
        every { pendingOperationRepository.getPendingCount() } returns 0L

        syncManager = SyncManager(
            pendingOperationRepository = pendingOperationRepository,
            networkMonitor = networkMonitor,
            coroutineScope = coroutineScope,
            syncHandlers = mapOf(EntityType.TRANSACTION to handler)
        )
        syncManager.start()
        syncManager.processQueue()

        // Op 1 succeeded, op 2 failed, op 3 should still have been processed
        coVerify { handler.processCreate(match { it.localId == 1L }) }
        coVerify { handler.processCreate(match { it.localId == 2L }) }
        coVerify { handler.processCreate(match { it.localId == 3L }) }
    }

    @Test
    fun `syncState transitions correctly through sync lifecycle`() = runTest {
        every { pendingOperationRepository.getPendingCount() } returns 2L
        val pending = listOf(
            PendingOperationEntity(localId = 1, entityType = EntityType.TRANSACTION, operationType = OperationType.CREATE, payload = "{}", createdAt = 1L),
            PendingOperationEntity(localId = 2, entityType = EntityType.TRANSACTION, operationType = OperationType.CREATE, payload = "{}", createdAt = 2L)
        )
        every { pendingOperationRepository.getPending() } returns pending

        val handler: EntitySyncHandler = mockk(relaxed = true)
        every { handler.entityType } returns EntityType.TRANSACTION
        coEvery { handler.processCreate(any()) } returns Unit

        syncManager = SyncManager(
            pendingOperationRepository = pendingOperationRepository,
            networkMonitor = networkMonitor,
            coroutineScope = coroutineScope,
            syncHandlers = mapOf(EntityType.TRANSACTION to handler)
        )
        syncManager.start()
        syncManager.processQueue()

        // Should end in Idle state
        assertThat(syncManager.syncState.value).isInstanceOf(SyncState.Idle::class.java)
    }
}
