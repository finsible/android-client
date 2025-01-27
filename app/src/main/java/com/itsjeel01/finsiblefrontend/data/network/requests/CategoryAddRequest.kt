package com.itsjeel01.finsiblefrontend.data.network.requests

import kotlinx.serialization.Serializable

@Serializable
data class CategoryAddRequest(
    val name: String,
    val color: Int,
)