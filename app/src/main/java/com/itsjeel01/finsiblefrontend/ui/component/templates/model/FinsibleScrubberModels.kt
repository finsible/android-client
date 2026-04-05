package com.itsjeel01.finsiblefrontend.ui.component.templates.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/** Immutable color contract for `FinsibleScrubber`. */
@Immutable
data class FinsibleScrubberColors(
    val currentColor: Color,
    val restColor: Color,
    val disabledCurrentColor: Color,
    val disabledRestColor: Color
)

/** Immutable sizing contract for `FinsibleScrubber`. */
@Immutable
data class FinsibleScrubberSizes(
    val activeBarWidth: Dp,
    val inactiveBarWidth: Dp,
    val barHeight: Dp,
    val barSpacing: Dp,
    val cornerRadius: Dp,
    val minTouchTargetHeight: Dp
)

