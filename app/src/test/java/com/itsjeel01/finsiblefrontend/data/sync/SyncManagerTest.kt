package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.OperationType
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.SyncState
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.PendingOperationRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.objectbox.Box
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/** Unit tests for SyncManager queue processing and state management. */
@OptIn(ExperimentalCoroutinesApi::class)
class SyncManagerTest {

    private lateinit var mockPendingOperationBox: Box<PendingOperationEntity>
    private lateinit var mockPendingOperationRepository: PendingOperationRepository
    private lateinit var mockNetworkMonitor: NetworkMonitor
    private lateinit var testScope: CoroutineScope
    private lateinit var mockSyncHandler: EntitySyncHandler
    private lateinit var syncHandlers: Map<EntityType, EntitySyncHandler>
    private lateinit var syncManager: SyncManager
    private lateinit var networkStateFlow: MutableStateFlow<Boolean>

    @Before
    fun setUp() {
        mockPendingOperationBox = mockk(relaxed = true)
        mockPendingOperationRepository = mockk(relaxed = true)
        mockNetworkMonitor = mockk(relaxed = true)
        mockSyncHandler = mockk(relaxed = true)
        testScope = CoroutineScope(UnconfinedTestDispatcher())
        networkStateFlow = MutableStateFlow(false)  // Start offline to prevent auto-sync in init

        syncHandlers = mapOf(EntityType.TRANSACTION to mockSyncHandler)

        // Mock network monitor state flow
        every { mockNetworkMonitor.isOnline } returns networkStateFlow

        // Mock pending operation repository
        every { mockPendingOperationRepository.getPending() } returns emptyList()
        every { mockPendingOperationRepository.getPendingCount() } returns 0L
        every { mockPendingOperationRepository.getFailed() } returns emptyList()

        syncManager = SyncManager(
            mockPendingOperationBox,
            mockPendingOperationRepository,
            mockNetworkMonitor,
            testScope,
            syncHandlers
        )
    }

    // ============================================
    // Initialization Tests
    // ============================================

    @Test
    fun testInitialSyncStateIsIdle() {
        val initialState = syncManager.syncState.value
        assertTrue("Initial state should be Idle", initialState is SyncState.Idle)
    }

    @Test
    fun testInitialPendingCountIsZero() {
        val initialCount = syncManager.pendingCount.value
        assertEquals("Initial pending count should be 0", 0, initialCount)
    }

    @Test
    fun testPendingCountIsInitializedOnCreation() {
        val count = syncManager.pendingCount.value
        assertTrue("Pending count should be initialized", count >= 0)
        verify(atLeast = 1) { mockPendingOperationRepository.getPendingCount() }
    }

    @Test
    fun testSyncStateStateFlowIsAccessibleToCollectors() {
        val state = syncManager.syncState.value
        assertNotNull("SyncState should be accessible", state)
        assertTrue("Initial state should be Idle", state is SyncState.Idle)
    }

    @Test
    fun testPendingCountStateFlowIsAccessibleToCollectors() {
        val count = syncManager.pendingCount.value
        assertNotNull("Pending count should be accessible", count)
        assertTrue("Count should be non-negative", count >= 0)
    }

    @Test
    fun testMAX_RETRIESConstantIsDefined() {
        assertEquals("MAX_RETRIES should be 3", 3, SyncManager.MAX_RETRIES)
    }

    @Test
    fun testSyncHandlersAreRegistered() {
        // Handler map should contain TRANSACTION handler
        assertTrue("Should have registered handlers", syncHandlers.isNotEmpty())
        assertTrue("Should have TRANSACTION handler", syncHandlers.containsKey(EntityType.TRANSACTION))
    }

    // ============================================
    // Queue Processing Tests
    // ============================================

    @Test
    fun testProcessQueueWithEmptyQueue() = runTest {
        networkStateFlow.value = true  // Set network online for this test
        // Arrange
        every { mockPendingOperationRepository.getPending() } returns emptyList()

        // Act
        syncManager.processQueue()

        // Assert
        assertEquals("State should be Idle", SyncState.Idle, syncManager.syncState.value)
        verify(atLeast = 1) { mockPendingOperationRepository.getPending() }
    }

    @Test
    fun testProcessQueueWithSingleCreateOperation() = runTest {
        networkStateFlow.value = true  // Set network online for this test
        // Arrange
        networkStateFlow.value = true  // Set network online for this test

        val operation = PendingOperationEntity().apply {
            localId = 1L
            entityType = EntityType.TRANSACTION
            operationType = OperationType.CREATE
            status = Status.PENDING
            payload = "{}"
        }
        every { mockPendingOperationRepository.getPending() } returns listOf(operation)
        every { mockPendingOperationRepository.update(any()) } just Runs
        every { mockPendingOperationRepository.removeCompleted() } just Runs
        every { mockSyncHandler.entityType } returns EntityType.TRANSACTION
        coEvery { mockSyncHandler.processCreate(any()) } just Runs

        // Act
        syncManager.processQueue()

        // Assert
        coVerify(exactly = 1) { mockSyncHandler.processCreate(operation) }
        verify(exactly = 1) { mockPendingOperationRepository.update(operation) }
        verify(exactly = 1) { mockPendingOperationRepository.removeCompleted() }
        assertEquals("State should be Idle after completion", SyncState.Idle, syncManager.syncState.value)
    }

    @Test
    fun testProcessQueueWithSingleUpdateOperation() = runTest {
        networkStateFlow.value = true  // Set network online for this test
        // Arrange
        val operation = PendingOperationEntity().apply {
            localId = 2L
            entityType = EntityType.TRANSACTION
            operationType = OperationType.UPDATE
            entityId = 1001L
            status = Status.PENDING
            payload = "{}"
        }
        every { mockPendingOperationRepository.getPending() } returns listOf(operation)
        every { mockPendingOperationRepository.update(any()) } just Runs
        every { mockPendingOperationRepository.removeCompleted() } just Runs
        every { mockSyncHandler.entityType } returns EntityType.TRANSACTION
        coEvery { mockSyncHandler.processUpdate(any()) } just Runs

        // Act
        syncManager.processQueue()

        // Assert
        coVerify(exactly = 1) { mockSyncHandler.processUpdate(operation) }
        verify(exactly = 1) { mockPendingOperationRepository.update(operation) }
    }

    @Test
    fun testProcessQueueWithSingleDeleteOperation() = runTest {
        networkStateFlow.value = true  // Set network online for this test
        // Arrange
        val operation = PendingOperationEntity().apply {
            localId = 3L
            entityType = EntityType.TRANSACTION
            operationType = OperationType.DELETE
            entityId = 1002L
            status = Status.PENDING
            payload = "{}"
        }
        every { mockPendingOperationRepository.getPending() } returns listOf(operation)
        every { mockPendingOperationRepository.update(any()) } just Runs
        every { mockPendingOperationRepository.removeCompleted() } just Runs
        every { mockSyncHandler.entityType } returns EntityType.TRANSACTION
        coEvery { mockSyncHandler.processDelete(any()) } just Runs

        // Act
        syncManager.processQueue()

        // Assert
        coVerify(exactly = 1) { mockSyncHandler.processDelete(operation) }
        verify(exactly = 1) { mockPendingOperationRepository.update(operation) }
    }

    @Test
    fun testProcessQueueWithMultipleOperations() = runTest {
        networkStateFlow.value = true  // Set network online for this test
        // Arrange
        val op1 = PendingOperationEntity().apply {
            localId = 1L
            entityType = EntityType.TRANSACTION
            operationType = OperationType.CREATE
            status = Status.PENDING
            payload = "{}"
        }
        val op2 = PendingOperationEntity().apply {
            localId = 2L
            entityType = EntityType.TRANSACTION
            operationType = OperationType.UPDATE
            entityId = 1001L
            status = Status.PENDING
            payload = "{}"
        }
        val op3 = PendingOperationEntity().apply {
            localId = 3L
            entityType = EntityType.TRANSACTION
            operationType = OperationType.DELETE
            entityId = 1002L
            status = Status.PENDING
            payload = "{}"
        }

        every { mockPendingOperationRepository.getPending() } returns listOf(op1, op2, op3)
        every { mockPendingOperationRepository.update(any()) } just Runs
        every { mockPendingOperationRepository.removeCompleted() } just Runs
        every { mockSyncHandler.entityType } returns EntityType.TRANSACTION
        coEvery { mockSyncHandler.processCreate(any()) } just Runs
        coEvery { mockSyncHandler.processUpdate(any()) } just Runs
        coEvery { mockSyncHandler.processDelete(any()) } just Runs

        // Act
        syncManager.processQueue()

        // Assert
        coVerify(exactly = 1) { mockSyncHandler.processCreate(op1) }
        coVerify(exactly = 1) { mockSyncHandler.processUpdate(op2) }
        coVerify(exactly = 1) { mockSyncHandler.processDelete(op3) }
        verify(exactly = 3) { mockPendingOperationRepository.update(any()) }
        verify(exactly = 1) { mockPendingOperationRepository.removeCompleted() }
    }

    // ============================================
    // Error Handling Tests
    // ============================================

    @Test
    fun testProcessQueueWithRetryableError() = runTest {
        networkStateFlow.value = true  // Set network online for this test
        // Arrange
        val operation = PendingOperationEntity().apply {
            localId = 4L
            entityType = EntityType.TRANSACTION
            operationType = OperationType.CREATE
            status = Status.PENDING
            retryCount = 0
            payload = "{}"
        }
        every { mockPendingOperationRepository.getPending() } returns listOf(operation)
        every { mockPendingOperationRepository.update(any()) } just Runs
        every { mockPendingOperationRepository.removeCompleted() } just Runs
        every { mockSyncHandler.entityType } returns EntityType.TRANSACTION
        coEvery { mockSyncHandler.processCreate(any()) } throws SyncException("Network error", isRetryable = true)

        // Act
        syncManager.processQueue()

        // Assert
        assertEquals("Operation should still be PENDING", Status.PENDING, operation.status)
        assertEquals("Retry count should increment", 1, operation.retryCount)
        assertNotNull("Error message should be set", operation.lastError)
        verify(exactly = 1) { mockPendingOperationRepository.update(operation) }
    }

    @Test
    fun testProcessQueueWithNonRetryableError() = runTest {
        networkStateFlow.value = true  // Set network online for this test
        // Arrange
        val operation = PendingOperationEntity().apply {
            localId = 5L
            entityType = EntityType.TRANSACTION
            operationType = OperationType.UPDATE
            entityId = 1003L
            status = Status.PENDING
            retryCount = 0
            payload = "{}"
        }

        every { mockPendingOperationRepository.getPending() } returns listOf(operation)
        every { mockPendingOperationRepository.update(any()) } just Runs
        every { mockPendingOperationRepository.removeCompleted() } just Runs
        every { mockSyncHandler.entityType } returns EntityType.TRANSACTION
        coEvery { mockSyncHandler.processUpdate(any()) } throws SyncException("Unauthorized", isRetryable = false)

        // Act
        syncManager.processQueue()

        // Assert
        assertEquals("Operation should be marked FAILED", Status.FAILED, operation.status)
        assertEquals("Retry count should increment", 1, operation.retryCount)
        assertNotNull("Error message should be set", operation.lastError)
        assertTrue("State should be Idle after processing", syncManager.syncState.value is SyncState.Idle)
    }

    @Test
    fun testProcessQueueWithMaxRetriesExceeded() = runTest {
        networkStateFlow.value = true  // Set network online for this test
        // Arrange
        val operation = PendingOperationEntity().apply {
            localId = 6L
            entityType = EntityType.TRANSACTION
            operationType = OperationType.CREATE
            status = Status.PENDING
            retryCount = SyncManager.MAX_RETRIES - 1  // At max retries
            payload = "{}"
        }

        every { mockPendingOperationRepository.getPending() } returns listOf(operation)
        every { mockPendingOperationRepository.update(any()) } just Runs
        every { mockPendingOperationRepository.removeCompleted() } just Runs
        every { mockSyncHandler.entityType } returns EntityType.TRANSACTION
        coEvery { mockSyncHandler.processCreate(any()) } throws SyncException("Still failing", isRetryable = true)

        // Act
        syncManager.processQueue()

        // Assert
        assertEquals("Operation should be marked FAILED", Status.FAILED, operation.status)
        assertEquals("Retry count should reach max", SyncManager.MAX_RETRIES, operation.retryCount)
        assertNotNull("Error message should be set", operation.lastError)
        assertTrue("State should be Idle after processing", syncManager.syncState.value is SyncState.Idle)
    }

    @Test
    fun testProcessQueueWithUnexpectedException() = runTest {
        networkStateFlow.value = true  // Set network online for this test
        // Arrange
        val operation = PendingOperationEntity().apply {
            localId = 7L
            entityType = EntityType.TRANSACTION
            operationType = OperationType.DELETE
            entityId = 1004L
            status = Status.PENDING
            payload = "{}"
        }
        every { mockPendingOperationRepository.getPending() } returns listOf(operation)
        every { mockPendingOperationRepository.update(any()) } just Runs
        every { mockPendingOperationRepository.removeCompleted() } just Runs
        every { mockSyncHandler.entityType } returns EntityType.TRANSACTION
        coEvery { mockSyncHandler.processDelete(any()) } throws RuntimeException("Unexpected error")

        // Act
        syncManager.processQueue()

        // Assert
        assertEquals("Retry count should increment", 1, operation.retryCount)
        assertNotNull("Error message should be set", operation.lastError)
    }

    @Test
    fun testProcessQueueWithMissingHandler() = runTest {
        networkStateFlow.value = true  // Set network online for this test
        // Arrange
        val operation = PendingOperationEntity().apply {
            localId = 8L
            entityType = EntityType.ACCOUNT  // No handler registered
            operationType = OperationType.CREATE
            status = Status.PENDING
            payload = "{}"
        }
        every { mockPendingOperationRepository.getPending() } returns listOf(operation)
        every { mockPendingOperationRepository.update(any()) } just Runs
        every { mockPendingOperationRepository.removeCompleted() } just Runs

        // Act
        syncManager.processQueue()

        // Assert
        assertEquals("Operation should be FAILED", Status.FAILED, operation.status)
        assertTrue("Error should indicate no handler", operation.lastError!!.contains("No handler"))
    }

    @Test
    fun testProcessQueueWithNullOperationType() = runTest {
        networkStateFlow.value = true  // Set network online for this test
        // Arrange
        val operation = PendingOperationEntity().apply {
            localId = 9L
            entityType = EntityType.TRANSACTION
            operationType = null  // Invalid
            status = Status.PENDING
            payload = "{}"
        }
        every { mockPendingOperationRepository.getPending() } returns listOf(operation)
        every { mockPendingOperationRepository.update(any()) } just Runs
        every { mockPendingOperationRepository.removeCompleted() } just Runs
        every { mockSyncHandler.entityType } returns EntityType.TRANSACTION

        // Act
        syncManager.processQueue()

        // Assert
        assertEquals("Operation should be FAILED", Status.FAILED, operation.status)
        assertTrue("Error should indicate null operation type", operation.lastError!!.contains("Operation type is null"))
    }

    // ============================================
    // Network Monitoring Tests
    // ============================================

    @Test
    fun testProcessQueuePausesWhenNetworkLost() = runTest {
        networkStateFlow.value = true  // Set network online initially

        // Arrange
        val op1 = PendingOperationEntity().apply {
            localId = 10L
            entityType = EntityType.TRANSACTION
            operationType = OperationType.CREATE
            status = Status.PENDING
            payload = "{}"
        }
        val op2 = PendingOperationEntity().apply {
            localId = 11L
            entityType = EntityType.TRANSACTION
            operationType = OperationType.UPDATE
            entityId = 1005L
            status = Status.PENDING
            payload = "{}"
        }

        every { mockPendingOperationRepository.getPending() } returns listOf(op1, op2)
        every { mockPendingOperationRepository.update(any()) } just Runs
        every { mockPendingOperationRepository.removeCompleted() } just Runs
        every { mockSyncHandler.entityType } returns EntityType.TRANSACTION
        coEvery { mockSyncHandler.processCreate(any()) } answers {
            networkStateFlow.value = false  // Lose network mid-sync
        }

        // Act
        syncManager.processQueue()

        // Assert
        assertEquals("State should be Idle after network lost", SyncState.Idle, syncManager.syncState.value)
        coVerify(exactly = 1) { mockSyncHandler.processCreate(op1) }
        coVerify(exactly = 0) { mockSyncHandler.processUpdate(op2) }  // Should not process second op
    }

    // ============================================
    // Retry Failed Tests
    // ============================================

    @Test
    fun testRetryFailedResetsPendingOperations() = runTest {
        // Arrange
        val failedOp1 = PendingOperationEntity().apply {
            localId = 12L
            entityType = EntityType.TRANSACTION
            operationType = OperationType.CREATE
            status = Status.FAILED
            retryCount = 3
            lastError = "Previous error"
            payload = "{}"
        }
        val failedOp2 = PendingOperationEntity().apply {
            localId = 13L
            entityType = EntityType.TRANSACTION
            operationType = OperationType.UPDATE
            entityId = 1006L
            status = Status.FAILED
            retryCount = 2
            lastError = "Another error"
            payload = "{}"
        }

        every { mockPendingOperationRepository.getFailed() } returns listOf(failedOp1, failedOp2)
        every { mockPendingOperationRepository.update(any()) } just Runs
        every { mockPendingOperationRepository.getPending() } returns emptyList()

        // Act
        syncManager.retryFailed()

        // Assert
        assertEquals("Status should be reset to PENDING", Status.PENDING, failedOp1.status)
        assertEquals("Retry count should be reset", 0, failedOp1.retryCount)
        assertNull("Error should be cleared", failedOp1.lastError)

        assertEquals("Status should be reset to PENDING", Status.PENDING, failedOp2.status)
        assertEquals("Retry count should be reset", 0, failedOp2.retryCount)
        assertNull("Error should be cleared", failedOp2.lastError)

        verify(exactly = 2) { mockPendingOperationRepository.update(any()) }
    }

    // ============================================
    // Concurrent Sync Protection Tests
    // ============================================

    @Test
    fun testProcessQueueDoesNotRunConcurrently() = runTest {
        networkStateFlow.value = true  // Set network online for this test

        // Arrange - first call will be "in progress"
        val operation = PendingOperationEntity().apply {
            localId = 14L
            entityType = EntityType.TRANSACTION
            operationType = OperationType.CREATE
            status = Status.PENDING
            payload = "{}"
        }
        every { mockPendingOperationRepository.getPending() } returns listOf(operation)
        every { mockPendingOperationRepository.update(any()) } just Runs
        every { mockPendingOperationRepository.removeCompleted() } just Runs
        every { mockSyncHandler.entityType } returns EntityType.TRANSACTION

        var processCount = 0
        coEvery { mockSyncHandler.processCreate(any()) } coAnswers {
            processCount++
            // Try to call processQueue again while first is running
            if (processCount == 1) {
                syncManager.processQueue()
            }
        }

        // Act
        syncManager.processQueue()

        // Assert
        assertEquals("Handler should only be called once", 1, processCount)
    }
}


