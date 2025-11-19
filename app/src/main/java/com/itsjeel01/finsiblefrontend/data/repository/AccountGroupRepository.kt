package com.itsjeel01.finsiblefrontend.data.repository

import com.itsjeel01.finsiblefrontend.data.model.AccountGroup
import com.itsjeel01.finsiblefrontend.data.remote.api.AccountGroupApiService
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountGroupRepository @Inject constructor(private val apiService: AccountGroupApiService) {
    suspend fun getAccountGroups(): BaseResponse<List<AccountGroup>> {
        return apiService.getAccountGroups()
    }
}