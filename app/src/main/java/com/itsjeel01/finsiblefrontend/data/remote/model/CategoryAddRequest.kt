package com.itsjeel01.finsiblefrontend.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryAddRequest(
    val name: String,
    val color: String,
)
