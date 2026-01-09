package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.OperationType
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountGroupLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.CategoryLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.PendingOperationRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.TransactionLocalRepository
import com.itsjeel01.finsiblefrontend.data.remote.api.SyncApiService
import com.itsjeel01.finsiblefrontend.data.remote.model.EntitySnapshot
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntegrityChecker @Inject constructor(
    private val categoryLocalRepo: CategoryLocalRepository,
    private val accountGroupLocalRepo: AccountGroupLocalRepository,
    private val accountLocalRepo: AccountLocalRepository,
    private val transactionLocalRepo: TransactionLocalRepository,
    private val pendingOperationRepo: PendingOperationRepository,
    private val syncApi: SyncApiService
) {

    private fun calculateExpectedServerCount(localCount: Int, entityType: EntityType): Int {
        val pendingOps = pendingOperationRepo.getPending().filter { it.entityType == entityType }

        val pendingCreates = pendingOps.count { it.operationType == OperationType.CREATE }
        val pendingDeletes = pendingOps.count { it.operationType == OperationType.DELETE }

        val expectedServerCount = localCount - pendingCreates + pendingDeletes

        if (pendingCreates > 0 || pendingDeletes > 0) {
            Logger.Sync.d(
                "$entityType pending ops: creates=$pendingCreates, deletes=$pendingDeletes, " +
                        "adjusted expected=$expectedServerCount (local=$localCount)"
            )
        }

        return expectedServerCount.coerceAtLeast(0)
    }

    private suspend fun verifyCount(
        entityType: EntityType,
        localCountProvider: suspend () -> Int,
        serverCountProvider: (EntitySnapshot) -> Int,
        label: String
    ): Boolean {
        return try {
            val snapshot = syncApi.getSnapshot()
            val localCount = localCountProvider()
            val expectedServerCount = calculateExpectedServerCount(localCount, entityType)
            val serverCount = serverCountProvider(snapshot)
            val match = expectedServerCount == serverCount

            if (!match) {
                Logger.Sync.w(
                    "$label count mismatch: expected=$expectedServerCount, server=$serverCount (local=$localCount)"
                )
            } else {
                Logger.Sync.d("$label integrity verified: $expectedServerCount")
            }

            match
        } catch (e: Exception) {
            Logger.Sync.e("Failed to verify $label integrity: ${e.message}")
            true
        }
    }

    suspend fun verifyCategoriesIntegrity(): Boolean =
        verifyCount(
            entityType = EntityType.CATEGORY,
            localCountProvider = { categoryLocalRepo.getAll().size },
            serverCountProvider = { it.categories },
            label = "Category"
        )

    suspend fun verifyAccountGroupsIntegrity(): Boolean =
        verifyCount(
            entityType = EntityType.ACCOUNT_GROUP,
            localCountProvider = { accountGroupLocalRepo.getAll().size },
            serverCountProvider = { it.accountGroups },
            label = "AccountGroup"
        )

    suspend fun verifyAccountsIntegrity(): Boolean =
        verifyCount(
            entityType = EntityType.ACCOUNT,
            localCountProvider = { accountLocalRepo.getAll().size },
            serverCountProvider = { it.accounts },
            label = "Account"
        )

    suspend fun verifyTransactionsIntegrity(): Boolean =
        verifyCount(
            entityType = EntityType.TRANSACTION,
            localCountProvider = { transactionLocalRepo.getTotalTransactionCount().toInt() },
            serverCountProvider = { it.transactions },
            label = "Transaction"
        )

    suspend fun verifyAllIntegrity(): IntegrityReport {
        return try {
            val snapshot = syncApi.getSnapshot()

            val categoryLocalCount = categoryLocalRepo.getAll().size
            val accountGroupLocalCount = accountGroupLocalRepo.getAll().size
            val accountLocalCount = accountLocalRepo.getAll().size
            val transactionLocalCount = transactionLocalRepo.getTotalTransactionCount().toInt()

            val categoryExpected = calculateExpectedServerCount(categoryLocalCount, EntityType.CATEGORY)
            val accountGroupExpected = calculateExpectedServerCount(accountGroupLocalCount, EntityType.ACCOUNT_GROUP)
            val accountExpected = calculateExpectedServerCount(accountLocalCount, EntityType.ACCOUNT)
            val transactionExpected = calculateExpectedServerCount(transactionLocalCount, EntityType.TRANSACTION)

            val categoriesMatch = categoryExpected == snapshot.categories
            val accountGroupsMatch = accountGroupExpected == snapshot.accountGroups
            val accountsMatch = accountExpected == snapshot.accounts
            val transactionsMatch = transactionExpected == snapshot.transactions

            IntegrityReport(
                categoriesMatch = categoriesMatch,
                accountGroupsMatch = accountGroupsMatch,
                accountsMatch = accountsMatch,
                transactionsMatch = transactionsMatch,
                serverSnapshot = snapshot,
                networkAvailable = true
            )
        } catch (e: Exception) {
            Logger.Sync.w("Network unavailable for integrity check: ${e.message}")
            // Return report indicating network unavailable - not a validation error
            IntegrityReport(
                categoriesMatch = true,
                accountGroupsMatch = true,
                accountsMatch = true,
                transactionsMatch = true,
                serverSnapshot = null,
                networkAvailable = false
            )
        }
    }
}

/** Report of integrity verification results. */
data class IntegrityReport(
    val categoriesMatch: Boolean = true,
    val accountGroupsMatch: Boolean = true,
    val accountsMatch: Boolean = true,
    val transactionsMatch: Boolean = true,
    val serverSnapshot: EntitySnapshot? = null,
    val networkAvailable: Boolean = true
) {
    val hasDiscrepancy: Boolean
        get() = networkAvailable && (!categoriesMatch || !accountGroupsMatch || !accountsMatch || !transactionsMatch)

    val canResolve: Boolean
        get() = networkAvailable && hasDiscrepancy
}