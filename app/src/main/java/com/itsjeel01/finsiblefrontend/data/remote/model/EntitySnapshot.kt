package com.itsjeel01.finsiblefrontend.data.remote.model

import kotlinx.serialization.Serializable

/** Server snapshot of entity counts for integrity verification. */
@Serializable
data class EntitySnapshot(
    val categories: Int,
    val accountGroups: Int,
    val accounts: Int,
    val transactions: Int,
    /** UTC epoch milliseconds for server snapshot last modified time (optional). */
    val lastModified: Long? = null
)

