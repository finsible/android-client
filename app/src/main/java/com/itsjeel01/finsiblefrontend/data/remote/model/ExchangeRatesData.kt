package com.itsjeel01.finsiblefrontend.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRatesData(
    val base: String,
    val rates: Map<String, Double>
)