package com.itsjeel01.finsiblefrontend.data.di

import android.content.Context
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.ExchangeRateEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.ExchangeRateLocalRepository
import io.objectbox.BoxStore
import io.objectbox.android.Admin
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DebugObjectBoxAdminStarter @Inject constructor() : ObjectBoxAdminStarter {
	override fun start(store: BoxStore, context: Context) {
		val started = Admin(store).start(context)
		Logger.Database.d("ObjectBox Admin started: $started")
		seedExchangeRates(store, context)
	}

	@Serializable
	private data class ExchangeRateSeed(
		val baseCurrencyCode: String,
		val targetCurrencyCode: String,
		val rate: Double
	)

	private fun seedExchangeRates(store: BoxStore, context: Context) {
		try {
			val json = Json { ignoreUnknownKeys = true }
			val seeds = context.resources.openRawResource(R.raw.mock_exchange_rates)
				.bufferedReader()
				.use { json.decodeFromString<List<ExchangeRateSeed>>(it.readText()) }

			val freshnessTimestamp = System.currentTimeMillis() + FRESHNESS_BUFFER_MS
			val rates = seeds.map { seed ->
				ExchangeRateEntity().apply {
					pairCode = "${seed.baseCurrencyCode}_${seed.targetCurrencyCode}"
					baseCurrencyCode = seed.baseCurrencyCode
					targetCurrencyCode = seed.targetCurrencyCode
					rate = seed.rate
					lastSyncedAt = freshnessTimestamp
				}
			}

			// Use a fresh repository instance so debug seed data lands in the same ObjectBox box.
			val repository = ExchangeRateLocalRepository(store.boxFor(ExchangeRateEntity::class.java))
			repository.saveRates(rates)
			Logger.Database.d("Seeded ${rates.size} exchange rates for debug conversion testing")
		} catch (e: Exception) {
			Logger.Database.w("Failed to seed debug exchange rates", e)
		}
	}

	private companion object {
		// Keep seeded mock rates fresh for months of testing, not just a single day.
		const val FRESHNESS_BUFFER_MS = 90L * 24L * 60L * 60L * 1000L
	}
}

