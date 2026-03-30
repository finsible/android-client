package com.itsjeel01.finsiblefrontend.ui.component.templates.model

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

/** Immutable holder for Filter Chip colors. */
@Immutable
data class FinsibleFilterChipColors(
    val selectedContainerColor: Color,
    val selectedLabelColor: Color,
    val selectedIconTint: Color,
    val selectedBorderColor: Color,
    val unselectedContainerColor: Color,
    val unselectedLabelColor: Color,
    val unselectedIconTint: Color,
    val unselectedBorderColor: Color,
    val disabledSelectedContainerColor: Color,
    val disabledSelectedLabelColor: Color,
    val disabledSelectedIconTint: Color,
    val disabledSelectedBorderColor: Color,
    val disabledUnselectedContainerColor: Color,
    val disabledUnselectedLabelColor: Color,
    val disabledUnselectedIconTint: Color,
    val disabledUnselectedBorderColor: Color,
    val rippleColor: Color
)

/** Immutable holder for Filter Chip dimensions and typography. */
@Immutable
data class FinsibleFilterChipSizes(
    val height: Dp,
    val contentPadding: PaddingValues,
    val textStyle: TextStyle,
    val iconSize: Dp,
    val iconSpacing: Dp,
    val borderWidth: Dp,
    val cornerRadius: Dp
)

