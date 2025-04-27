package com.itsjeel01.finsiblefrontend.core.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class CategoryAddRequest(
    val name: String,
    val color: String,
)