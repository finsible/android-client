package com.itsjeel01.finsiblefrontend.data.sync

import android.util.Log
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import com.itsjeel01.finsiblefrontend.data.repository.AccountGroupRepository
import com.itsjeel01.finsiblefrontend.data.repository.AccountRepository
import com.itsjeel01.finsiblefrontend.data.repository.CategoryRepository
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
    private val accountRepository: AccountRepository
) {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun initialize() {
        Log.d(TAG, "Starting post-authentication initialization")

        fetchAndCacheCategories(TransactionType.INCOME.name, "Income categories")
        fetchAndCacheCategories(TransactionType.EXPENSE.name, "Expense categories")
        fetchAndCacheCategories(TransactionType.TRANSFER.name, "Transfer categories")
        fetchAndCache("Account groups") { accountGroupRepository.getAccountGroups() }
        fetchAndCache("Accounts") { accountRepository.getAccounts() }
    }

    private fun fetchAndCacheCategories(type: String, resourceName: String) {
        fetchAndCache(resourceName) { categoryRepository.getCategories(type) }
    }

    private fun <T> fetchAndCache(resourceName: String, fetcher: suspend () -> BaseResponse<T>) {
        applicationScope.launch {
            runCatching {
                val result = fetcher()
                if (result.success) {
                    Log.d(TAG, "$resourceName cached successfully")
                } else {
                    Log.w(TAG, "$resourceName fetch unsuccessful: ${result.message}")
                }
            }.onFailure { e ->
                Log.e(TAG, "Error fetching ${resourceName.lowercase()}", e)
            }
        }
    }

    companion object {
        private const val TAG = "PostAuthInitializer"
    }
}