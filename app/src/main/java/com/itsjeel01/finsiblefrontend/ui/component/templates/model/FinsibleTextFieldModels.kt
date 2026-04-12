package com.itsjeel01.finsiblefrontend.ui.component.templates.model

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

/** Immutable holder for text field colors. */
@Immutable
data class FinsibleTextFieldColors(
    val containerColor: Color,
    val contentColor: Color,
    val placeholderColor: Color,
    val borderColor: Color,
    val focusedBorderColor: Color,
    val errorBorderColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val supportingTextColor: Color,
    val errorTextColor: Color,
    val iconTint: Color,
    val disabledIconTint: Color,
    val rippleColor: Color
)

/** Immutable holder for text field sizing. */
@Immutable
data class FinsibleTextFieldSizes(
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val textStyle: TextStyle,
    val placeholderStyle: TextStyle,
    val supportingTextStyle: TextStyle,
    val iconSize: Dp,
    val cornerRadius: Dp
)

/** Input restrictions supported by the text field. */
@Immutable
data class FinsibleTextFieldInputConfig(
    val keyboardOptions: KeyboardOptions,
    val maxLength: Int?
)


