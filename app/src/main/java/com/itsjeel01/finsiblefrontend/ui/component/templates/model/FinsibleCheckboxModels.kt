package com.itsjeel01.finsiblefrontend.ui.component.templates.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

/** Immutable holder for Checkbox colors. */
@Immutable
data class FinsibleCheckboxColors(
    val checkedContainerColor: Color,
    val checkedBorderColor: Color,
    val checkedIconColor: Color,
    val uncheckedContainerColor: Color,
    val uncheckedBorderColor: Color,
    val uncheckedIconColor: Color,
    val disabledCheckedContainerColor: Color,
    val disabledCheckedBorderColor: Color,
    val disabledCheckedIconColor: Color,
    val disabledUncheckedContainerColor: Color,
    val disabledUncheckedBorderColor: Color,
    val disabledUncheckedIconColor: Color,
    val labelColor: Color,
    val disabledLabelColor: Color,
    val rippleColor: Color
)

/** Immutable holder for Checkbox sizes and stroke values. */
@Immutable
data class FinsibleCheckboxSizes(
    val boxSize: Dp,
    val cornerRadius: Dp,
    val borderWidth: Dp,
    val checkStrokeWidth: Dp,
    val labelSpacing: Dp,
    val labelTextStyle: TextStyle
)

