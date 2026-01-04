package com.itsjeel01.finsiblefrontend.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryUpdateRequest(
    val name: String? = null,
    val icon: String? = null
)

