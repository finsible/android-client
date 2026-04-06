package com.itsjeel01.finsiblefrontend.ui.screen.playground.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleMonthYearPicker
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.CalendarConstraints
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleMonthYear
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.screen.playground.OptionDropdown
import com.itsjeel01.finsiblefrontend.ui.screen.playground.OptionToggle
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import java.time.Month
import java.time.YearMonth

@Composable
fun MonthYearPickerPlayground() {
    val nowMonthYear = FinsibleMonthYear.from(YearMonth.now())
    var selected by rememberSaveable(stateSaver = finsibleMonthYearSaver) { mutableStateOf(nowMonthYear) }
    var displayYear by rememberSaveable { mutableStateOf(nowMonthYear.year) }
    var useQuarterMonthsOnly by rememberSaveable { mutableStateOf(false) }
    var includeAllMonthsOption by rememberSaveable { mutableStateOf(true) }
    var useCompactYearRange by rememberSaveable { mutableStateOf(true) }
    var pickerSize by rememberSaveable { mutableStateOf(FinsibleSize.Medium) }

    val yearSpan = if (useCompactYearRange) 2 else 10
    val yearRange = (displayYear - yearSpan) .. (displayYear + yearSpan)
    val availableMonths = if (useQuarterMonthsOnly) {
        listOf(Month.JANUARY, Month.APRIL, Month.JULY, Month.OCTOBER)
    } else {
        Month.entries
    }
    val resolvedSelected = when {
        selected.month in availableMonths -> selected
        includeAllMonthsOption -> selected.copy(month = null)
        else -> selected.copy(month = availableMonths.first())
    }
    val monthYearConfig = remember(
        displayYear,
        useQuarterMonthsOnly,
        includeAllMonthsOption,
        useCompactYearRange,
    ) {
        CalendarConstraints(
            displayYear = displayYear,
            yearRange = yearRange,
            availableMonths = availableMonths,
            includeAllMonthsOption = includeAllMonthsOption,
        )
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = FinsibleTheme.dimes.d16, vertical = FinsibleTheme.dimes.d12),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12)
    ) {
        FinsibleMonthYearPicker(
            onMonthYearSelected = { selected = it },
            onDisplayYearChange = { displayYear = it },
            selectedMonthYear = resolvedSelected,
            constraints = monthYearConfig,
            size = pickerSize,
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_generic_size),
            selectedLabel = sizeLabel(pickerSize),
            options = listOf(FinsibleSize.Small, FinsibleSize.Medium, FinsibleSize.Large),
            optionLabel = { sizeLabel(it) },
            onSelect = { pickerSize = it },
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_monthyear_quarter_months_only),
            checked = useQuarterMonthsOnly,
            onCheckedChange = {
                useQuarterMonthsOnly = it
                val nextAvailableMonths = if (it) {
                    listOf(Month.JANUARY, Month.APRIL, Month.JULY, Month.OCTOBER)
                } else {
                    Month.entries
                }
                selected = when {
                    selected.month in nextAvailableMonths -> selected
                    includeAllMonthsOption -> selected.copy(month = null)
                    else -> selected.copy(month = nextAvailableMonths.first())
                }
            },
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_monthyear_include_all_months),
            checked = includeAllMonthsOption,
            onCheckedChange = {
                includeAllMonthsOption = it
                if (!it && selected.month == null) {
                    selected = selected.copy(month = availableMonths.first())
                }
            },
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_monthyear_compact_year_range),
            checked = useCompactYearRange,
            onCheckedChange = { useCompactYearRange = it },
        )

        FinsibleText(
            text = stringResource(
                R.string.component_playground_monthyear_label,
                resolvedSelected.month?.name ?: stringResource(R.string.component_playground_monthyear_all_months),
                resolvedSelected.year
            ),
            variant = FinsibleTextVariant.SmallLabelRegular,
            color = FinsibleTheme.colors.secondaryContent
        )
    }
}
