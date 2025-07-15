package com.itsjeel01.finsiblefrontend.core.network.model.base

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val success: Boolean,
    val status: Int,
    val message: String,
    val data: T,
)