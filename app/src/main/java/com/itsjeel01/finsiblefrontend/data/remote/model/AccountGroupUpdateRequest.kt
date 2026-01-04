package com.itsjeel01.finsiblefrontend.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AccountGroupUpdateRequest(
    val name: String? = null,
    val description: String? = null,
    val icon: String? = null,
    val color: String? = null
)

