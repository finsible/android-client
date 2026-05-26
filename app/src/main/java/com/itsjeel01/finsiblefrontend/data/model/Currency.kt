package com.itsjeel01.finsiblefrontend.data.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Currency(
    val code: String,
    val symbol: String,
    val name: String,
    val flagEmoji: String,
    val localeTag: String? = null,
)