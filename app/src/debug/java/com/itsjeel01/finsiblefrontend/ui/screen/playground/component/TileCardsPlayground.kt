package com.itsjeel01.finsiblefrontend.ui.screen.playground.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleIconBadge
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleTileCards
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.allowedStats
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleTileCardDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTileCardRotationVariant
import com.itsjeel01.finsiblefrontend.ui.model.item.StatEntry
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.OptionDropdown
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.OptionSlider
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.OptionToggle
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.sizeLabel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleGradients
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.GradientType
import kotlinx.collections.immutable.persistentListOf
import com.composables.icons.lucide.R as LucideR

@Composable
fun TileCardsPlayground() {
    var size by rememberSaveable { mutableStateOf(FinsibleSize.Medium) }
    var count by rememberSaveable { mutableStateOf(3f) }
    var rotationVariant by rememberSaveable { mutableStateOf(FinsibleTileCardRotationVariant.Carousel) }
    var showDecorativeIcon by rememberSaveable { mutableStateOf(false) }
    var showSubtitle by rememberSaveable { mutableStateOf(true) }
    var showPill by rememberSaveable { mutableStateOf(true) }
    var showTrend by rememberSaveable { mutableStateOf(true) }
    var inverted by rememberSaveable { mutableStateOf(false) }
    var statsCount by rememberSaveable { mutableStateOf(2f) }
    var carouselFraction by rememberSaveable { mutableStateOf(0.92f) }
    var backgroundPreset by rememberSaveable { mutableStateOf(TileBackgroundPreset.Default) }
    var contentVariant by rememberSaveable { mutableStateOf(TileContentVariantPreset.HeroWithMetaKpiAndStats) }
    val maxStatsForSize = allowedStats(size)

    val backgroundBrush = backgroundPreset.asBrush()

    val cards = List(count.toInt().coerceAtLeast(1)) { index ->
        val sample = contentVariant.sample(index, statsCount.toInt().coerceIn(0, maxStatsForSize))
        com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTileCardData(
            title = sample.title,
            subtitle = if (showSubtitle) sample.subtitle else null,
            heroText = sample.hero,
            statistics = sample.stats,
            backgroundBrush = backgroundBrush,
            pillText = if (showPill) sample.pill else null,
            kpiText = if (showTrend) sample.kpiText else null,
            kpiPositive = sample.kpiPositive
        )
    }.let { persistentListOf(*it.toTypedArray()) }
    val validationError = runCatching {
        validateTileCardsConfig(cards = cards, size = size, carouselCardFraction = carouselFraction)
    }.exceptionOrNull()?.message

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = FinsibleTheme.dimes.d16, vertical = FinsibleTheme.dimes.d12),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12)
    ) {
        if (validationError == null) {
            FinsibleTileCards(
                cards = cards,
                size = size,
                rotationVariant = rotationVariant,
                inverted = inverted,
                carouselCardFraction = carouselFraction,
                colors = FinsibleTileCardDefaults.colors(inverted = inverted),
                decorativeIcon = {
                    if (rotationVariant == FinsibleTileCardRotationVariant.Carousel && showDecorativeIcon) {
                        FinsibleIconBadge(icon = { Icon(painterResource(LucideR.drawable.lucide_ic_star), contentDescription = null) })
                    }
                }
            )
        } else {
            Text(
                text = "Invalid tile cards config: $validationError",
                color = FinsibleTheme.colors.error,
                style = FinsibleTheme.typography.t14
            )
        }

        OptionDropdown(
            label = stringResource(R.string.component_playground_tile_content_variant_label),
            selectedLabel = stringResource(contentVariant.labelRes),
            options = TileContentVariantPreset.entries,
            optionLabel = { stringResource(it.labelRes) },
            onSelect = {
                contentVariant = it
                showSubtitle = it.hasSubtitle
                showPill = it.hasPill
                showTrend = it.hasKpi
            }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_generic_size),
            selectedLabel = sizeLabel(size),
            options = listOf(FinsibleSize.Small, FinsibleSize.Medium, FinsibleSize.Large),
            optionLabel = { sizeLabel(it) },
            onSelect = {
                size = it
                statsCount = statsCount.coerceAtMost(allowedStats(it).toFloat())
            }
        )
        OptionSlider(
            label = stringResource(R.string.component_playground_tile_count),
            value = count,
            valueRange = 1f .. 6f,
            steps = 5,
            onValueChange = { count = it }
        )

        OptionSlider(
            label = stringResource(R.string.component_playground_tile_stats_count_label),
            value = statsCount,
            valueRange = 0f .. maxStatsForSize.toFloat(),
            steps = (maxStatsForSize - 1).coerceAtLeast(0),
            onValueChange = { statsCount = it.coerceIn(0f, maxStatsForSize.toFloat()) }
        )

        OptionSlider(
            label = stringResource(R.string.component_playground_tile_card_fraction_label),
            value = carouselFraction,
            valueRange = 0.6f .. 1f,
            steps = 4,
            onValueChange = { carouselFraction = it },
            helperText = stringResource(R.string.component_playground_tile_card_fraction_helper)
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_tile_background_label),
            selectedLabel = stringResource(backgroundPreset.labelRes),
            options = TileBackgroundPreset.entries,
            optionLabel = { stringResource(it.labelRes) },
            onSelect = { preset -> backgroundPreset = preset }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_tile_rotation_variant_label),
            selectedLabel = stringResource(
                if (rotationVariant == FinsibleTileCardRotationVariant.Sequential) R.string.component_playground_tile_rotation_sequential else R.string.component_playground_tile_rotation_carousel
            ),
            options = FinsibleTileCardRotationVariant.entries,
            optionLabel = {
                stringResource(
                    if (it == FinsibleTileCardRotationVariant.Sequential) R.string.component_playground_tile_rotation_sequential else R.string.component_playground_tile_rotation_carousel
                )
            },
            onSelect = { rotationVariant = it }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_tile_decorative_icon),
            checked = showDecorativeIcon,
            onCheckedChange = { showDecorativeIcon = it },
            enabled = rotationVariant == FinsibleTileCardRotationVariant.Carousel,
            helperText = if (rotationVariant == FinsibleTileCardRotationVariant.Sequential) stringResource(R.string.component_playground_tile_rotation_helper) else null
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_tile_subtitle_toggle),
            checked = showSubtitle,
            onCheckedChange = { showSubtitle = it }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_tile_pill_toggle),
            checked = showPill,
            onCheckedChange = { showPill = it }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_tile_trend_toggle),
            checked = showTrend,
            onCheckedChange = { showTrend = it }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_tile_inverted_toggle),
            checked = inverted,
            onCheckedChange = { inverted = it }
        )
    }
}


private fun validateTileCardsConfig(
    cards: List<com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTileCardData>,
    size: FinsibleSize,
    carouselCardFraction: Float
) {
    require(cards.isNotEmpty()) { "cards must not be empty." }
    require(cards.all { it.statistics.size <= allowedStats(size) }) { "Statistics exceed allowed count for size $size." }
    require(carouselCardFraction in 0f .. 1f && carouselCardFraction > 0f) { "carouselCardFraction must be within (0, 1]." }
}

private enum class TileBackgroundPreset(@StringRes val labelRes: Int, val gradientType: GradientType?) {
    Default(R.string.component_playground_tile_background_default, null),
    Brand(R.string.component_playground_tile_background_brand, GradientType.BRAND),
    NetWorth(R.string.component_playground_tile_background_net_worth, GradientType.NET_WORTH),
    Assets(R.string.component_playground_tile_background_assets, GradientType.ASSETS),
    Liabilities(R.string.component_playground_tile_background_liabilities, GradientType.LIABILITIES)
}

@Composable
private fun TileBackgroundPreset.asBrush(): Brush? = when (gradientType) {
    null -> null
    else -> FinsibleGradients.getLinearGradient(gradientType)
}

private enum class TileContentVariantPreset(
    @StringRes val labelRes: Int,
    val hasSubtitle: Boolean,
    val hasPill: Boolean,
    val hasKpi: Boolean
) {
    HeroOnly(R.string.component_playground_tile_content_variant_hero_only, false, false, false),
    HeroWithMeta(R.string.component_playground_tile_content_variant_hero_meta, true, true, false),
    HeroWithKpi(R.string.component_playground_tile_content_variant_hero_kpi, false, false, true),
    HeroWithMetaAndKpi(R.string.component_playground_tile_content_variant_hero_meta_kpi, true, true, true),
    HeroWithStats(R.string.component_playground_tile_content_variant_hero_stats, false, false, false),
    HeroWithMetaAndStats(R.string.component_playground_tile_content_variant_hero_meta_stats, true, true, false),
    HeroWithKpiAndStats(R.string.component_playground_tile_content_variant_hero_kpi_stats, false, false, true),
    HeroWithMetaKpiAndStats(R.string.component_playground_tile_content_variant_hero_meta_kpi_stats, true, true, true);

    fun sample(index: Int, statsCount: Int): TileSample {
        val allStats = persistentListOf(
            StatEntry(title = "Stat A", value = "24%"),
            StatEntry(title = "Stat B", value = "$342"),
            StatEntry(title = "Stat C", value = "7d")
        )
        return TileSample(
            title = "Card ${index + 1}",
            subtitle = "Optional subtitle slot",
            hero = "$12,480",
            pill = "Optional badge",
            kpiText = "+6.2% vs last period",
            kpiPositive = true,
            stats = if (
                this == HeroOnly ||
                this == HeroWithMeta ||
                this == HeroWithKpi ||
                this == HeroWithMetaAndKpi
            ) {
                persistentListOf()
            } else {
                persistentListOf(*allStats.take(statsCount).toTypedArray())
            }
        )
    }
}

private data class TileSample(
    val title: String,
    val subtitle: String,
    val hero: String,
    val pill: String,
    val kpiText: String,
    val kpiPositive: Boolean,
    val stats: kotlinx.collections.immutable.PersistentList<StatEntry>
)

