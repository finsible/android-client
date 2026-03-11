package com.itsjeel01.finsiblefrontend.ui.model.item

import androidx.compose.runtime.Immutable

/** Stable UI model for an account group chip/filter, optimized for Compose. */
@Immutable
data class AccountGroupUIModel(
    val id: Long,
    val name: String,
    val color: String?,
)

