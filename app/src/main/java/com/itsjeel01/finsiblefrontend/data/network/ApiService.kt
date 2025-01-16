package com.itsjeel01.finsiblefrontend.data.network

import com.itsjeel01.finsiblefrontend.data.network.requests.AuthRequest
import com.itsjeel01.finsiblefrontend.data.network.responses.AuthAttributes
import com.itsjeel01.finsiblefrontend.data.network.responses.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/googleSignIn")
    suspend fun login(@Body request: AuthRequest): BaseResponse<AuthAttributes>
}