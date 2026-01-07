package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.OperationType
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountGroupLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.CategoryLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.PendingOperationRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.TransactionLocalRepository
import com.itsjeel01.finsiblefrontend.data.remote.api.SyncApiService
import com.itsjeel01.finsiblefrontend.data.remote.model.EntitySnapshot
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/** Unit tests for IntegrityChecker with pending operations. */
class IntegrityCheckerPendingOpsTest {

    private lateinit var categoryLocalRepo: CategoryLocalRepository
    private lateinit var accountGroupLocalRepo: AccountGroupLocalRepository
    private lateinit var accountLocalRepo: AccountLocalRepository
    private lateinit var transactionLocalRepo: TransactionLocalRepository
    private lateinit var pendingOperationRepo: PendingOperationRepository
    private lateinit var syncApi: SyncApiService
    private lateinit var integrityChecker: IntegrityChecker

    @Before
    fun setup() {
        categoryLocalRepo = mockk(relaxed = true)
        accountGroupLocalRepo = mockk(relaxed = true)
        accountLocalRepo = mockk(relaxed = true)
        transactionLocalRepo = mockk(relaxed = true)
        pendingOperationRepo = mockk(relaxed = true)
        syncApi = mockk(relaxed = true)

        // Default: no pending operations
        every { pendingOperationRepo.getPending() } returns emptyList()

        integrityChecker = IntegrityChecker(
            categoryLocalRepo,
            accountGroupLocalRepo,
            accountLocalRepo,
            transactionLocalRepo,
            pendingOperationRepo,
            syncApi
        )
    }

    @Test
    fun `verifyCategoriesIntegrity accounts for pending CREATE operations`() = runTest {
        // Given: Local has 12 categories (10 + 2 pending creates), server has 10
        every { categoryLocalRepo.getAll() } returns List(12) { mockk() }
        every { pendingOperationRepo.getPending() } returns listOf(
            PendingOperationEntity(
                localId = 1,
                entityType = EntityType.CATEGORY,
                operationType = OperationType.CREATE,
                status = Status.PENDING
            ),
            PendingOperationEntity(
                localId = 2,
                entityType = EntityType.CATEGORY,
                operationType = OperationType.CREATE,
                status = Status.PENDING
            )
        )
        coEvery { syncApi.getSnapshot() } returns EntitySnapshot(
            categories = 10,
            accountGroups = 5,
            accounts = 8,
            transactions = 100
        )

        // When: Expected server count = 12 (local) - 2 (pending creates) = 10
        val result = integrityChecker.verifyCategoriesIntegrity()

        // Then: Should match
        assertTrue("Expected integrity to pass when pending creates are accounted for", result)
    }

    @Test
    fun `verifyCategoriesIntegrity accounts for pending DELETE operations`() = runTest {
        // Given: Local has 8 categories (10 - 2 pending deletes), server has 10
        every { categoryLocalRepo.getAll() } returns List(8) { mockk() }
        every { pendingOperationRepo.getPending() } returns listOf(
            PendingOperationEntity(
                localId = 1,
                entityType = EntityType.CATEGORY,
                operationType = OperationType.DELETE,
                status = Status.PENDING
            ),
            PendingOperationEntity(
                localId = 2,
                entityType = EntityType.CATEGORY,
                operationType = OperationType.DELETE,
                status = Status.PENDING
            )
        )
        coEvery { syncApi.getSnapshot() } returns EntitySnapshot(
            categories = 10,
            accountGroups = 5,
            accounts = 8,
            transactions = 100
        )

        // When: Expected server count = 8 (local) + 2 (pending deletes) = 10
        val result = integrityChecker.verifyCategoriesIntegrity()

        // Then: Should match
        assertTrue("Expected integrity to pass when pending deletes are accounted for", result)
    }

    @Test
    fun `verifyCategoriesIntegrity ignores pending UPDATE operations`() = runTest {
        // Given: Local has 10 categories with 2 pending updates, server has 10
        every { categoryLocalRepo.getAll() } returns List(10) { mockk() }
        every { pendingOperationRepo.getPending() } returns listOf(
            PendingOperationEntity(
                localId = 1,
                entityType = EntityType.CATEGORY,
                operationType = OperationType.UPDATE,
                status = Status.PENDING
            ),
            PendingOperationEntity(
                localId = 2,
                entityType = EntityType.CATEGORY,
                operationType = OperationType.UPDATE,
                status = Status.PENDING
            )
        )
        coEvery { syncApi.getSnapshot() } returns EntitySnapshot(
            categories = 10,
            accountGroups = 5,
            accounts = 8,
            transactions = 100
        )

        // When: Expected server count = 10 (local), updates don't affect count
        val result = integrityChecker.verifyCategoriesIntegrity()

        // Then: Should match
        assertTrue("Expected integrity to pass when only UPDATEs are pending", result)
    }

    @Test
    fun `verifyTransactionsIntegrity accounts for mixed pending operations`() = runTest {
        // Given: Local has 105 transactions (100 + 7 creates - 2 deletes), server has 100
        every { transactionLocalRepo.getTotalTransactionCount() } returns 105L
        every { pendingOperationRepo.getPending() } returns listOf(
            // 7 pending creates
            *Array(7) {
                PendingOperationEntity(
                    localId = it.toLong(),
                    entityType = EntityType.TRANSACTION,
                    operationType = OperationType.CREATE,
                    status = Status.PENDING
                )
            },
            // 2 pending deletes
            *Array(2) {
                PendingOperationEntity(
                    localId = (it + 7).toLong(),
                    entityType = EntityType.TRANSACTION,
                    operationType = OperationType.DELETE,
                    status = Status.PENDING
                )
            },
            // 3 pending updates (should be ignored)
            *Array(3) {
                PendingOperationEntity(
                    localId = (it + 9).toLong(),
                    entityType = EntityType.TRANSACTION,
                    operationType = OperationType.UPDATE,
                    status = Status.PENDING
                )
            }
        )
        coEvery { syncApi.getSnapshot() } returns EntitySnapshot(
            categories = 10,
            accountGroups = 5,
            accounts = 8,
            transactions = 100
        )

        // When: Expected = 105 - 7 (creates) + 2 (deletes) = 100
        val result = integrityChecker.verifyTransactionsIntegrity()

        // Then: Should match
        assertTrue("Expected integrity to pass with mixed pending operations", result)
    }

    @Test
    fun `verifyTransactionsIntegrity only counts pending ops for its entity type`() = runTest {
        // Given: Pending ops for multiple entity types
        every { transactionLocalRepo.getTotalTransactionCount() } returns 102L
        every { pendingOperationRepo.getPending() } returns listOf(
            // 2 pending transaction creates
            PendingOperationEntity(
                localId = 1,
                entityType = EntityType.TRANSACTION,
                operationType = OperationType.CREATE,
                status = Status.PENDING
            ),
            PendingOperationEntity(
                localId = 2,
                entityType = EntityType.TRANSACTION,
                operationType = OperationType.CREATE,
                status = Status.PENDING
            ),
            // 5 pending category creates (should be ignored for transactions)
            *Array(5) {
                PendingOperationEntity(
                    localId = (it + 3).toLong(),
                    entityType = EntityType.CATEGORY,
                    operationType = OperationType.CREATE,
                    status = Status.PENDING
                )
            }
        )
        coEvery { syncApi.getSnapshot() } returns EntitySnapshot(
            categories = 10,
            accountGroups = 5,
            accounts = 8,
            transactions = 100
        )

        // When: Expected = 102 - 2 (transaction creates only) = 100
        val result = integrityChecker.verifyTransactionsIntegrity()

        // Then: Should match
        assertTrue("Expected to only count pending ops for transactions", result)
    }

    @Test
    fun `verifyAllIntegrity returns report with pending operations accounted`() = runTest {
        // Given: Mixed state with pending operations
        every { categoryLocalRepo.getAll() } returns List(11) { mockk() }
        every { accountGroupLocalRepo.getAll() } returns List(5) { mockk() }
        every { accountLocalRepo.getAll() } returns List(7) { mockk() }
        every { transactionLocalRepo.getTotalTransactionCount() } returns 101L

        every { pendingOperationRepo.getPending() } returns listOf(
            // 1 pending category create
            PendingOperationEntity(
                localId = 1,
                entityType = EntityType.CATEGORY,
                operationType = OperationType.CREATE,
                status = Status.PENDING
            ),
            // 1 pending account delete
            PendingOperationEntity(
                localId = 2,
                entityType = EntityType.ACCOUNT,
                operationType = OperationType.DELETE,
                status = Status.PENDING
            ),
            // 1 pending transaction create
            PendingOperationEntity(
                localId = 3,
                entityType = EntityType.TRANSACTION,
                operationType = OperationType.CREATE,
                status = Status.PENDING
            )
        )

        val snapshot = EntitySnapshot(
            categories = 10,  // 11 - 1 create = 10 ✓
            accountGroups = 5, // 5 (no pending) = 5 ✓
            accounts = 8,      // 7 + 1 delete = 8 ✓
            transactions = 100 // 101 - 1 create = 100 ✓
        )
        coEvery { syncApi.getSnapshot() } returns snapshot

        // When
        val report = integrityChecker.verifyAllIntegrity()

        // Then: All should match
        assertTrue("Categories should match", report.categoriesMatch)
        assertTrue("Account groups should match", report.accountGroupsMatch)
        assertTrue("Accounts should match", report.accountsMatch)
        assertTrue("Transactions should match", report.transactionsMatch)
        assertFalse("Should not have discrepancy", report.hasDiscrepancy)
        assertEquals("Snapshot should be attached", snapshot, report.serverSnapshot)
    }

    @Test
    fun `integrity check detects real discrepancy despite pending operations`() = runTest {
        // Given: Local has 10, 1 pending create, server has 12 (true discrepancy)
        every { categoryLocalRepo.getAll() } returns List(10) { mockk() }
        every { pendingOperationRepo.getPending() } returns listOf(
            PendingOperationEntity(
                localId = 1,
                entityType = EntityType.CATEGORY,
                operationType = OperationType.CREATE,
                status = Status.PENDING
            )
        )
        coEvery { syncApi.getSnapshot() } returns EntitySnapshot(
            categories = 12,
            accountGroups = 5,
            accounts = 8,
            transactions = 100
        )

        // When: Expected = 10 - 1 = 9, server = 12 (mismatch)
        val result = integrityChecker.verifyCategoriesIntegrity()

        // Then: Should detect the real discrepancy
        assertFalse("Expected integrity check to fail when real discrepancy exists", result)
    }
}

