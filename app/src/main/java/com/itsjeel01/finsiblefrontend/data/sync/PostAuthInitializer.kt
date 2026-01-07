package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.di.ObjectBoxModule
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountGroupEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.CategoryEntity
import com.itsjeel01.finsiblefrontend.data.repository.AccountGroupRepository
import com.itsjeel01.finsiblefrontend.data.repository.AccountRepository
import com.itsjeel01.finsiblefrontend.data.repository.CategoryRepository
import io.objectbox.BoxStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostAuthInitializer @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val accountGroupRepository: AccountGroupRepository,
    private val accountRepository: AccountRepository,
    private val dataFetcher: DataFetcher,
    private val boxStore: BoxStore,
    private val integrityChecker: IntegrityChecker
) {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun initialize(forceRefresh: Boolean = false) {
        val wasDatabaseCleared = ObjectBoxModule.wasDatabaseCleared()
        val isDatabaseEmpty = isDatabaseEmpty()

        if (wasDatabaseCleared) {
            Logger.Sync.w("Database was cleared due to schema conflict - forcing data re-sync")
            ObjectBoxModule.resetClearedFlag()
        }

        if (isDatabaseEmpty) {
            Logger.Sync.i("Database is empty - fetching all data from server")
        }

        val shouldForceRefresh = forceRefresh || wasDatabaseCleared || isDatabaseEmpty

        if (shouldForceRefresh) {
            Logger.Sync.i("Starting forced post-authentication data synchronization")
        } else {
            Logger.Sync.i("Starting post-authentication initialization")
        }

        // Fetch categories using DataFetcher
        fetchCategories(TransactionType.INCOME, shouldForceRefresh)
        fetchCategories(TransactionType.EXPENSE, shouldForceRefresh)
        fetchCategories(TransactionType.TRANSFER, shouldForceRefresh)

        // Fetch account groups using DataFetcher
        fetchAccountGroups(shouldForceRefresh)

        // Fetch accounts using DataFetcher
        fetchAccounts(shouldForceRefresh)

        Logger.Sync.i("Post-authentication initialization tasks dispatched")
    }

    /** Checks if the database is empty by checking if core entity boxes are empty. */
    private fun isDatabaseEmpty(): Boolean {
        return try {
            val categoryCount = boxStore.boxFor(CategoryEntity::class.java).count()
            val accountGroupCount = boxStore.boxFor(AccountGroupEntity::class.java).count()
            val accountCount = boxStore.boxFor(AccountEntity::class.java).count()

            val totalCount = categoryCount + accountGroupCount + accountCount
            Logger.Sync.d("Database entity counts - Categories: $categoryCount, AccountGroups: $accountGroupCount, Accounts: $accountCount")

            totalCount == 0L
        } catch (e: Exception) {
            Logger.Sync.w("Error checking database emptiness", e)
            false
        }
    }

    private fun fetchCategories(type: TransactionType, forceRefresh: Boolean) {
        applicationScope.launch {
            val action = if (forceRefresh) "Force refreshing" else "Ensuring"
            Logger.Sync.d("$action ${type.name} categories")

            val success = if (forceRefresh) {
                dataFetcher.refreshData(
                    fetcher = { categoryRepository.getCategories(type.name) }
                )
            } else {
                dataFetcher.ensureDataFetched(
                    fetcher = { categoryRepository.getCategories(type.name) },
                    verifyIntegrity = { integrityChecker.verifyCategoriesIntegrity() }
                )
            }

            if (success) {
                Logger.Sync.i("${type.name} categories synced successfully")
            } else {
                Logger.Sync.w("${type.name} categories sync unsuccessful")
            }
        }
    }

    private fun fetchAccountGroups(forceRefresh: Boolean) {
        applicationScope.launch {
            val action = if (forceRefresh) "Force refreshing" else "Ensuring"
            Logger.Sync.d("$action account groups")

            val success = if (forceRefresh) {
                dataFetcher.refreshData(
                    fetcher = { accountGroupRepository.getAccountGroups() }
                )
            } else {
                dataFetcher.ensureDataFetched(
                    fetcher = { accountGroupRepository.getAccountGroups() },
                    verifyIntegrity = { integrityChecker.verifyAccountGroupsIntegrity() }
                )
            }

            if (success) {
                Logger.Sync.i("Account groups synced successfully")
            } else {
                Logger.Sync.w("Account groups sync unsuccessful")
            }
        }
    }

    private fun fetchAccounts(forceRefresh: Boolean) {
        applicationScope.launch {
            val action = if (forceRefresh) "Force refreshing" else "Ensuring"
            Logger.Sync.d("$action accounts")

            val success = if (forceRefresh) {
                dataFetcher.refreshData(
                    fetcher = { accountRepository.getAccounts() }
                )
            } else {
                dataFetcher.ensureDataFetched(
                    fetcher = { accountRepository.getAccounts() },
                    verifyIntegrity = { integrityChecker.verifyAccountsIntegrity() }
                )
            }

            if (success) {
                Logger.Sync.i("Accounts synced successfully")
            } else {
                Logger.Sync.w("Accounts sync unsuccessful")
            }
        }
    }
}