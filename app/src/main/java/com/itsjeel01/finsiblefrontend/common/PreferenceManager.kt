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
        private const val KEY_JWT = "jwt"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "email"
        private const val KEY_USER_NAME = "name"
        private const val KEY_LOCAL_ID_COUNTER = "local_id_counter"
        private const val KEY_SYNC_ENABLED = "sync_enabled"
        private const val KEY_BACKUP_ENABLED = "backup_enabled"
        private const val KEY_WIFI_ONLY_SYNC = "wifi_only_sync"
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
            putString(KEY_JWT, authResponse.jwt)
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_ID, authResponse.userId)
            putString(KEY_USER_EMAIL, authResponse.email)
            putString(KEY_USER_NAME, authResponse.name)
        }
    }

    fun clearAuthData() {
        sharedPreferences.edit {
            remove(KEY_JWT)
            putBoolean(KEY_IS_LOGGED_IN, false)
            remove(KEY_USER_ID)
            remove(KEY_USER_EMAIL)
            remove(KEY_USER_NAME)
        }
    }

    fun getLocalIdCounter(): Long {
        return sharedPreferences.getLong(KEY_LOCAL_ID_COUNTER, 0L)
    }

    fun saveLocalIdCounter(counter: Long) {
        sharedPreferences.edit { putLong(KEY_LOCAL_ID_COUNTER, counter) }
    }

    fun isSyncEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_SYNC_ENABLED, false)
    }

    fun setSyncEnabled(enabled: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_SYNC_ENABLED, enabled) }
    }

    fun isBackupEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_BACKUP_ENABLED, false)
    }

    fun setBackupEnabled(enabled: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_BACKUP_ENABLED, enabled) }
    }

    fun isWifiOnlySyncEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_WIFI_ONLY_SYNC, true)
    }

    fun setWifiOnlySyncEnabled(enabled: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_WIFI_ONLY_SYNC, enabled) }
    }

    fun isLoggedIn(): Boolean = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    fun getJwt(): String? = sharedPreferences.getString(KEY_JWT, null)
    fun getUserId(): String? = sharedPreferences.getString(KEY_USER_ID, null)
    fun getEmail(): String? = sharedPreferences.getString(KEY_USER_EMAIL, null)
    fun getName(): String? = sharedPreferences.getString(KEY_USER_NAME, null)
}
