package com.itsjeel01.finsiblefrontend.data.repository

import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.ExchangeRateEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.ExchangeRateLocalRepository
import com.itsjeel01.finsiblefrontend.data.remote.api.ExchangeRateApiService
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import com.itsjeel01.finsiblefrontend.data.remote.model.ExchangeRatesData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateRepository @Inject constructor(
    private val apiService: ExchangeRateApiService,
    private val exchangeRateLocalRepository: ExchangeRateLocalRepository
) {
    suspend fun fetchRates(baseCurrencyCode: String): BaseResponse<ExchangeRatesData> {
        return apiService.getExchangeRates(baseCurrencyCode)
    }

    /** Fetches the latest rates for [baseCurrencyCode] and caches them into ObjectBox. */
    suspend fun refreshRatesAndCache(baseCurrencyCode: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = fetchRates(baseCurrencyCode)
            if (!response.success) {
                Logger.Network.w("Exchange rates fetch failed (base=$baseCurrencyCode): ${response.message}")
                return@withContext false
            }

            val data = response.data
            val now = System.currentTimeMillis()

            val entities = data.rates.map { (targetCode, rate) ->
                ExchangeRateEntity().apply {
                    pairCode = "${data.base}_$targetCode"
                    this.baseCurrencyCode = data.base
                    this.targetCurrencyCode = targetCode
                    this.rate = rate
                    lastSyncedAt = now
                }
            }

            exchangeRateLocalRepository.saveRates(entities)
            Logger.Cache.i("Cached ${entities.size} exchange rates (base=${data.base})")
            true
        } catch (e: Exception) {
            Logger.Network.w("Exchange rates refresh failed (base=$baseCurrencyCode)", e)
            false
        }
    }
}