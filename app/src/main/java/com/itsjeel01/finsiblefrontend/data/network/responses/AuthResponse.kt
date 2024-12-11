package com.itsjeel01.finsiblefrontend.data.network.responses

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val success: Boolean,
    val status: Int,
    val isNewUser: Boolean,
    val userId: String,
    val email: String,
    val name: String,
    val picture: String,
    val accountCreated: String,
    val jwt: String,
    val message: String?,
)