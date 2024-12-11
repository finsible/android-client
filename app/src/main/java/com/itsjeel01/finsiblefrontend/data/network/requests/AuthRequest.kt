package com.itsjeel01.finsiblefrontend.data.network.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val clientId: String,
    val token: String,
)