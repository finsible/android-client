package com.itsjeel01.finsiblefrontend.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryCreateRequest(
    val type: String,
    val name: String,
    val icon: String,
    val parentCategoryId: Long? = null
)

