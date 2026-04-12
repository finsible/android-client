package com.itsjeel01.finsiblefrontend.ui.component.templates.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

/** Immutable holder for radio button colors. */
@Immutable
data class FinsibleRadioButtonColors(
    val selectedRingColor: Color,
    val unselectedRingColor: Color,
    val selectedDotColor: Color,
    val selectedLabelColor: Color,
    val unselectedLabelColor: Color,
    val disabledSelectedRingColor: Color,
    val disabledUnselectedRingColor: Color,
    val disabledSelectedDotColor: Color,
    val disabledSelectedLabelColor: Color,
    val disabledUnselectedLabelColor: Color,
    val rippleColor: Color
)

/** Immutable holder for radio button sizing. */
@Immutable
data class FinsibleRadioButtonSizes(
    val outerDiameter: Dp,
    val ringWidth: Dp,
    val dotDiameter: Dp,
    val iconSize: Dp,
    val spacing: Dp,
    val labelStyle: TextStyle
)

