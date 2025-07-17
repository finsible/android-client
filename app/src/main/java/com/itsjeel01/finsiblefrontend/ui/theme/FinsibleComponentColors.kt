package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun finsibleTextFieldColors(accentColor: Color): TextFieldColors {
    val disabledColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5F)
    val neutralColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5F)
    val errorColor = MaterialTheme.colorScheme.error
    val onSurface = MaterialTheme.colorScheme.onSurface
    val transparent = Color.Transparent

    return TextFieldDefaults.colors(
        disabledLabelColor = disabledColor,
        unfocusedLabelColor = neutralColor,
        focusedLabelColor = neutralColor,
        errorLabelColor = neutralColor,
        disabledIndicatorColor = disabledColor,
        unfocusedIndicatorColor = neutralColor,
        focusedIndicatorColor = accentColor,
        errorIndicatorColor = errorColor,
        disabledContainerColor = transparent,
        unfocusedContainerColor = transparent,
        focusedContainerColor = transparent,
        errorContainerColor = transparent,
        unfocusedTextColor = onSurface,
        focusedTextColor = onSurface,
        disabledTextColor = disabledColor,
        errorTextColor = onSurface,
        unfocusedSupportingTextColor = neutralColor,
        focusedSupportingTextColor = neutralColor,
        disabledSupportingTextColor = disabledColor,
        errorSupportingTextColor = errorColor,
        disabledPlaceholderColor = disabledColor,
        unfocusedPlaceholderColor = neutralColor,
        focusedPlaceholderColor = neutralColor,
        cursorColor = accentColor,
        selectionColors = TextSelectionColors(
            handleColor = accentColor,
            backgroundColor = accentColor.copy(alpha = 0.4F)
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun finsibleDatePickerColors(accentColor: Color): DatePickerColors {
    return DatePickerDefaults.colors().copy(
        containerColor = MaterialTheme.colorScheme.background,
        selectedDayContainerColor = accentColor.copy(alpha = 0.5F),
        selectedDayContentColor = MaterialTheme.colorScheme.onBackground,
        todayContentColor = accentColor,
        todayDateBorderColor = accentColor,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun finsibleDatePickerDialogColors(): DatePickerColors {
    return DatePickerDefaults.colors()
        .copy(containerColor = MaterialTheme.colorScheme.background)
}

@Composable
fun finsibleNavigationBarItemColors(): NavigationBarItemColors {
    return NavigationBarItemColors(
        selectedIndicatorColor = MaterialTheme.colorScheme.background,
        unselectedIconColor = MaterialTheme.colorScheme.outline,
        unselectedTextColor = MaterialTheme.colorScheme.outline,
        disabledIconColor = MaterialTheme.colorScheme.outline,
        disabledTextColor = MaterialTheme.colorScheme.outline,
        selectedIconColor = MaterialTheme.colorScheme.primary,
        selectedTextColor = MaterialTheme.colorScheme.primary
    )
}
