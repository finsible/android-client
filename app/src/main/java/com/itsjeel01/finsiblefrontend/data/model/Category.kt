package com.itsjeel01.finsiblefrontend.data.model

import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.entity.CategoryEntity
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Long,
    val name: String,
    val icon: String,
    val readOnly: Boolean = true,
    val parentCategory: Long?,
    val subCategory: Boolean = false
)

fun Category.toEntity(
    type: TransactionType,
    syncStatus: Status = Status.COMPLETED
): CategoryEntity {
    return CategoryEntity(
        id = id,
        type = type,
        name = name,
        icon = icon,
        readOnly = readOnly,
        parentCategoryId = parentCategory ?: 0L,
        syncStatus = syncStatus
    )
}