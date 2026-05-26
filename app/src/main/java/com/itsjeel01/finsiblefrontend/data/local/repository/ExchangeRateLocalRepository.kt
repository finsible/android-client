package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.data.local.entity.ExchangeRateEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.ExchangeRateEntity_
import io.objectbox.Box
import io.objectbox.kotlin.equal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateLocalRepository @Inject constructor(
    private val box: Box<ExchangeRateEntity>
) {
    /**
     * Fetches a rate directly or mathematically calculates the inverse if the direct pair is missing.
     * Returns the Entity proxy so the Coordinator can check the `lastSyncedAt` timestamp.
     */
    fun getRateEntity(baseCode: String, targetCode: String): ExchangeRateEntity? {
        if (baseCode == targetCode) {
            return ExchangeRateEntity().apply {
                rate = 1.0
                lastSyncedAt = System.currentTimeMillis()
            }
        }

        val directPair = "${baseCode}_${targetCode}"

        val direct = box.query()
            .apply(ExchangeRateEntity_.pairCode.equal(directPair))
            .build()
            .use { query -> query.findFirst() }

        if (direct != null) return direct

        val inversePair = "${targetCode}_${baseCode}"

        val inverse = box.query()
            .apply(ExchangeRateEntity_.pairCode.equal(inversePair))
            .build()
            .use { query -> query.findFirst() }

        if (inverse != null && inverse.rate != 0.0) {
            return ExchangeRateEntity().apply {
                this.pairCode = directPair
                this.baseCurrencyCode = baseCode
                this.targetCurrencyCode = targetCode
                this.rate = 1.0 / inverse.rate
                this.lastSyncedAt = inverse.lastSyncedAt
            }
        }

        return null
    }

    fun saveRates(rates: List<ExchangeRateEntity>) {
        box.put(rates)
    }
}