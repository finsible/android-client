package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.data.local.entity.ExchangeRateEntity
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.repository.ExchangeRateLocalRepository
import com.itsjeel01.finsiblefrontend.data.repository.ExchangeRateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateSyncHandler @Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepository,
    private val exchangeRateLocalRepository: ExchangeRateLocalRepository
) {
    /** Fetches the latest rates remotely and caches them locally. */
    suspend fun refreshRatesAndCache(baseCurrencyCode: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = exchangeRateRepository.fetchRates(baseCurrencyCode)
            if (!response.success) {
                Logger.Network.w("Exchange rates fetch failed (base=$baseCurrencyCode): ${response.message}")
                return@withContext false
            }

            val now = System.currentTimeMillis()
            val entities = response.data.rates.map { (targetCode, rate) ->
                ExchangeRateEntity().apply {
                    pairCode = "${response.data.base}_$targetCode"
                    baseCurrencyCode = response.data.base
                    targetCurrencyCode = targetCode
                    this.rate = rate
                    lastSyncedAt = now
                }
            }

            exchangeRateLocalRepository.saveRates(entities)
            Logger.Cache.i("Cached ${entities.size} exchange rates (base=${response.data.base})")
            true
        } catch (e: Exception) {
            Logger.Network.w("Exchange rates refresh failed (base=$baseCurrencyCode)", e)
            false
        }
    }
}


