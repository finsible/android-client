package com.itsjeel01.finsiblefrontend.data.repository

import android.content.Context
import com.itsjeel01.finsiblefrontend.data.model.Currency
import com.itsjeel01.finsiblefrontend.data.sync.ScopeManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/** Asset-backed repository for currency data.
 * Currencies are parsed once per session from `currencies.json` and cached in memory.
 * No runtime mutations are allowed; currencies are sourced exclusively from the asset.
 */
@Singleton
class CurrencyRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json,
    private val scopeManager: ScopeManager
) {

    private val fileName = "currencies.json"

    @Serializable
    internal data class CurrenciesAsset(
        @SerialName("currencies_version")
        val currenciesVersion: String,
        @SerialName("currencies")
        val currencies: List<Currency>
    )

    private val currenciesMap: Map<String, Currency> by lazy {
        loadCurrenciesFromAsset()
    }

    private val sortedCurrencies: List<Currency> by lazy {
        currenciesMap.values.sortedBy { it.code }
    }

    /**
     * Fetches all available currencies ordered by the provided preference.
     *
     * **Ordering:**
     * - If `preferredCurrencyCode` is provided and valid, it is placed first. Remaining currencies are sorted alphabetically by code.
     * - If `preferredCurrencyCode` is null or invalid, returns alphabetically sorted list.
     */
    fun getAll(preferredCurrencyCode: String? = null): List<Currency> {
        if (preferredCurrencyCode == null || !currenciesMap.containsKey(preferredCurrencyCode)) {
            return sortedCurrencies
        }

        val preferred = currenciesMap.getValue(preferredCurrencyCode)
        val others = sortedCurrencies.filter { it.code != preferredCurrencyCode }

        return listOf(preferred) + others
    }

    /** Looks up a currency by its ISO 4217 code. */
    fun getByIsoCode(code: String): Currency? {
        return currenciesMap[code]
    }

    /** Retrieves the flag emoji associated with a given ISO 4217 currency code, if available. */
    fun getFlagEmojiByIsoCode(code: String): String? {
        return currenciesMap[code]?.flagEmoji
    }

    /** * Pre-warms the currency cache during app startup.
     * Note: This should ideally be called from a background thread to avoid blocking the Main thread with disk I/O.
     */
    fun initialize() {
        scopeManager.scope.launch {
            currenciesMap // Accessing the property triggers the lazy initialization
        }
    }

    private fun loadCurrenciesFromAsset(): Map<String, Currency> {
        return try {
            val content = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val asset = json.decodeFromString<CurrenciesAsset>(content)
            asset.currencies.associateBy { it.code }
        } catch (e: Exception) {
            throw IllegalStateException("Failed to load currencies from asset: ${e.message}", e)
        }
    }
}