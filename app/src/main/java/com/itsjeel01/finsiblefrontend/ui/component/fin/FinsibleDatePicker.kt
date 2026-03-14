package com.itsjeel01.finsiblefrontend.ui.component.fin

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Themed date picker that applies Finsible brand colors by default. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinsibleDatePicker(
    state: DatePickerState,
    modifier: Modifier = Modifier,
    showModeToggle: Boolean = false,
    title: (@Composable () -> Unit)? = null,
    headline: (@Composable () -> Unit)? = null
) {
    DatePicker(
        state = state,
        modifier = modifier,
        showModeToggle = showModeToggle,
        title = title,
        headline = headline,
        colors = DatePickerDefaults.colors().copy(
            containerColor = FinsibleTheme.colors.surface,
            selectedDayContainerColor = FinsibleTheme.colors.brandAccent,
            todayContentColor = FinsibleTheme.colors.link,
            todayDateBorderColor = FinsibleTheme.colors.brandAccent,
            dayContentColor = FinsibleTheme.colors.primaryContent,
            weekdayContentColor = FinsibleTheme.colors.secondaryContent,
            currentYearContentColor = FinsibleTheme.colors.link,
            selectedYearContainerColor = FinsibleTheme.colors.brandAccent,
            yearContentColor = FinsibleTheme.colors.primaryContent
        )
    )
}
