package com.itsjeel01.finsiblefrontend.ui.component.templates.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

/** Icon style variants for accordion affordances. */
@Immutable
enum class FinsibleAccordionIconVariant {
    Chevron,
    PlusMinus
}

/** Immutable holder for accordion colors. */
@Immutable
data class FinsibleAccordionColors(
    val containerColor: Color,
    val titleColor: Color,
    val subtitleColor: Color,
    val iconTint: Color,
    val disabledIconTint: Color,
    val borderColor: Color,
    val rippleColor: Color
)

/** Immutable holder for accordion sizing. */
@Immutable
data class FinsibleAccordionSizes(
    val cornerRadius: Dp,
    val padding: Dp,
    val titleStyle: TextStyle,
    val subtitleStyle: TextStyle,
    val iconSize: Dp,
    val spacing: Dp
)

