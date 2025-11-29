package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.di.ObjectBoxModule
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountGroupEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.CategoryEntity
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
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
    private val boxStore: BoxStore
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

        fetchAndCacheCategories(TransactionType.INCOME.name, "Income categories", shouldForceRefresh)
        fetchAndCacheCategories(TransactionType.EXPENSE.name, "Expense categories", shouldForceRefresh)
        fetchAndCacheCategories(TransactionType.TRANSFER.name, "Transfer categories", shouldForceRefresh)
        fetchAndCache("Account groups", shouldForceRefresh) { accountGroupRepository.getAccountGroups() }
        fetchAndCache("Accounts", shouldForceRefresh) { accountRepository.getAccounts() }

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

    private fun fetchAndCacheCategories(type: String, resourceName: String, forceRefresh: Boolean = false) {
        fetchAndCache(resourceName, forceRefresh) { categoryRepository.getCategories(type) }
    }

    private fun <T> fetchAndCache(resourceName: String, forceRefresh: Boolean = false, fetcher: suspend () -> BaseResponse<T>) {
        applicationScope.launch {
            val logPrefix = if (forceRefresh) "Force fetching" else "Fetching"
            Logger.Sync.d("$logPrefix $resourceName")
            runCatching {
                val result = fetcher()
                if (result.success) {
                    Logger.Sync.i("$resourceName cached successfully")
                } else {
                    Logger.Sync.w("$resourceName fetch unsuccessful: ${result.message}")
                }
            }.onFailure { e ->
                Logger.Sync.e("Error fetching ${resourceName.lowercase()}", e)
            }
        }
    }
}