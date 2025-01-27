package com.itsjeel01.finsiblefrontend.data.network.apis

import com.itsjeel01.finsiblefrontend.data.network.requests.AuthRequest
import com.itsjeel01.finsiblefrontend.data.network.responses.AuthData
import com.itsjeel01.finsiblefrontend.data.network.responses.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    // Authenticate user using Google Sign-In
    @POST("auth/googleSignIn")
    suspend fun login(@Body request: AuthRequest): BaseResponse<AuthData>
}