package com.itsjeel01.finsiblefrontend.core.network.service

import com.itsjeel01.finsiblefrontend.core.network.model.base.BaseResponse
import com.itsjeel01.finsiblefrontend.core.network.model.request.AuthLoginRequest
import com.itsjeel01.finsiblefrontend.core.network.model.response.AuthDataResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    // Authenticate user using Google Sign-In
    @POST("auth/googleSignIn")
    suspend fun login(@Body request: AuthLoginRequest): BaseResponse<AuthDataResponse>
}