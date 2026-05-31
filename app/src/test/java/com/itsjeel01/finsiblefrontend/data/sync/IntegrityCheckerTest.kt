package com.itsjeel01.finsiblefrontend.data.sync

import com.google.common.truth.Truth.assertThat
import com.itsjeel01.finsiblefrontend.MainDispatcherRule
import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.OperationType
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class IntegrityCheckerTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val categoryLocalRepo: CategoryLocalRepository = mockk(relaxed = true)
    private val accountGroupLocalRepo: AccountGroupLocalRepository = mockk(relaxed = true)
    private val accountLocalRepo: AccountLocalRepository = mockk(relaxed = true)
    private val transactionLocalRepo: TransactionLocalRepository = mockk(relaxed = true)
    private val pendingOperationRepo: PendingOperationRepository = mockk(relaxed = true)
    private val syncApi: SyncApiService = mockk()

    private lateinit var checker: IntegrityChecker

    private val emptySnapshot = EntitySnapshot(
        categories = 0,
        accountGroups = 0,
        accounts = 0,
        transactions = 0
    )

    @Before
    fun setUp() {
        coEvery { syncApi.getSnapshot() } returns emptySnapshot
        every { pendingOperationRepo.getPending() } returns emptyList()

        checker = IntegrityChecker(
            categoryLocalRepo = categoryLocalRepo,
            accountGroupLocalRepo = accountGroupLocalRepo,
            accountLocalRepo = accountLocalRepo,
            transactionLocalRepo = transactionLocalRepo,
            pendingOperationRepo = pendingOperationRepo,
            syncApi = syncApi
        )
    }

    @Test
    fun `verifyAllIntegrity returns match when counts align`() = runTest {
        every { categoryLocalRepo.getAll().size } returns 5
        every { accountGroupLocalRepo.getAll().size } returns 3
        every { accountLocalRepo.getAll().size } returns 8
        every { transactionLocalRepo.getTotalTransactionCount() } returns 20L

        coEvery { syncApi.getSnapshot() } returns EntitySnapshot(
            categories = 5, accountGroups = 3, accounts = 8, transactions = 20
        )

        val report = checker.verifyAllIntegrity()

        assertThat(report.hasDiscrepancy).isFalse()
        assertThat(report.networkAvailable).isTrue()
    }

    @Test
    fun `verifyAllIntegrity detects category count mismatch`() = runTest {
        every { categoryLocalRepo.getAll().size } returns 10
        coEvery { syncApi.getSnapshot() } returns EntitySnapshot(
            categories = 7, accountGroups = 3, accounts = 8, transactions = 20
        )

        val report = checker.verifyAllIntegrity()

        assertThat(report.hasDiscrepancy).isTrue()
        assertThat(report.categoriesMatch).isFalse()
    }

    @Test
    fun `verifyAllIntegrity returns network unavailable on exception`() = runTest {
        coEvery { syncApi.getSnapshot() } throws RuntimeException("Network error")

        val report = checker.verifyAllIntegrity()

        assertThat(report.networkAvailable).isFalse()
        assertThat(report.hasDiscrepancy).isFalse()
    }

    @Test
    fun `calculateExpectedServerCount accounts for pending creates`() = runTest {
        every { categoryLocalRepo.getAll().size } returns 7
        every { pendingOperationRepo.getPending() } returns listOf(
            PendingOperationEntity(
                localId = 1, entityType = EntityType.CATEGORY,
                operationType = OperationType.CREATE,
                entityId = -1, payload = "{}", createdAt = 1L
            )
        )
        coEvery { syncApi.getSnapshot() } returns EntitySnapshot(
            categories = 6, accountGroups = 0, accounts = 0, transactions = 0
        )

        val report = checker.verifyAllIntegrity()

        // 7 local - 1 pending create = 6 expected on server = server has 6
        assertThat(report.categoriesMatch).isTrue()
    }

    @Test
    fun `calculateExpectedServerCount accounts for pending deletes`() = runTest {
        every { categoryLocalRepo.getAll().size } returns 7
        every { pendingOperationRepo.getPending() } returns listOf(
            PendingOperationEntity(
                localId = 1, entityType = EntityType.CATEGORY,
                operationType = OperationType.DELETE,
                entityId = 5, payload = "{}", createdAt = 1L
            )
        )
        coEvery { syncApi.getSnapshot() } returns EntitySnapshot(
            categories = 8, accountGroups = 0, accounts = 0, transactions = 0
        )

        val report = checker.verifyAllIntegrity()

        // 7 local + 1 pending delete = 8 expected on server = server has 8
        assertThat(report.categoriesMatch).isTrue()
    }

    @Test
    fun `individual verifyCategoriesIntegrity succeeds`() = runTest {
        every { categoryLocalRepo.getAll().size } returns 5
        coEvery { syncApi.getSnapshot() } returns emptySnapshot.copy(categories = 5)

        val result = checker.verifyCategoriesIntegrity()

        assertThat(result).isTrue()
    }

    @Test
    fun `individual verifyCategoriesIntegrity fails on mismatch`() = runTest {
        every { categoryLocalRepo.getAll().size } returns 5
        coEvery { syncApi.getSnapshot() } returns emptySnapshot.copy(categories = 3)

        val result = checker.verifyCategoriesIntegrity()

        assertThat(result).isFalse()
    }
}
