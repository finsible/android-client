package com.itsjeel01.finsiblefrontend.data.remote.model

import kotlinx.serialization.Serializable

/** Server snapshot of entity counts for integrity verification. */
@Serializable
data class EntitySnapshot(
    val categories: Int,
    val accountGroups: Int,
    val accounts: Int,
    val transactions: Int,
    val lastModified: String? = null // ISO-8601 timestamp, optional for display
)

