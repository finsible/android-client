package com.itsjeel01.finsiblefrontend.data.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Immutable currency data model for display and serialization.
 *
 * This model represents a currency with all metadata needed for UI rendering and server communication.
 * The flag emoji is stored as a Unicode string (e.g., "🇮🇳") to avoid asset management overhead.
 *
 * @param code ISO 4217 currency code (e.g., "INR", "USD", "EUR")
 * @param symbol Currency symbol (e.g., "₹", "$", "€")
 * @param name Full descriptive name (e.g., "Indian Rupee", "US Dollar")
 * @param flagEmoji Unicode flag emoji for the currency's country (e.g., "🇮🇳", "🇺🇸", "🇪🇺")
 * @param localeTag Locale tag for the currency (e.g., "en-IN", "en-US", "de-DE")
 */
@Immutable
@Serializable
data class Currency(
    val code: String,
    val symbol: String,
    val name: String,
    val flagEmoji: String,
    val localeTag: String? = null,
)

@Serializable
enum class CompactSystem {
    @SerialName("standard")
    STANDARD,

    @SerialName("indian")
    INDIAN
}