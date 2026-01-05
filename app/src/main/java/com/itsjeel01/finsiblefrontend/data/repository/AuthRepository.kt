package com.itsjeel01.finsiblefrontend.data.repository

import com.itsjeel01.finsiblefrontend.common.PreferenceManager
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.remote.api.AuthApiService
import com.itsjeel01.finsiblefrontend.data.remote.model.AuthData
import com.itsjeel01.finsiblefrontend.data.remote.model.AuthLoginRequest
import com.itsjeel01.finsiblefrontend.data.sync.ScopeManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: AuthApiService,
    private val prefsManager: PreferenceManager,
    private val scopeManager: ScopeManager
) {

    suspend fun authenticate(clientId: String, idToken: String): Result<AuthData> {
        return try {
            val response = apiService.login(AuthLoginRequest(clientId, idToken))

            if (!response.success) {
                Logger.Auth.e("Authentication rejected by server: ${response.message}")
                Result.failure(Exception(response.message))
            } else {
                Logger.Auth.i("Authentication successful: userId=${response.data.userId}")
                prefsManager.saveAuthData(response.data)
                Result.success(response.data)
            }
        } catch (e: Exception) {
            Logger.Auth.e("Error during authentication", e)
            Result.failure(e)
        }
    }

    fun logout(): Result<Unit> {
        // Cancel all ongoing sync operations before logout
        scopeManager.reset()
        // TODO: Inform backend about logout if necessary
        prefsManager.clearAuthData()
        Logger.Auth.i("User logged out successfully")
        return Result.success(Unit)
    }

    fun isAuthenticated(): Boolean = prefsManager.isLoggedIn()
}