package com.itsjeel01.finsiblefrontend.data.remote.api

import com.itsjeel01.finsiblefrontend.data.model.AccountGroup
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import retrofit2.http.GET

interface AccountGroupApiService {
    @GET("account-groups/all")
    suspend fun getAccountGroups(): BaseResponse<List<AccountGroup>>
}
