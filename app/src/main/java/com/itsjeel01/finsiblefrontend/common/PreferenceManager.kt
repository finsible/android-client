package com.itsjeel01.finsiblefrontend.common

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.itsjeel01.finsiblefrontend.data.remote.model.AuthData
import javax.inject.Inject

class PreferenceManager @Inject constructor(context: Context) {
    private var masterKey: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private var sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        Strings.PREFS_FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    /** Saves authentication data to encrypted shared preferences. */
    fun saveAuthData(authResponse: AuthData) {
        sharedPreferences.edit {
            putString(Strings.JWT, authResponse.jwt)
            putBoolean(Strings.IS_LOGGED_IN, true)
            putString(Strings.USER_ID, authResponse.userId)
            putString(Strings.EMAIL, authResponse.email)
            putString(Strings.NAME, authResponse.name)
        }
    }

    /** Clears authentication data from encrypted shared preferences. */
    fun clearAuthData() {
        sharedPreferences.edit {
            remove(Strings.JWT)
            putBoolean(Strings.IS_LOGGED_IN, false)
            remove(Strings.USER_ID)
            remove(Strings.EMAIL)
            remove(Strings.NAME)
        }
    }

    fun isLoggedIn(): Boolean = sharedPreferences.getBoolean(Strings.JWT, false)
    fun getJwt(): String? = sharedPreferences.getString(Strings.IS_LOGGED_IN, null)
    fun getUserId(): String? = sharedPreferences.getString(Strings.USER_ID, null)
    fun getEmail(): String? = sharedPreferences.getString(Strings.EMAIL, null)
    fun getName(): String? = sharedPreferences.getString(Strings.NAME, null)
}
