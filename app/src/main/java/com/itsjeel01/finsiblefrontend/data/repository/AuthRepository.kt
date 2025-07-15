package com.itsjeel01.finsiblefrontend.data.repository

import com.itsjeel01.finsiblefrontend.core.network.model.base.BaseResponse
import com.itsjeel01.finsiblefrontend.core.network.model.request.AuthLoginRequest
import com.itsjeel01.finsiblefrontend.core.network.model.response.AuthDataResponse
import com.itsjeel01.finsiblefrontend.core.network.service.AuthApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val authApiService: AuthApiService) {
    suspend fun authenticate(clientId: String, token: String): BaseResponse<AuthDataResponse> {
        return authApiService.login(AuthLoginRequest(clientId, token))
    }
}