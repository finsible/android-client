package com.itsjeel01.finsiblefrontend.data.remote.api

import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import com.itsjeel01.finsiblefrontend.data.remote.model.AuthLoginRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.AuthDataResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    // Authenticate user using Google Sign-In
    @POST("auth/googleSignIn")
    suspend fun login(@Body request: AuthLoginRequest): BaseResponse<AuthDataResponse>
}
