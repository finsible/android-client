package com.itsjeel01.finsiblefrontend.data.repository

import android.util.Log
import com.itsjeel01.finsiblefrontend.common.PreferenceManager
import com.itsjeel01.finsiblefrontend.data.remote.api.AuthApiService
import com.itsjeel01.finsiblefrontend.data.remote.model.AuthData
import com.itsjeel01.finsiblefrontend.data.remote.model.AuthLoginRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: AuthApiService,
    private val prefsManager: PreferenceManager
) {

    suspend fun authenticate(clientId: String, idToken: String): Result<AuthData> {
        return try {
            val response = apiService.login(AuthLoginRequest(clientId, idToken))

            if (!response.success) {
                Log.e(TAG, "Authentication rejected by server: ${response.message}")
                Result.failure(Exception(response.message))
            } else {
                Log.d(TAG, "Authentication successful: ${response.data.userId}")
                prefsManager.saveAuthData(response.data)
                Result.success(response.data)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error during authentication", e)
            Result.failure(e)
        }
    }

    fun logout(): Result<Unit> {
        // TODO: Inform backend about logout if necessary
        prefsManager.clearAuthData()
        Log.d(TAG, "User logged out successfully")
        return Result.success(Unit)
    }

    fun isAuthenticated(): Boolean = prefsManager.isLoggedIn()

    companion object {
        private const val TAG = "AuthRepository"
    }
}