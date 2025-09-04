package com.itsjeel01.finsiblefrontend.common

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.itsjeel01.finsiblefrontend.data.remote.model.AuthData
import javax.inject.Inject

class PreferenceManager @Inject constructor(context: Context) {
    companion object {
        private const val JWT = "jwt"
        private const val IS_LOGGED_IN = "is_logged_in"
        private const val USER_ID = "user_id"
        private const val EMAIL = "email"
        private const val NAME = "name"
        private const val PREFS_FILE_NAME = "secret_shared_prefs"
    }

    private var masterKey: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private var sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFS_FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    /** Saves authentication data to encrypted shared preferences. */
    fun saveAuthData(authResponse: AuthData) {
        sharedPreferences.edit {
            putString(JWT, authResponse.jwt)
            putBoolean(IS_LOGGED_IN, true)
            putString(USER_ID, authResponse.userId)
            putString(EMAIL, authResponse.email)
            putString(NAME, authResponse.name)
        }
    }

    /** Clears authentication data from encrypted shared preferences. */
    fun clearAuthData() {
        sharedPreferences.edit {
            remove(JWT)
            putBoolean(IS_LOGGED_IN, false)
            remove(USER_ID)
            remove(EMAIL)
            remove(NAME)
        }
    }

    fun isLoggedIn(): Boolean = sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    fun getJwt(): String? = sharedPreferences.getString(JWT, null)
    fun getUserId(): String? = sharedPreferences.getString(USER_ID, null)
    fun getEmail(): String? = sharedPreferences.getString(EMAIL, null)
    fun getName(): String? = sharedPreferences.getString(NAME, null)
}
