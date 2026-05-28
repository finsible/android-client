package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.ExchangeRateEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.ExchangeRateEntity_
import io.objectbox.Box
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
            .findFirst()

        if (direct != null) return direct

        val inversePair = "${targetCode}_${baseCode}"

        val inverse = box.query()
            .apply(ExchangeRateEntity_.pairCode.equal(inversePair))
            .build()
            .findFirst()

        if (inverse != null) {
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
        if (rates.isEmpty()) return

        val deduplicatedRates = rates.distinctBy { it.pairCode }

        box.store.callInTx {
            val pairCodes = deduplicatedRates.map(ExchangeRateEntity::pairCode).toTypedArray()
            val existingByPairCode = box.query()
                .apply(ExchangeRateEntity_.pairCode.oneOf(pairCodes))
                .build()
                .find()
                .associateBy { it.pairCode }

            deduplicatedRates.forEach { rate ->
                existingByPairCode[rate.pairCode]?.let { rate.id = it.id }
            }

            box.put(deduplicatedRates)
        }

        Logger.Database.d("Saved ${deduplicatedRates.size} exchange rates (deduplicated from ${rates.size})")
    }
}