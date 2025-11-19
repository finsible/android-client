package com.itsjeel01.finsiblefrontend.data.remote.model

import com.itsjeel01.finsiblefrontend.data.model.Category
import kotlinx.serialization.Serializable

@Serializable
data class CategoriesData(
    val type: String,
    val categories: List<Category>,
)