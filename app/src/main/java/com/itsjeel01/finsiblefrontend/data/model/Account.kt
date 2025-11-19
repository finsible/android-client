package com.itsjeel01.finsiblefrontend.data.model

import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: Long,
    val name: String,
    val description: String,
    val accountGroupId: Long,
    val balance: Double,
    val currencyCode: String,
    val icon: String,
    val isActive: Boolean,
    val isSystemDefault: Boolean
)

fun Account.toEntity(): AccountEntity {
    return AccountEntity(
        id = id,
        name = name,
        description = description,
        balance = balance,
        currencyCode = currencyCode,
        icon = icon,
        isActive = isActive,
        isSystemDefault = isSystemDefault
    )
}