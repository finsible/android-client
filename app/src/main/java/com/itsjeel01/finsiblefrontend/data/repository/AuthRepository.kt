package com.itsjeel01.finsiblefrontend.data.repository

import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import com.itsjeel01.finsiblefrontend.data.remote.model.AuthLoginRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.AuthDataResponse
import com.itsjeel01.finsiblefrontend.data.remote.api.AuthApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val authApiService: AuthApiService) {
    suspend fun authenticate(clientId: String, token: String): BaseResponse<AuthDataResponse> {
        return authApiService.login(AuthLoginRequest(clientId, token))
    }
}
