package com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant

import androidx.compose.runtime.Immutable

/** Rotation modes supported by tile cards. */
@Immutable
enum class FinsibleTileCardRotationVariant {
    Sequential,
    Carousel
}

/** Slot composition variants used to render tile card sections. */
@Immutable
internal enum class TileCardSlotVariant(
    val hasStatistics: Boolean,
    val useCompactStats: Boolean
) {
    HeroOnly(
        hasStatistics = false,
        useCompactStats = false
    ),
    HeroAndStats(
        hasStatistics = true,
        useCompactStats = false
    ),
    HeroAndMeta(
        hasStatistics = false,
        useCompactStats = false
    ),
    HeroAndKpi(
        hasStatistics = false,
        useCompactStats = false
    ),
    HeroMetaAndKpi(
        hasStatistics = false,
        useCompactStats = false
    ),
    HeroMetaAndStats(
        hasStatistics = true,
        useCompactStats = false
    ),
    HeroKpiAndStats(
        hasStatistics = true,
        useCompactStats = false
    ),
    HeroMetaKpiAndStats(
        hasStatistics = true,
        useCompactStats = true
    )
}

