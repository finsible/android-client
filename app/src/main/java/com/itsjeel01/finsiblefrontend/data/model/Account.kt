package com.itsjeel01.finsiblefrontend.data.model

import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.toAmountCentis
import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: Long,
    val name: String,
    val description: String,
    val accountGroupId: Long? = null,
    /** Balance as decimal String for server transmission. E.g., "1234.56". */
    val balance: String,
    val currencyCode: String,
    val icon: String,
    val isActive: Boolean,
    val isSystemDefault: Boolean
)

fun Account.toEntity(
    syncStatus: Status = Status.COMPLETED
): AccountEntity {
    val centis = try {
        balance.toAmountCentis()
    } catch (e: Exception) {
        Logger.Database.e("Invalid balance: $balance for account $name(id: $id)", e)
        0L
    }
    return AccountEntity(
        id = id,
        name = name,
        description = description,
        balanceCentis = centis,
        currencyCode = currencyCode,
        icon = icon,
        isActive = isActive,
        isSystemDefault = isSystemDefault,
        syncStatus = syncStatus
    )
}