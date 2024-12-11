package com.itsjeel01.finsiblefrontend.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.itsjeel01.finsiblefrontend.data.network.responses.AuthResponse
import javax.inject.Inject

class PreferenceManager @Inject constructor(context: Context) {
    private var masterKey: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private var sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secret_shared_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveAuthData(authResponse: AuthResponse) {
        sharedPreferences.edit().apply {
            putString("jwt", authResponse.jwt)
            putBoolean("isLoggedIn", true)
            putString("userId", authResponse.userId)
            putString("email", authResponse.email)
            putString("name", authResponse.name)
        }.apply()
    }

    fun clearAuthData() {
        sharedPreferences.edit().apply {
            remove("jwt")
            putBoolean("isLoggedIn", false)
        }.apply()
    }

    fun isLoggedIn(): Boolean = sharedPreferences.getBoolean("isLoggedIn", false)
    fun getJwt(): String? = sharedPreferences.getString("jwt", null)
    fun getUserId(): String? = sharedPreferences.getString("userId", null)
    fun getEmail(): String? = sharedPreferences.getString("email", null)
    fun getName(): String? = sharedPreferences.getString("name", null)
}