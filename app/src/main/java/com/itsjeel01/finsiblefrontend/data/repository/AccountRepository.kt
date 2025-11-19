package com.itsjeel01.finsiblefrontend.data.repository

import com.itsjeel01.finsiblefrontend.data.model.Account
import com.itsjeel01.finsiblefrontend.data.remote.api.AccountApiService
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(private val apiService: AccountApiService) {
    suspend fun getAccounts(): BaseResponse<List<Account>> {
        return apiService.getAccounts()
    }
}