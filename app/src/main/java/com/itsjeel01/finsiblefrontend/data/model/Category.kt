package com.itsjeel01.finsiblefrontend.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Long,
    val name: String,
    val color: String,
)
