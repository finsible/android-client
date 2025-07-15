package com.itsjeel01.finsiblefrontend.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthDataResponse(
    val isNewUser: Boolean,
    val userId: String,
    val email: String,
    val name: String,
    val picture: String,
    val accountCreated: String,
    val jwt: String,
)