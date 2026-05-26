package com.itsjeel01.finsiblefrontend.data.repository

import com.itsjeel01.finsiblefrontend.data.remote.api.ExchangeRateApiService
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import com.itsjeel01.finsiblefrontend.data.remote.model.ExchangeRatesData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateRepository @Inject constructor(
    private val apiService: ExchangeRateApiService
) {
    suspend fun fetchRates(baseCurrencyCode: String): BaseResponse<ExchangeRatesData> {
        return apiService.getExchangeRates(baseCurrencyCode)
    }
}