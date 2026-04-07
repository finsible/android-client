package com.itsjeel01.finsiblefrontend.ui.component.templates.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

/** Immutable holder for dropdown styling. */
@Immutable
data class FinsibleDropdownColors(
    val containerColor: Color,
    val menuColor: Color,
    val borderColor: Color,
    val selectedOptionColor: Color,
    val selectedOptionTextColor: Color,
    val selectedIconTint: Color,
    val optionTextColor: Color,
    val placeholderColor: Color,
    val iconTint: Color,
    val disabledIconTint: Color,
    val rippleColor: Color
)

/** Immutable holder for dropdown sizing. */
@Immutable
data class FinsibleDropdownSizes(
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val textStyle: TextStyle,
    val placeholderStyle: TextStyle,
    val iconSize: Dp,
    val iconSpacing: Dp,
    val cornerRadius: Dp,
    val itemPadding: Dp,
    val borderWidth: Dp
)

/** Represents a single dropdown option. */
@Immutable
data class FinsibleDropdownOption(
    val id: String,
    val label: String,
    val icon: (@Composable () -> Unit)? = null
)
