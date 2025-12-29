package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.SyncState
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import io.mockk.every
import io.mockk.mockk
import io.objectbox.Box
import io.objectbox.query.Query
import io.objectbox.query.QueryBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/** Unit tests for SyncManager queue processing and state management. */
class SyncManagerTest {

    private lateinit var mockPendingOperationBox: Box<PendingOperationEntity>
    private lateinit var mockNetworkMonitor: NetworkMonitor
    private lateinit var testScope: CoroutineScope
    private lateinit var syncManager: SyncManager
    private lateinit var networkStateFlow: MutableStateFlow<Boolean>

    @Before
    fun setUp() {
        mockPendingOperationBox = mockk(relaxed = true)
        mockNetworkMonitor = mockk(relaxed = true)
        testScope = CoroutineScope(Dispatchers.Unconfined)
        networkStateFlow = MutableStateFlow(false)

        // Mock network monitor state flow
        every { mockNetworkMonitor.isOnline } returns networkStateFlow

        // Mock ObjectBox query chain
        val mockQuery: Query<PendingOperationEntity> = mockk(relaxed = true)
        val mockQueryBuilder: QueryBuilder<PendingOperationEntity> = mockk(relaxed = true)

        every { mockPendingOperationBox.query() } returns mockQueryBuilder
        every { mockQueryBuilder.build() } returns mockQuery
        every { mockQuery.count() } returns 0L
    }

    @Test
    fun testInitialSyncStateIsIdle() {
        syncManager = SyncManager(mockPendingOperationBox, mockNetworkMonitor, testScope)

        val initialState = syncManager.syncState.value
        assertTrue("Initial state should be Idle", initialState is SyncState.Idle)
    }

    @Test
    fun testInitialPendingCountIsZero() {
        syncManager = SyncManager(mockPendingOperationBox, mockNetworkMonitor, testScope)

        val initialCount = syncManager.pendingCount.value
        assertEquals("Initial pending count should be 0", 0, initialCount)
    }

    @Test
    fun testPendingCountIsInitializedOnCreation() {
        syncManager = SyncManager(mockPendingOperationBox, mockNetworkMonitor, testScope)

        // Verify pending count is initialized (value may be 0 or based on mock)
        val count = syncManager.pendingCount.value
        assertTrue("Pending count should be initialized", count >= 0)
    }

    @Test
    fun testSyncStateStateFlowIsAccessibleToCollectors() {
        syncManager = SyncManager(mockPendingOperationBox, mockNetworkMonitor, testScope)

        val state = syncManager.syncState.value
        assertNotNull("SyncState should be accessible", state)
        assertTrue("Initial state should be Idle", state is SyncState.Idle)
    }

    @Test
    fun testPendingCountStateFlowIsAccessibleToCollectors() {
        syncManager = SyncManager(mockPendingOperationBox, mockNetworkMonitor, testScope)

        val count = syncManager.pendingCount.value
        assertNotNull("Pending count should be accessible", count)
        assertTrue("Count should be non-negative", count >= 0)
    }

    @Test
    fun testMAX_RETRIESConstantIsDefined() {
        assertEquals("MAX_RETRIES should be 3", 3, SyncManager.MAX_RETRIES)
    }

    @Test
    fun testPendingCountQueriesObjectBox() {
        syncManager = SyncManager(mockPendingOperationBox, mockNetworkMonitor, testScope)

        // Verify that query() was called during initialization
        io.mockk.verify(atLeast = 1) { mockPendingOperationBox.query() }
    }
}


