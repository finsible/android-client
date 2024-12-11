package com.itsjeel01.finsiblefrontend.data.repositories

import com.itsjeel01.finsiblefrontend.data.network.ApiService
import com.itsjeel01.finsiblefrontend.data.network.requests.AuthRequest
import com.itsjeel01.finsiblefrontend.data.network.responses.AuthResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun authenticate(clientId: String, token: String): AuthResponse {
        return apiService.login(AuthRequest(clientId, token))
    }
}