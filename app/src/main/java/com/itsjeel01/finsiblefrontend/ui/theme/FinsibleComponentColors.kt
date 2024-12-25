package com.itsjeel01.finsiblefrontend.ui.theme

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
    return TextFieldDefaults.colors().copy(
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
        focusedContainerColor = MaterialTheme.colorScheme.background,
        focusedLabelColor = accentColor,
        cursorColor = accentColor,
        focusedIndicatorColor = accentColor.copy(alpha = 0.5F)
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