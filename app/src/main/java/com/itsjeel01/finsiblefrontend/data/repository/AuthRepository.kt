package com.itsjeel01.finsiblefrontend.data.repository

import com.itsjeel01.finsiblefrontend.data.remote.api.AuthApiService
import com.itsjeel01.finsiblefrontend.data.remote.model.AuthData
import com.itsjeel01.finsiblefrontend.data.remote.model.AuthLoginRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val apiService: AuthApiService) {
    suspend fun authenticate(clientId: String, token: String): BaseResponse<AuthData> {
        return apiService.login(AuthLoginRequest(clientId, token))
    }
}
