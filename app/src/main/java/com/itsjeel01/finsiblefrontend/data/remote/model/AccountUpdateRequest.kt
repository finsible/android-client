package com.itsjeel01.finsiblefrontend.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AccountUpdateRequest(
    val name: String? = null,
    val description: String? = null,
    val balance: String? = null,
    val currencyCode: String? = null,
    val icon: String? = null,
    val accountGroupId: Long? = null,
    val isActive: Boolean? = null
)

