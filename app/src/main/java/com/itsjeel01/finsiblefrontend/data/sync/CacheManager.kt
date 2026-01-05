package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountGroupLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.CategoryLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.TransactionLocalRepository
import com.itsjeel01.finsiblefrontend.data.model.Account
import com.itsjeel01.finsiblefrontend.data.model.AccountGroup
import com.itsjeel01.finsiblefrontend.data.model.toEntity
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import com.itsjeel01.finsiblefrontend.data.remote.model.CategoriesData
import com.itsjeel01.finsiblefrontend.data.remote.model.TransactionsData
import com.itsjeel01.finsiblefrontend.data.remote.model.TransactionsDeltaData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheManager @Inject constructor(
    private val categoryLocalRepo: CategoryLocalRepository,
    private val accountLocalRepo: AccountLocalRepository,
    private val accountGroupLocalRepo: AccountGroupLocalRepository,
    private val transactionLocalRepo: TransactionLocalRepository
) {
    fun cacheData(response: BaseResponse<*>) {
        if (!response.success || response.data == null || !response.cache) {
            Logger.Cache.d("Skipping ${response.data?.javaClass?.simpleName} caching: success=${response.success}, hasData=${response.data != null}, cache=${response.cache}")
            return
        }

        when (val data = response.data) {
            is CategoriesData -> {
                Logger.Cache.i("Caching ${data.categories.size} categories of type ${data.type}")
                categoryLocalRepo.addAll(
                    data.categories,
                    additionalInfo = TransactionType.valueOf(data.type),
                    ttlMinutes = response.cacheTtlMinutes
                )
            }

            is TransactionsData -> {
                Logger.Cache.i("Caching ${data.transactions.size} transactions")
                transactionLocalRepo.addAll(
                    data.transactions,
                    additionalInfo = null,
                    ttlMinutes = response.cacheTtlMinutes
                )
            }

            is TransactionsDeltaData -> {
                Logger.Cache.i("Applying ${data.changes.size} transaction delta changes")
                applyTransactionDelta(data)
            }

            is List<*> -> {
                if (data.isEmpty()) {
                    Logger.Cache.d("Empty list received, skipping cache")
                    return
                }

                when (val first = data.first()) {
                    is AccountGroup -> {
                        @Suppress("UNCHECKED_CAST")
                        val groups = data.filterIsInstance<AccountGroup>()
                        if (groups.size == data.size) {
                            Logger.Cache.i("Caching ${groups.size} account groups")
                            accountGroupLocalRepo.addAll(
                                groups,
                                additionalInfo = null,
                                ttlMinutes = response.cacheTtlMinutes
                            )
                        }
                    }

                    is Account -> {
                        @Suppress("UNCHECKED_CAST")
                        val accounts = data.filterIsInstance<Account>()
                        if (accounts.size == data.size) {
                            Logger.Cache.i("Caching ${accounts.size} accounts")
                            accountLocalRepo.addAll(
                                accounts,
                                additionalInfo = null,
                                ttlMinutes = response.cacheTtlMinutes
                            )
                        }
                    }

                    else -> {
                        Logger.Cache.w("Unknown data type in list: ${first?.javaClass?.simpleName}")
                    }
                }
            }

            else -> {
                Logger.Cache.w("Unsupported data type for caching: ${data.javaClass.simpleName}")
            }
        }
    }

    private fun applyTransactionDelta(deltaData: TransactionsDeltaData) {
        for (change in deltaData.changes) {
            if (change.deleted) {
                transactionLocalRepo.removeById(change.id)
                Logger.Cache.d("Delta: removed transaction ${change.id}")
            } else {
                change.transaction?.let { tx ->
                    transactionLocalRepo.upsert(tx.toEntity())
                    Logger.Cache.d("Delta: upserted transaction ${change.id}")
                }
            }
        }
    }
}