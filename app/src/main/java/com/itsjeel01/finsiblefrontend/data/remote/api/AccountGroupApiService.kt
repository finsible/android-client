package com.itsjeel01.finsiblefrontend.data.remote.api

import com.itsjeel01.finsiblefrontend.data.model.AccountGroup
import com.itsjeel01.finsiblefrontend.data.remote.model.AccountGroupCreateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.AccountGroupUpdateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AccountGroupApiService {
    @GET("account-groups/all")
    suspend fun getAccountGroups(): BaseResponse<List<AccountGroup>>

    @GET("account-groups/{id}")
    suspend fun getAccountGroup(@Path("id") id: Long): BaseResponse<AccountGroup>

    @POST("account-groups/")
    suspend fun createAccountGroup(
        @Body request: AccountGroupCreateRequest
    ): BaseResponse<AccountGroup>

    @PUT("account-groups/{id}")
    suspend fun updateAccountGroup(
        @Path("id") id: Long,
        @Body request: AccountGroupUpdateRequest
    ): BaseResponse<AccountGroup>

    @DELETE("account-groups/{id}")
    suspend fun deleteAccountGroup(@Path("id") id: Long): BaseResponse<Unit>
}
