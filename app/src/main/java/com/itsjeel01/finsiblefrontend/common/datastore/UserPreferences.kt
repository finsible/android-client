package com.itsjeel01.finsiblefrontend.common.datastore

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val jwt: String? = null,
    val isLoggedIn: Boolean = false,
    val userId: String? = null,
    val email: String? = null,
    val name: String? = null,
    val localIdCounter: Long = 0L,
    val isSyncEnabled: Boolean = false,
    val isBackupEnabled: Boolean = false,
    val isWifiOnlySyncEnabled: Boolean = true,
    val preferredCurrencyCode: String? = null
)