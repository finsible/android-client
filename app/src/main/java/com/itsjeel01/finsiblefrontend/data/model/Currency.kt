package com.itsjeel01.finsiblefrontend.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Currency data model for display and serialization.
 *
 * This model represents a currency with all metadata needed for UI rendering and server communication.
 * The flag emoji is stored as a Unicode string (e.g., "🇮🇳") to avoid asset management overhead.
 */
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