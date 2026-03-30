package com.itsjeel01.finsiblefrontend.ui.component.templates.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/** Immutable holder for Icon Badge colors. */
@Immutable
data class FinsibleIconBadgeColors(
    val iconTint: Color,
    val backgroundTint: Color
)

/** Immutable holder for Icon Badge dimensions. */
@Immutable
data class FinsibleIconBadgeSizes(
    val containerSize: Dp,
    val iconSize: Dp,
    val roundedCornerRadius: Dp
)

