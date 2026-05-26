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
import com.itsjeel01.finsiblefrontend.ui.di.hiltUserLocale
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleDatePickerDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleDropdownDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.CalendarConstraints
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerSizes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDropdownOption
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleMonthYear
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle

/**
 * A highly configurable semantic month/year picker component that adheres to Finsible Design System.
 * @param onMonthYearSelected Callback when a month/year is selected.
 * @param onDisplayYearChange Callback when the display year changes.
 * @param modifier Composable modifier.
 * @param selectedMonthYear The currently selected month/year.
 * @param constraints Shared superset config consumed by all temporal pickers.
 * @param size The size of the picker.
 * @param colors The resolved color styles for the date picker.
 * @param sizes The resolved size styles for the date picker.
 */
@Composable
fun FinsibleMonthYearPicker(
    onMonthYearSelected: (FinsibleMonthYear) -> Unit,
    onDisplayYearChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    selectedMonthYear: FinsibleMonthYear = FinsibleMonthYear.from(YearMonth.now()),
    constraints: CalendarConstraints = CalendarConstraints(
        displayYear = selectedMonthYear.year,
        yearRange =
            (selectedMonthYear.year - CalendarConstraints.DEFAULT_YEAR_WINDOW) ..
                    (selectedMonthYear.year + CalendarConstraints.DEFAULT_YEAR_WINDOW),
    ),
    size: FinsibleSize = FinsibleSize.Medium,
    colors: FinsibleDatePickerColors = FinsibleDatePickerDefaults.colors(),
    sizes: FinsibleDatePickerSizes = FinsibleDatePickerDefaults.sizes(),
) {
    constraints.validateSelection(selectedMonthYear)
    require(size in setOf(FinsibleSize.Small, FinsibleSize.Medium, FinsibleSize.Large)) {
        "FinsibleMonthYearPicker supports only Small, Medium, and Large sizes."
    }

    val resolvedLocale = constraints.resolveLocale(hiltUserLocale())

    val allMonthsLabel = stringResource(R.string.finsible_date_picker_all_months)
    val validMonthsForYear = remember(constraints.availableMonths, constraints.startMonth, constraints.endMonth, constraints.displayYear) {
        constraints.availableMonths.filter { month ->
            val ym = YearMonth.of(constraints.displayYear, month)
            !ym.isBefore(constraints.startMonth) && !ym.isAfter(constraints.endMonth)
        }
    }
    val monthOptions = remember(validMonthsForYear, constraints.includeAllMonthsOption, resolvedLocale, allMonthsLabel) {
        buildList {
            if (constraints.includeAllMonthsOption) {
                add(FinsibleDropdownOption(id = CalendarConstraints.ID_ALL_MONTHS, label = allMonthsLabel))
            }
            validMonthsForYear.forEach { month ->
                add(
                    FinsibleDropdownOption(
                        id = month.value.toString(),
                        label = month.getDisplayName(TextStyle.FULL, resolvedLocale),
                    ),
                )
            }
        }
    }
    val yearOptions = remember(constraints.yearRange) {
        constraints.yearRange.map { year ->
            FinsibleDropdownOption(id = year.toString(), label = year.toString())
        }
    }
    var isMonthExpanded by remember { mutableStateOf(false) }
    var isYearExpanded by remember { mutableStateOf(false) }
    val dropdownColors = FinsibleDropdownDefaults.colors(
        optionTextColor = colors.dayContentColor,
        selectedOptionTextColor = colors.dayContentColor,
        placeholderColor = colors.dayContentColor,
        iconTint = colors.dayContentColor,
        selectedIconTint = colors.dayContentColor,
    )
    val selectedMonthId = selectedMonthYear.month?.value?.toString() ?: CalendarConstraints.ID_ALL_MONTHS

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(sizes.monthYearPickerSpacing),
    ) {
        FinsibleDropdown(
            modifier = Modifier,
            options = monthOptions,
            selectedId = selectedMonthId,
            onSelected = { selectedId ->
                val month = if (selectedId == CalendarConstraints.ID_ALL_MONTHS) null else Month.of(selectedId.toInt())
                onMonthYearSelected(FinsibleMonthYear(constraints.displayYear, month))
                isMonthExpanded = false
            },
            placeholder = allMonthsLabel,
            expanded = isMonthExpanded,
            onExpandedChange = { isMonthExpanded = it },
            size = size,
            colors = dropdownColors,
            fullWidth = false,
        )

        FinsibleDropdown(
            modifier = Modifier,
            options = yearOptions,
            selectedId = constraints.displayYear.toString(),
            onSelected = { selectedId ->
                val newYear = selectedId.toInt()
                onDisplayYearChange(newYear)

                val newValidMonths = constraints.availableMonths.filter { m ->
                    val ym = YearMonth.of(newYear, m)
                    !ym.isBefore(constraints.startMonth) && !ym.isAfter(constraints.endMonth)
                }
                if (selectedMonthYear.month != null && selectedMonthYear.month !in newValidMonths) {
                    newValidMonths.firstOrNull()?.let { fallbackMonth ->
                        onMonthYearSelected(FinsibleMonthYear(newYear, fallbackMonth))
                    }
                }
                isYearExpanded = false
            },
            placeholder = constraints.displayYear.toString(),
            expanded = isYearExpanded,
            onExpandedChange = { isYearExpanded = it },
            size = size,
            colors = dropdownColors,
            fullWidth = false,
        )
    }
}

