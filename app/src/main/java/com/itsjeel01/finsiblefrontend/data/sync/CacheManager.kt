package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.repository.CategoryLocalRepository
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import com.itsjeel01.finsiblefrontend.data.remote.model.CategoriesData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheManager @Inject constructor(
    private val categoryLocalRepository: CategoryLocalRepository
) {
    fun cacheData(response: BaseResponse<*>) {
        if (!response.success || response.data == null || !response.cache) return

        when (val data = response.data) {
            is CategoriesData -> {
                categoryLocalRepository.addAll(
                    data.categories,
                    additionalInfo = TransactionType.valueOf(data.type),
                    ttlMinutes = response.cacheTtlMinutes
                )
            }
        }
    }
}