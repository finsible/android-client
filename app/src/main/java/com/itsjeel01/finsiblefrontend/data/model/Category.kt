package com.itsjeel01.finsiblefrontend.data.model

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

fun Category.toEntity(type: TransactionType): CategoryEntity {
    return CategoryEntity(id, type, name, icon, readOnly, parentCategory ?: 0L)
}