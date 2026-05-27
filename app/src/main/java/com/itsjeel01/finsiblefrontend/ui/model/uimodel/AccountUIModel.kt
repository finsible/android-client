package com.itsjeel01.finsiblefrontend.ui.model.uimodel

import androidx.compose.runtime.Immutable

/** Stable UI Model for account display, optimized for Compose. */
@Immutable
data class AccountUIModel(
    val id: Long,
    val name: String,
    val description: String,
    val icon: String,
    val currencyCode: String,
    val formattedBalance: String,
    val groupColor: String?,
    val groupName: String?,
    val isPositiveBalance: Boolean,
    val usageCount: Long = 0,
    val lastUsedAt: Long? = null,
)
