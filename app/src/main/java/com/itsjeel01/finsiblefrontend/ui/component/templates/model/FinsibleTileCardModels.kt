package com.itsjeel01.finsiblefrontend.ui.component.templates.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import kotlinx.collections.immutable.ImmutableList

/** Immutable holder for tile card colors. */
@Immutable
data class FinsibleTileCardColors(
    val containerColor: Color,
    val contentColor: Color,
    val decorativeIconTint: Color,
    val rotationIconTint: Color,
    val subtitleColor: Color = Color.Unspecified,
    val dividerColor: Color = Color.Unspecified,
    val pillContainerColor: Color = Color.Unspecified,
    val pillContentColor: Color = Color.Unspecified,
    val positiveKpiColor: Color = Color.Unspecified,
    val negativeKpiColor: Color = Color.Unspecified
)

/** Immutable holder for tile card sizing. */
@Immutable
data class FinsibleTileCardSizes(
    val cornerRadius: Dp,
    val padding: Dp,
    val statSpacing: Dp,
    val statTitleStyle: TextStyle,
    val statValueStyle: TextStyle,
    val titleStyle: TextStyle,
    val heroStyle: TextStyle,
    val iconSize: Dp,
    val rotationIconSize: Dp = iconSize * 0.7f,
    val subtitleStyle: TextStyle = TextStyle.Default,
    val pillStyle: TextStyle = TextStyle.Default,
    val kpiStyle: TextStyle = TextStyle.Default
)

/** Represents a single tile card item. */
@Immutable
data class FinsibleTileCardData(
    val title: String,
    val heroText: String,
    val statistics: ImmutableList<com.itsjeel01.finsiblefrontend.ui.model.item.StatEntry>,
    val backgroundBrush: Brush? = null,
    val subtitle: String? = null,
    val pillText: String? = null,
    val kpiText: String? = null,
    val kpiPositive: Boolean = true
)


