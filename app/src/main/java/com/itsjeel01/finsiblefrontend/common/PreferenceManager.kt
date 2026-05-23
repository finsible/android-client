package com.itsjeel01.finsiblefrontend.common

import androidx.datastore.core.DataStore
import com.itsjeel01.finsiblefrontend.common.datastore.UserPreferences
import com.itsjeel01.finsiblefrontend.data.model.Currency
import com.itsjeel01.finsiblefrontend.data.remote.model.AuthData
import com.itsjeel01.finsiblefrontend.data.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceManager @Inject constructor(
    private val dataStore: DataStore<UserPreferences>,
    private val currencyRepository: CurrencyRepository
) {
    val defaultCurrencyCodeFlow: Flow<String> = dataStore.data.map { prefs ->
        prefs.preferredCurrencyCode ?: resolveDefaultCurrencyCode()
    }

    suspend fun saveAuthData(authResponse: AuthData) {
        dataStore.updateData { current ->
            current.copy(
                jwt = authResponse.jwt,
                isLoggedIn = true,
                userId = authResponse.userId,
                email = authResponse.email,
                name = authResponse.name
            )
        }
    }

    suspend fun clearAuthData() {
        dataStore.updateData { current ->
            current.copy(
                jwt = null,
                isLoggedIn = false,
                userId = null,
                email = null,
                name = null
            )
        }
    }

    suspend fun saveLocalIdCounter(counter: Long) {
        dataStore.updateData { it.copy(localIdCounter = counter) }
    }

    suspend fun setSyncEnabled(enabled: Boolean) {
        dataStore.updateData { it.copy(isSyncEnabled = enabled) }
    }

    suspend fun setCurrency(currency: Currency) {
        dataStore.updateData { it.copy(preferredCurrencyCode = currency.code) }
    }

    suspend fun clearPreferredCurrencyCode() {
        dataStore.updateData { it.copy(preferredCurrencyCode = null) }
    }

    // Synchronous-style suspend getters for one-off reads (e.g., inside Repositories or Interceptors)
    suspend fun getJwt(): String? = dataStore.data.first().jwt
    suspend fun getLocalIdCounter(): Long = dataStore.data.first().localIdCounter
    suspend fun isSyncEnabled(): Boolean = dataStore.data.first().isSyncEnabled
    suspend fun isBackupEnabled(): Boolean = dataStore.data.first().isBackupEnabled
    suspend fun isWifiOnlySyncEnabled(): Boolean = dataStore.data.first().isWifiOnlySyncEnabled
    suspend fun isLoggedIn(): Boolean = dataStore.data.first().isLoggedIn

    suspend fun getDefaultCurrencyCode(): String = getCurrency().code

    private suspend fun resolveDefaultCurrencyCode(): String = getCurrency().code

    suspend fun getCurrency(): Currency {
        val prefs = dataStore.data.first()

        // Try stored preference
        prefs.preferredCurrencyCode?.let { code ->
            currencyRepository.getByIsoCode(code)?.let { return it }
        }

        // Try device locale currency
        UserLocaleRegistry.currentGeographicCurrencyCode()?.let { code ->
            currencyRepository.getByIsoCode(code)?.let { return it }
        }

        // Fallback
        return currencyRepository.getAll().firstOrNull()
            ?: throw IllegalStateException("No currencies available in repository")
    }
}