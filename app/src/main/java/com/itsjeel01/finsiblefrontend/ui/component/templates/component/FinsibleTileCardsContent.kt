package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTileCardColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTileCardData
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTileCardSizes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTileCardRotationVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
internal fun TileCardFace(
    card: FinsibleTileCardData,
    colors: FinsibleTileCardColors,
    sizes: FinsibleTileCardSizes,
    shape: RoundedCornerShape,
    rotationVariant: FinsibleTileCardRotationVariant,
    decorativeIcon: (@Composable () -> Unit)?,
    isFront: Boolean,
    showFace: Boolean
) {
    val backgroundBrush = card.backgroundBrush ?: Brush.verticalGradient(
        colors = listOf(colors.containerColor, colors.containerColor)
    )
    val slotVariant = remember(card) { resolveSlotVariant(card) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                rotationY = if (isFront) 0f else 180f
                alpha = if (showFace) 1f else 0f
            }
            .clip(shape)
            .background(backgroundBrush)
            .padding(sizes.padding)
    ) {
        val isNarrowCard = maxWidth <= FinsibleTheme.dimes.d260

        Column(modifier = Modifier.fillMaxWidth()) {
            TileCardHeader(
                card = card,
                sizes = sizes,
                colors = colors,
                rotationVariant = rotationVariant,
                decorativeIcon = decorativeIcon,
                isNarrowCard = isNarrowCard
            )

            Spacer(Modifier.height(FinsibleTheme.dimes.d8))

            FinsibleText(
                text = card.heroText,
                textStyleOverride = sizes.heroStyle,
                color = colors.contentColor,
                maxLines = if (isNarrowCard) 2 else 1,
                overflow = if (isNarrowCard) TextOverflow.Clip else TextOverflow.Ellipsis
            )

            if (!card.kpiText.isNullOrBlank()) {
                Spacer(Modifier.height(FinsibleTheme.dimes.d8))
                FinsibleText(
                    text = card.kpiText,
                    textStyleOverride = sizes.kpiStyle,
                    color = if (card.kpiPositive) colors.positiveKpiColor else colors.negativeKpiColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(slotVariant.bodyTopSpacing()))

            if (slotVariant.hasStatistics) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(FinsibleTheme.dimes.d1)
                        .background(colors.dividerColor)
                )

                Spacer(Modifier.height(FinsibleTheme.dimes.d12))

                if (slotVariant.useCompactStats) {
                    CompactStatisticsRow(card = card, colors = colors, sizes = sizes, isNarrowCard = isNarrowCard)
                } else {
                    StandardStatisticsRow(card = card, colors = colors, sizes = sizes, isNarrowCard = isNarrowCard)
                }
            }
        }
    }
}

@Composable
private fun TileCardHeader(
    card: FinsibleTileCardData,
    sizes: FinsibleTileCardSizes,
    colors: FinsibleTileCardColors,
    rotationVariant: FinsibleTileCardRotationVariant,
    decorativeIcon: (@Composable () -> Unit)?,
    isNarrowCard: Boolean
) {
    val trailingIcon: (@Composable () -> Unit)? = when {
        rotationVariant == FinsibleTileCardRotationVariant.Sequential -> {
            { DefaultRotationIcon(iconSize = sizes.rotationIconSize) }
        }

        else -> decorativeIcon
    }
    val trailingIconTint = if (rotationVariant == FinsibleTileCardRotationVariant.Sequential) {
        colors.rotationIconTint.copy(alpha = 0.86f)
    } else {
        colors.decorativeIconTint
    }

    Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FinsibleText(
                text = card.title,
                textStyleOverride = sizes.titleStyle,
                color = colors.contentColor,
                maxLines = if (isNarrowCard) 2 else 1,
                overflow = if (isNarrowCard) TextOverflow.Clip else TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            if (trailingIcon != null) {
                CompositionLocalProvider(LocalContentColor provides trailingIconTint) {
                    trailingIcon()
                }
            }
        }

        if (!card.subtitle.isNullOrBlank() || !card.pillText.isNullOrBlank()) {
            if (isNarrowCard) {
                Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4)) {
                    if (!card.subtitle.isNullOrBlank()) {
                        FinsibleText(
                            text = card.subtitle,
                            textStyleOverride = sizes.subtitleStyle,
                            color = colors.subtitleColor,
                            maxLines = 2,
                            overflow = TextOverflow.Clip
                        )
                    }
                    PillText(pillText = card.pillText, sizes = sizes, colors = colors)
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!card.subtitle.isNullOrBlank()) {
                        FinsibleText(
                            text = card.subtitle,
                            textStyleOverride = sizes.subtitleStyle,
                            color = colors.subtitleColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Spacer(Modifier.width(FinsibleTheme.dimes.d8))
                    PillText(pillText = card.pillText, sizes = sizes, colors = colors)
                }
            }
        }
    }
}

@Composable
private fun DefaultRotationIcon(iconSize: Dp) {
    Icon(
        painter = painterResource(com.composables.icons.lucide.R.drawable.lucide_ic_refresh_cw),
        contentDescription = null,
        modifier = Modifier.size(iconSize)
    )
}

@Composable
private fun PillText(
    pillText: String?,
    sizes: FinsibleTileCardSizes,
    colors: FinsibleTileCardColors
) {
    if (pillText.isNullOrBlank()) return

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(percent = 50))
            .background(colors.pillContainerColor)
            .padding(horizontal = FinsibleTheme.dimes.d8, vertical = FinsibleTheme.dimes.d4)
    ) {
        FinsibleText(
            text = pillText,
            textStyleOverride = sizes.pillStyle,
            color = colors.pillContentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun StandardStatisticsRow(
    card: FinsibleTileCardData,
    colors: FinsibleTileCardColors,
    sizes: FinsibleTileCardSizes,
    isNarrowCard: Boolean
) {
    if (isNarrowCard) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(sizes.statSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            card.statistics.forEach { stat ->
                FinsibleText(
                    text = stat.title,
                    textStyleOverride = sizes.statTitleStyle,
                    color = colors.subtitleColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                FinsibleText(
                    text = stat.value,
                    textStyleOverride = sizes.statValueStyle,
                    color = colors.contentColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        return
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        card.statistics.forEachIndexed { index, stat ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FinsibleText(
                    text = stat.title,
                    textStyleOverride = sizes.statTitleStyle,
                    color = colors.subtitleColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(FinsibleTheme.dimes.d4))
                FinsibleText(
                    text = stat.value,
                    textStyleOverride = sizes.statValueStyle,
                    color = colors.contentColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (index < card.statistics.lastIndex) {
                Spacer(Modifier.width(FinsibleTheme.dimes.d8))
            }
        }
    }
}

@Composable
private fun CompactStatisticsRow(
    card: FinsibleTileCardData,
    colors: FinsibleTileCardColors,
    sizes: FinsibleTileCardSizes,
    isNarrowCard: Boolean
) {
    if (isNarrowCard) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(sizes.statSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            card.statistics.forEachIndexed { index, stat ->
                FinsibleText(
                    text = stat.title,
                    textStyleOverride = sizes.subtitleStyle,
                    color = colors.subtitleColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                FinsibleText(
                    text = stat.value,
                    textStyleOverride = sizes.kpiStyle,
                    color = colors.contentColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                if (index < card.statistics.lastIndex) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.38f)
                            .height(FinsibleTheme.dimes.d1)
                            .background(colors.dividerColor)
                    )
                }
            }
        }
        return
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)
    ) {
        card.statistics.forEachIndexed { index, stat ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FinsibleText(
                    text = stat.title,
                    textStyleOverride = sizes.subtitleStyle,
                    color = colors.subtitleColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(FinsibleTheme.dimes.d2))
                FinsibleText(
                    text = stat.value,
                    textStyleOverride = sizes.kpiStyle,
                    color = colors.contentColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (index < card.statistics.lastIndex) {
                Box(
                    modifier = Modifier
                        .width(FinsibleTheme.dimes.d1)
                        .height(FinsibleTheme.dimes.d36)
                        .background(colors.dividerColor)
                )
            }
        }
    }
}


