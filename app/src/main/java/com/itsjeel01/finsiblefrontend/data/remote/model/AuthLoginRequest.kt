package com.itsjeel01.finsiblefrontend.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthLoginRequest(
    val clientId: String,
    val token: String,
)
