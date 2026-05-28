package com.itsjeel01.finsiblefrontend.ui.model.uimodel

import androidx.compose.runtime.Immutable

/** Stable UI model for a category chip, optimized for Compose. */
@Immutable
data class CategoryUIModel(
    val id: Long,
    val name: String,
    val icon: String,
    val isParent: Boolean = false,
    val usageCount: Long = 0,
    val lastUsedAt: Long? = null,
)

