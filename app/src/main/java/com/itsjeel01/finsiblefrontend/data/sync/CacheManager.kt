package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountGroupLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.CategoryLocalRepository
import com.itsjeel01.finsiblefrontend.data.model.Account
import com.itsjeel01.finsiblefrontend.data.model.AccountGroup
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import com.itsjeel01.finsiblefrontend.data.remote.model.CategoriesData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheManager @Inject constructor(
    private val categoryLocalRepo: CategoryLocalRepository,
    private val accountLocalRepo: AccountLocalRepository,
    private val accountGroupLocalRepo: AccountGroupLocalRepository
) {
    fun cacheData(response: BaseResponse<*>) {
        if (!response.success || response.data == null || !response.cache) return

        when (val data = response.data) {
            is CategoriesData -> {
                categoryLocalRepo.addAll(
                    data.categories,
                    additionalInfo = TransactionType.valueOf(data.type),
                    ttlMinutes = response.cacheTtlMinutes
                )
            }

            is List<*> -> {
                if (data.isEmpty()) return
                val first = data.first()
                when (first) {
                    is AccountGroup -> {
                        @Suppress("UNCHECKED_CAST")
                        val groups = data.filterIsInstance<AccountGroup>()
                        if (groups.size == data.size) {
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
                            accountLocalRepo.addAll(
                                accounts,
                                additionalInfo = null,
                                ttlMinutes = response.cacheTtlMinutes
                            )
                        }
                    }
                }
            }
        }
    }
}