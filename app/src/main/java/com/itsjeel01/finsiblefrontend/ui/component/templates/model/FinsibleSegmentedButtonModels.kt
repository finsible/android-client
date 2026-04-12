package com.itsjeel01.finsiblefrontend.ui.component.templates.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

/** Immutable holder for segmented row colors. */
@Immutable
data class FinsibleSegmentedButtonColors(
    val selectedContainerColor: Color,
    val selectedContentColor: Color,
    val unselectedContainerColor: Color,
    val unselectedContentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val borderColor: Color,
    val rippleColor: Color
)

/** Immutable holder for segmented row sizing. */
@Immutable
data class FinsibleSegmentedButtonSizes(
    val textStyle: TextStyle,
    val iconSize: Dp,
    val iconSpacing: Dp,
    val horizontalPadding: Dp,
    val verticalPadding: Dp
)

/** Represents a single option within the segmented control. */
@Immutable
data class FinsibleSegmentedButtonOption(
    val id: String,
    val label: String,
    val icon: (@Composable (Modifier) -> Unit)? = null,
    val alignment: FinsibleSegmentAlignment = FinsibleSegmentAlignment.Center
)

@Immutable
enum class FinsibleSegmentAlignment { Start, Center, End }




