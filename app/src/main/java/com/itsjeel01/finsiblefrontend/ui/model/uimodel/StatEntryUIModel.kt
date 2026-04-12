package com.itsjeel01.finsiblefrontend.ui.model.uimodel

import androidx.compose.runtime.Immutable

/** Represents a single statistic item with title and value. */
@Immutable
data class StatEntryUIModel(
    val title: String,
    val value: String
)

