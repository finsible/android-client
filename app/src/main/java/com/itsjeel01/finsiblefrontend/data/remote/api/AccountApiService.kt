package com.itsjeel01.finsiblefrontend.data.remote.api

import com.itsjeel01.finsiblefrontend.data.model.Account
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import retrofit2.http.GET

interface AccountApiService {
    @GET("accounts/all")
    suspend fun getAccounts(): BaseResponse<List<Account>>
}