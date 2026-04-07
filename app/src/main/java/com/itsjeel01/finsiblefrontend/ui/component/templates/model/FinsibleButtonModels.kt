package com.itsjeel01.finsiblefrontend.ui.component.templates.model

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

/** Immutable holder for Button colors. */
@Immutable
data class FinsibleButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val borderColor: Color?, // Null means no border
    val badgeContainerColor: Color,
    val badgeContentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val disabledBorderColor: Color?,
    val rippleColor: Color
)

/** Immutable holder for Button sizes and typography. */
@Immutable
data class FinsibleButtonSizes(
    val contentPadding: PaddingValues,
    val textStyle: TextStyle,
    val iconSize: Dp,
    val iconSpacing: Dp,
)

/** Immutable holder for in-house badge metrics. */
@Immutable
data class FinsibleButtonBadgeSpec(
    val diameter: Dp,
    val textStyle: TextStyle
)