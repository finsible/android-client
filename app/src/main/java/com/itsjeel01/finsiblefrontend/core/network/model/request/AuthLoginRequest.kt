package com.itsjeel01.finsiblefrontend.core.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class AuthLoginRequest(
    val clientId: String,
    val token: String,
)