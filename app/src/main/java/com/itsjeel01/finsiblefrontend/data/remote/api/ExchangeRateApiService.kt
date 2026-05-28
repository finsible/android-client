package com.itsjeel01.finsiblefrontend.data.remote.api

import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import com.itsjeel01.finsiblefrontend.data.remote.model.ExchangeRatesData
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateApiService {
    @GET("currency/rates")
    suspend fun getExchangeRates(
        @Query("base") baseCurrencyCode: String
    ): BaseResponse<ExchangeRatesData>
}