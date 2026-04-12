package com.itsjeel01.finsiblefrontend.ui.component.templates.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

/** Immutable holder for toggle colors. */
@Immutable
data class FinsibleToggleColors(
    val trackOnColor: Color,
    val thumbOnColor: Color,
    val trackOffColor: Color,
    val thumbOffColor: Color,
    val disabledTrackColor: Color,
    val disabledThumbColor: Color,
    val labelColor: Color,
    val disabledLabelColor: Color,
)

/** Immutable holder for toggle sizing. */
@Immutable
data class FinsibleToggleSizes(
    val width: Dp,
    val height: Dp,
    val thumbDiameter: Dp,
    val padding: Dp,
    val labelStyle: TextStyle,
    val iconSize: Dp,
    val spacing: Dp
)

/** Label placement relative to the toggle thumb. */
@Immutable
enum class FinsibleToggleLabelPosition {
    Leading,
    Trailing
}

/** Arrangement of label and toggle within the row. */
@Immutable
enum class FinsibleToggleArrangement {
    Attached,
    SpaceBetween
}

