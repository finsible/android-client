package com.itsjeel01.finsiblefrontend.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AccountCreateRequest(
    val name: String,
    val description: String,
    val balance: String,
    val currencyCode: String,
    val icon: String,
    val accountGroupId: Long?,
    val isActive: Boolean
)

