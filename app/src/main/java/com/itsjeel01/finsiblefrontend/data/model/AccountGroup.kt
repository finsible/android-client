package com.itsjeel01.finsiblefrontend.data.model

import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountGroupEntity
import kotlinx.serialization.Serializable

@Serializable
data class AccountGroup(
    val id: Long,
    val name: String,
    val description: String,
    val icon: String,
    val color: String,
    val isSystemDefault: Boolean
)

fun AccountGroup.toEntity(
    syncStatus: Status = Status.COMPLETED
): AccountGroupEntity {
    return AccountGroupEntity(
        id = id,
        name = name,
        description = description,
        icon = icon,
        color = color,
        isSystemDefault = isSystemDefault,
        syncStatus = syncStatus
    )
}