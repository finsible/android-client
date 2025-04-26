package com.itsjeel01.finsiblefrontend.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class CategoryData(
    val type: String,
    val categories: List<Category>,
)

@Serializable
data class Category(
    val id: Long,
    val name: String,
    val color: Int,
)