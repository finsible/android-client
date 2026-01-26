package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.data.local.repository.AccountGroupLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.CategoryLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.PendingOperationRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.TransactionLocalRepository
import com.itsjeel01.finsiblefrontend.data.remote.api.SyncApiService
import com.itsjeel01.finsiblefrontend.data.remote.model.EntitySnapshot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/** Unit tests for IntegrityChecker snapshot-based verification. */
class IntegrityCheckerTest {

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
    fun `verifyCategoriesIntegrity returns true when counts match`() = runTest {
        // Given: Local has 10 categories, server has 10
        every { categoryLocalRepo.getAll() } returns List(10) { mockk() }
        coEvery { syncApi.getSnapshot() } returns EntitySnapshot(
            categories = 10,
            accountGroups = 5,
            accounts = 8,
            transactions = 100
        )

        // When
        val result = integrityChecker.verifyCategoriesIntegrity()

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { syncApi.getSnapshot() }
    }

    @Test
    fun `verifyCategoriesIntegrity returns false when counts mismatch`() = runTest {
        // Given: Local has 8 categories, server has 10
        every { categoryLocalRepo.getAll() } returns List(8) { mockk() }
        coEvery { syncApi.getSnapshot() } returns EntitySnapshot(
            categories = 10,
            accountGroups = 5,
            accounts = 8,
            transactions = 100
        )

        // When
        val result = integrityChecker.verifyCategoriesIntegrity()

        // Then
        assertFalse(result)
    }

    @Test
    fun `verifyCategoriesIntegrity returns true on network error`() = runTest {
        // Given: Network error
        every { categoryLocalRepo.getAll() } returns List(10) { mockk() }
        coEvery { syncApi.getSnapshot() } throws Exception("Network error")

        // When
        val result = integrityChecker.verifyCategoriesIntegrity()

        // Then: Assume valid on error
        assertTrue(result)
    }

    @Test
    fun `verifyAccountGroupsIntegrity returns true when counts match`() = runTest {
        // Given
        every { accountGroupLocalRepo.getAll() } returns List(5) { mockk() }
        coEvery { syncApi.getSnapshot() } returns EntitySnapshot(
            categories = 10,
            accountGroups = 5,
            accounts = 8,
            transactions = 100
        )

        // When
        val result = integrityChecker.verifyAccountGroupsIntegrity()

        // Then
        assertTrue(result)
    }

    @Test
    fun `verifyAccountGroupsIntegrity returns false when counts mismatch`() = runTest {
        // Given: Local has 3, server has 5
        every { accountGroupLocalRepo.getAll() } returns List(3) { mockk() }
        coEvery { syncApi.getSnapshot() } returns EntitySnapshot(
            categories = 10,
            accountGroups = 5,
            accounts = 8,
            transactions = 100
        )

        // When
        val result = integrityChecker.verifyAccountGroupsIntegrity()

        // Then
        assertFalse(result)
    }

    @Test
    fun `verifyAccountsIntegrity returns true when counts match`() = runTest {
        // Given
        every { accountLocalRepo.getAll() } returns List(8) { mockk() }
        coEvery { syncApi.getSnapshot() } returns EntitySnapshot(
            categories = 10,
            accountGroups = 5,
            accounts = 8,
            transactions = 100
        )

        // When
        val result = integrityChecker.verifyAccountsIntegrity()

        // Then
        assertTrue(result)
    }

    @Test
    fun `verifyAccountsIntegrity returns false when counts mismatch`() = runTest {
        // Given: Local has 6, server has 8
        every { accountLocalRepo.getAll() } returns List(6) { mockk() }
        coEvery { syncApi.getSnapshot() } returns EntitySnapshot(
            categories = 10,
            accountGroups = 5,
            accounts = 8,
            transactions = 100
        )

        // When
        val result = integrityChecker.verifyAccountsIntegrity()

        // Then
        assertFalse(result)
    }

    @Test
    fun `verifyTransactionsIntegrity returns true when counts match`() = runTest {
        // Given
        every { transactionLocalRepo.getTotalTransactionCount() } returns 100L
        coEvery { syncApi.getSnapshot() } returns EntitySnapshot(
            categories = 10,
            accountGroups = 5,
            accounts = 8,
            transactions = 100
        )

        // When
        val result = integrityChecker.verifyTransactionsIntegrity()

        // Then
        assertTrue(result)
    }

    @Test
    fun `verifyTransactionsIntegrity returns false when counts mismatch`() = runTest {
        // Given: Local has 95, server has 100
        every { transactionLocalRepo.getTotalTransactionCount() } returns 95L
        coEvery { syncApi.getSnapshot() } returns EntitySnapshot(
            categories = 10,
            accountGroups = 5,
            accounts = 8,
            transactions = 100
        )

        // When
        val result = integrityChecker.verifyTransactionsIntegrity()

        // Then
        assertFalse(result)
    }

    @Test
    fun `verifyAllIntegrity returns report with all matches`() = runTest {
        // Given: All counts match
        every { categoryLocalRepo.getAll() } returns List(10) { mockk() }
        every { accountGroupLocalRepo.getAll() } returns List(5) { mockk() }
        every { accountLocalRepo.getAll() } returns List(8) { mockk() }
        every { transactionLocalRepo.getTotalTransactionCount() } returns 100L

        val snapshot = EntitySnapshot(
            categories = 10,
            accountGroups = 5,
            accounts = 8,
            transactions = 100
        )
        coEvery { syncApi.getSnapshot() } returns snapshot

        // When
        val report = integrityChecker.verifyAllIntegrity()

        // Then
        assertTrue(report.categoriesMatch)
        assertTrue(report.accountGroupsMatch)
        assertTrue(report.accountsMatch)
        assertTrue(report.transactionsMatch)
        assertFalse(report.hasDiscrepancy)
        assertEquals(snapshot, report.serverSnapshot)
    }

    @Test
    fun `verifyAllIntegrity returns report with partial mismatch`() = runTest {
        // Given: Categories and transactions mismatch
        every { categoryLocalRepo.getAll() } returns List(8) { mockk() }
        every { accountGroupLocalRepo.getAll() } returns List(5) { mockk() }
        every { accountLocalRepo.getAll() } returns List(8) { mockk() }
        every { transactionLocalRepo.getTotalTransactionCount() } returns 95L

        coEvery { syncApi.getSnapshot() } returns EntitySnapshot(
            categories = 10,
            accountGroups = 5,
            accounts = 8,
            transactions = 100
        )

        // When
        val report = integrityChecker.verifyAllIntegrity()

        // Then
        assertFalse(report.categoriesMatch)
        assertTrue(report.accountGroupsMatch)
        assertTrue(report.accountsMatch)
        assertFalse(report.transactionsMatch)
        assertTrue(report.hasDiscrepancy)
    }

    @Test
    fun `verifyAllIntegrity returns valid report on network error`() = runTest {
        // Given: Network error
        every { categoryLocalRepo.getAll() } returns List(10) { mockk() }
        coEvery { syncApi.getSnapshot() } throws Exception("Network error")

        // When
        val report = integrityChecker.verifyAllIntegrity()

        // Then: Assume all valid on error
        assertTrue(report.categoriesMatch)
        assertTrue(report.accountGroupsMatch)
        assertTrue(report.accountsMatch)
        assertTrue(report.transactionsMatch)
        assertFalse(report.hasDiscrepancy)
        assertNull(report.serverSnapshot)
    }

    @Test
    fun `verifyAllIntegrity returns report with all mismatches`() = runTest {
        // Given: All counts mismatch
        every { categoryLocalRepo.getAll() } returns List(8) { mockk() }
        every { accountGroupLocalRepo.getAll() } returns List(3) { mockk() }
        every { accountLocalRepo.getAll() } returns List(6) { mockk() }
        every { transactionLocalRepo.getTotalTransactionCount() } returns 95L

        coEvery { syncApi.getSnapshot() } returns EntitySnapshot(
            categories = 10,
            accountGroups = 5,
            accounts = 8,
            transactions = 100
        )

        // When
        val report = integrityChecker.verifyAllIntegrity()

        // Then
        assertFalse(report.categoriesMatch)
        assertFalse(report.accountGroupsMatch)
        assertFalse(report.accountsMatch)
        assertFalse(report.transactionsMatch)
        assertTrue(report.hasDiscrepancy)
    }

    @Test
    fun `verifyAllIntegrity handles zero counts correctly`() = runTest {
        // Given: Empty local DB, empty server
        every { categoryLocalRepo.getAll() } returns emptyList()
        every { accountGroupLocalRepo.getAll() } returns emptyList()
        every { accountLocalRepo.getAll() } returns emptyList()
        every { transactionLocalRepo.getTotalTransactionCount() } returns 0L

        coEvery { syncApi.getSnapshot() } returns EntitySnapshot(
            categories = 0,
            accountGroups = 0,
            accounts = 0,
            transactions = 0
        )

        // When
        val report = integrityChecker.verifyAllIntegrity()

        // Then: All match with zero
        assertTrue(report.categoriesMatch)
        assertTrue(report.accountGroupsMatch)
        assertTrue(report.accountsMatch)
        assertTrue(report.transactionsMatch)
        assertFalse(report.hasDiscrepancy)
    }

    // ============================================
    // IntegrityReport Unit Tests
    // ============================================

    @Test
    fun `IntegrityReport hasDiscrepancy returns false when all match`() {
        val report = IntegrityReport(
            categoriesMatch = true,
            accountGroupsMatch = true,
            accountsMatch = true,
            transactionsMatch = true,
            networkAvailable = true
        )

        assertFalse(report.hasDiscrepancy)
    }

    @Test
    fun `IntegrityReport hasDiscrepancy returns true when any mismatch`() {
        val reportWithCategoryMismatch = IntegrityReport(
            categoriesMatch = false,
            accountGroupsMatch = true,
            accountsMatch = true,
            transactionsMatch = true,
            networkAvailable = true
        )
        assertTrue(reportWithCategoryMismatch.hasDiscrepancy)

        val reportWithTransactionMismatch = IntegrityReport(
            categoriesMatch = true,
            accountGroupsMatch = true,
            accountsMatch = true,
            transactionsMatch = false,
            networkAvailable = true
        )
        assertTrue(reportWithTransactionMismatch.hasDiscrepancy)
    }

    @Test
    fun `IntegrityReport hasDiscrepancy returns false when network unavailable`() {
        val report = IntegrityReport(
            categoriesMatch = false,
            accountGroupsMatch = false,
            accountsMatch = false,
            transactionsMatch = false,
            networkAvailable = false
        )

        assertFalse("Should not report discrepancy when offline", report.hasDiscrepancy)
    }

    @Test
    fun `IntegrityReport canResolve returns true when has discrepancy and network available`() {
        val report = IntegrityReport(
            categoriesMatch = false,
            accountGroupsMatch = true,
            accountsMatch = true,
            transactionsMatch = true,
            networkAvailable = true
        )

        assertTrue(report.canResolve)
    }

    @Test
    fun `IntegrityReport canResolve returns false when network unavailable`() {
        val report = IntegrityReport(
            categoriesMatch = false,
            accountGroupsMatch = true,
            accountsMatch = true,
            transactionsMatch = true,
            networkAvailable = false
        )

        assertFalse(report.canResolve)
    }

    @Test
    fun `IntegrityReport canResolve returns false when no discrepancy`() {
        val report = IntegrityReport(
            categoriesMatch = true,
            accountGroupsMatch = true,
            accountsMatch = true,
            transactionsMatch = true,
            networkAvailable = true
        )

        assertFalse(report.canResolve)
    }
}

