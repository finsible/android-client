package com.itsjeel01.finsiblefrontend.ui.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/** Represents data for a single FlippableCard display from ViewModel. */
@Immutable
data class FlippableCardData(
    val title: String,
    val largeText: String,
    val statistics: ImmutableList<StatisticsModel> = persistentListOf()
)

/** Represents a single statistic item with title and value. */
@Immutable
data class StatisticsModel(
    val title: String,
    val value: String
)
