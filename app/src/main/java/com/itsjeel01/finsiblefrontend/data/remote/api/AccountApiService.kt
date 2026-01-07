package com.itsjeel01.finsiblefrontend.data.remote.api

import com.itsjeel01.finsiblefrontend.data.model.Account
import com.itsjeel01.finsiblefrontend.data.remote.model.AccountCreateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.AccountUpdateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AccountApiService {
    @GET("accounts/all")
    suspend fun getAccounts(): BaseResponse<List<Account>>

    @GET("accounts/{id}")
    suspend fun getAccount(@Path("id") id: Long): BaseResponse<Account>

    @POST("accounts/")
    suspend fun createAccount(
        @Body request: AccountCreateRequest
    ): BaseResponse<Account>

    @PUT("accounts/{id}")
    suspend fun updateAccount(
        @Path("id") id: Long,
        @Body request: AccountUpdateRequest
    ): BaseResponse<Account>

    @DELETE("accounts/{id}")
    suspend fun deleteAccount(@Path("id") id: Long): BaseResponse<Unit>
}