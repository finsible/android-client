package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.data.di.hiltUserLocale
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleDatePickerDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerSizes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleMonthYear
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

/**
 * A highly configurable semantic month/year picker component that adheres to Finsible Design System.
 * @param onMonthYearSelected Callback when a month/year is selected.
 * @param onDisplayYearChange Callback when the display year changes.
 * @param modifier Composable modifier.
 * @param selectedMonthYear The currently selected month/year.
 * @param displayYear The currently displayed year.
 * @param yearRange The range of years that can be displayed.
 * @param size The size of the picker.
 * @param locale The locale to use for formatting.
 * @param colors The resolved color styles for the date picker.
 * @param sizes The resolved size styles for the date picker.
 */
@Composable
fun FinsibleMonthYearPicker(
    onMonthYearSelected: (FinsibleMonthYear) -> Unit,
    onDisplayYearChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    selectedMonthYear: FinsibleMonthYear = FinsibleMonthYear.from(YearMonth.now()),
    displayYear: Int = selectedMonthYear.year,
    yearRange: IntRange = (displayYear - DEFAULT_YEAR_WINDOW) .. (displayYear + DEFAULT_YEAR_WINDOW),
    size: FinsibleSize = FinsibleSize.Medium,
    locale: Locale? = null,
    colors: FinsibleDatePickerColors = FinsibleDatePickerDefaults.colors(),
    sizes: FinsibleDatePickerSizes = FinsibleDatePickerDefaults.sizes(),
) {
    require(!yearRange.isEmpty()) { "yearRange must not be empty." }
    require(displayYear in yearRange) { "displayYear must be inside yearRange." }
    require(selectedMonthYear.year in yearRange) { "selectedMonthYear.year must be inside yearRange." }
    require(size in SUPPORTED_MONTH_YEAR_PICKER_SIZES) {
        "FinsibleMonthYearPicker supports only Small, Medium, and Large sizes."
    }
    val resolvedLocale = locale ?: hiltUserLocale()

    val allMonthsLabel = stringResource(R.string.finsible_date_picker_all_months)
    val monthOptions = remember { listOf<Month?>(null) + Month.entries }
    val yearOptions = remember(yearRange) { yearRange.toList() }
    var isMonthExpanded by remember { mutableStateOf(false) }
    var isYearExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(sizes.monthYearPickerSpacing),
    ) {
        FinsibleDropdownPicker(
            modifier = Modifier,
            value = selectedMonthYear.month?.getDisplayName(TextStyle.FULL, resolvedLocale) ?: allMonthsLabel,
            expanded = isMonthExpanded,
            onExpandedChange = { isMonthExpanded = it },
            options = monthOptions,
            optionLabel = { month -> month?.getDisplayName(TextStyle.FULL, resolvedLocale) ?: allMonthsLabel },
            isOptionSelected = { option -> option == selectedMonthYear.month },
            onOptionSelected = { month ->
                onMonthYearSelected(FinsibleMonthYear(displayYear, month))
                isMonthExpanded = false
            },
            size = size,
            menuItemTextColor = colors.dayContentColor,
        )

        FinsibleDropdownPicker(
            modifier = Modifier,
            value = displayYear.toString(),
            expanded = isYearExpanded,
            onExpandedChange = { isYearExpanded = it },
            options = yearOptions,
            optionLabel = { year -> year.toString() },
            isOptionSelected = { option -> option == displayYear },
            onOptionSelected = { year ->
                onDisplayYearChange(year)
                isYearExpanded = false
            },
            size = size,
            menuItemTextColor = colors.dayContentColor,
        )
    }
}

private val SUPPORTED_MONTH_YEAR_PICKER_SIZES = setOf(
    FinsibleSize.Small,
    FinsibleSize.Medium,
    FinsibleSize.Large,
)