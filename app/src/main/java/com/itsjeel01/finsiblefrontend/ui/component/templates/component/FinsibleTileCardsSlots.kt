package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.runtime.Composable
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTileCardData
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.TileCardSlotVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

internal fun resolveSlotVariant(card: FinsibleTileCardData): TileCardSlotVariant {
    val hasSubtitle = !card.subtitle.isNullOrBlank()
    val hasPill = !card.pillText.isNullOrBlank()
    val hasKpi = !card.kpiText.isNullOrBlank()
    val hasStats = card.statistics.isNotEmpty()
    val hasMeta = hasSubtitle || hasPill

    return when {
        !hasStats && hasMeta && hasKpi -> TileCardSlotVariant.HeroMetaAndKpi
        !hasStats && hasMeta -> TileCardSlotVariant.HeroAndMeta
        !hasStats && hasKpi -> TileCardSlotVariant.HeroAndKpi
        !hasStats -> TileCardSlotVariant.HeroOnly
        hasMeta && hasKpi -> TileCardSlotVariant.HeroMetaKpiAndStats
        hasMeta -> TileCardSlotVariant.HeroMetaAndStats
        hasKpi -> TileCardSlotVariant.HeroKpiAndStats
        else -> TileCardSlotVariant.HeroAndStats
    }
}

@Composable
internal fun TileCardSlotVariant.bodyTopSpacing() = when (this) {
    TileCardSlotVariant.HeroOnly -> FinsibleTheme.dimes.d8
    TileCardSlotVariant.HeroAndMeta,
    TileCardSlotVariant.HeroAndKpi,
    TileCardSlotVariant.HeroMetaAndKpi -> FinsibleTheme.dimes.d10

    TileCardSlotVariant.HeroAndStats -> FinsibleTheme.dimes.d16
    TileCardSlotVariant.HeroMetaAndStats,
    TileCardSlotVariant.HeroKpiAndStats -> FinsibleTheme.dimes.d14

    TileCardSlotVariant.HeroMetaKpiAndStats -> FinsibleTheme.dimes.d12
}

internal fun allowedStats(size: FinsibleSize): Int = when (size) {
    FinsibleSize.ExtraSmall -> 1
    FinsibleSize.Small -> 2
    else -> 3
}


