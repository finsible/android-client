package com.itsjeel01.finsiblefrontend.common

import androidx.datastore.core.DataStore
import com.itsjeel01.finsiblefrontend.common.datastore.UserPreferences
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.model.Currency
import com.itsjeel01.finsiblefrontend.data.remote.model.AuthData
import com.itsjeel01.finsiblefrontend.data.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceManager @Inject constructor(
    private val dataStore: DataStore<UserPreferences>,
    private val currencyRepository: CurrencyRepository
) {
    val defaultCurrencyFlow: Flow<Currency> = dataStore.data.map { prefs ->
        resolveCurrency(prefs.preferredCurrencyCode)
    }.distinctUntilChanged()

    val defaultCurrencyCodeFlow: Flow<String> = defaultCurrencyFlow.map { it.code }

    val jwtFlow: Flow<String?> = dataStore.data.map { it.jwt }.distinctUntilChanged()

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

    private suspend fun <T> readPrefs(selector: (UserPreferences) -> T, default: T): T =
        runCatching { selector(dataStore.data.first()) }
            .onFailure { Logger.App.e("Failed to read preference, using default: $default", it) }
            .getOrDefault(default)

    private suspend fun <T> readPrefsNullable(selector: (UserPreferences) -> T?): T? =
        runCatching { selector(dataStore.data.first()) }
            .onFailure { Logger.App.e("Failed to read nullable preference, returning null", it) }
            .getOrNull()

    suspend fun getJwt(): String? = readPrefsNullable { it.jwt }
    suspend fun getLocalIdCounter(): Long = readPrefs({ it.localIdCounter }, default = 0L)
    suspend fun isSyncEnabled(): Boolean = readPrefs({ it.isSyncEnabled }, default = false)
    suspend fun isBackupEnabled(): Boolean = readPrefs({ it.isBackupEnabled }, default = false)
    suspend fun isWifiOnlySyncEnabled(): Boolean = readPrefs({ it.isWifiOnlySyncEnabled }, default = true)
    suspend fun isLoggedIn(): Boolean = readPrefs({ it.isLoggedIn }, default = false)
    suspend fun getDefaultCurrencyCode(): String = defaultCurrencyCodeFlow.first()
    suspend fun getCurrency(): Currency = defaultCurrencyFlow.first()

    private fun resolveCurrency(preferredCode: String?): Currency {
        // Try stored preference
        preferredCode?.let { code ->
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