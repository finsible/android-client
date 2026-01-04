package com.itsjeel01.finsiblefrontend.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AccountGroupCreateRequest(
    val name: String,
    val description: String,
    val icon: String,
    val color: String
)

