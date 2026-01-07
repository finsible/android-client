package com.itsjeel01.finsiblefrontend.data.repository

import com.itsjeel01.finsiblefrontend.data.model.Account
import com.itsjeel01.finsiblefrontend.data.remote.api.AccountApiService
import com.itsjeel01.finsiblefrontend.data.remote.model.AccountCreateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.AccountUpdateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(private val apiService: AccountApiService) {
    suspend fun getAccounts(): BaseResponse<List<Account>> {
        return apiService.getAccounts()
    }

    suspend fun getAccount(id: Long): BaseResponse<Account> {
        return apiService.getAccount(id)
    }

    suspend fun createAccount(request: AccountCreateRequest): BaseResponse<Account> {
        return apiService.createAccount(request)
    }

    suspend fun updateAccount(id: Long, request: AccountUpdateRequest): BaseResponse<Account> {
        return apiService.updateAccount(id, request)
    }

    suspend fun deleteAccount(id: Long): BaseResponse<Unit> {
        return apiService.deleteAccount(id)
    }
}