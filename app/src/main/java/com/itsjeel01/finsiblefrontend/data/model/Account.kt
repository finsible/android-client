package com.itsjeel01.finsiblefrontend.data.model

import android.icu.math.BigDecimal
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: Long,
    val name: String,
    val description: String,
    val accountGroupId: Long? = null,
    val balance: String,
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
        balance = try {
            BigDecimal(balance)
        } catch (e: NumberFormatException) {
            Logger.Database.e("Invalid balance: $balance for account $name(id: $id)", e)
            BigDecimal.ZERO
        },
        currencyCode = currencyCode,
        icon = icon,
        isActive = isActive,
        isSystemDefault = isSystemDefault
    )
}