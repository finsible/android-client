package com.itsjeel01.finsiblefrontend.ui.model

import androidx.compose.runtime.Immutable

/** Stable UI Model for account display, optimized for Compose. */
@Immutable
data class AccountUiModel(
    val id: Long,
    val name: String,
    val description: String,
    val icon: String,
    val formattedBalance: String,
    val groupColor: String?,
    val isPositiveBalance: Boolean
)
