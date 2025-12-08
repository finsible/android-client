package com.itsjeel01.finsiblefrontend.ui.model

/** Represents data for a single FlippableCard display from ViewModel. */
data class FlippableCardData(
    val title: String,
    val largeText: String,
    val statistics: List<StatisticsModel>
)

/** Represents a single statistic item with title and value. */
data class StatisticsModel(
    val title: String,
    val value: String
)


