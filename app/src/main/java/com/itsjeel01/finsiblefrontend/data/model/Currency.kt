package com.itsjeel01.finsiblefrontend.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Currency(
    val code: String,
    val symbol: String,
    val name: String,
    val flagEmoji: String,
    val localeTag: String? = null,
)