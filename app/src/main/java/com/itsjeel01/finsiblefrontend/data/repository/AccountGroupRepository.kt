package com.itsjeel01.finsiblefrontend.data.repository

import com.itsjeel01.finsiblefrontend.data.model.AccountGroup
import com.itsjeel01.finsiblefrontend.data.remote.api.AccountGroupApiService
import com.itsjeel01.finsiblefrontend.data.remote.model.AccountGroupCreateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.AccountGroupUpdateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountGroupRepository @Inject constructor(private val apiService: AccountGroupApiService) {
    suspend fun getAccountGroups(): BaseResponse<List<AccountGroup>> {
        return apiService.getAccountGroups()
    }

    suspend fun getAccountGroup(id: Long): BaseResponse<AccountGroup> {
        return apiService.getAccountGroup(id)
    }

    suspend fun createAccountGroup(request: AccountGroupCreateRequest): BaseResponse<AccountGroup> {
        return apiService.createAccountGroup(request)
    }

    suspend fun updateAccountGroup(id: Long, request: AccountGroupUpdateRequest): BaseResponse<AccountGroup> {
        return apiService.updateAccountGroup(id, request)
    }

    suspend fun deleteAccountGroup(id: Long): BaseResponse<Unit> {
        return apiService.deleteAccountGroup(id)
    }
}