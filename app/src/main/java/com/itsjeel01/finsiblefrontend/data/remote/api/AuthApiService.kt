package com.itsjeel01.finsiblefrontend.data.remote.api

import com.itsjeel01.finsiblefrontend.data.remote.model.AuthData
import com.itsjeel01.finsiblefrontend.data.remote.model.AuthLoginRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/googleSignIn")
    suspend fun login(@Body request: AuthLoginRequest): BaseResponse<AuthData>
}
