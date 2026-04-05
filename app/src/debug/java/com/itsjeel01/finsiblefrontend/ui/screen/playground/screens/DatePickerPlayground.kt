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
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleDatePicker
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleMonthYearPicker
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.CalendarConstraints
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleMonthYear
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

@Composable
fun DatePickerPlayground() {
    var selectedDate by rememberSaveable { mutableStateOf<LocalDate?>(LocalDate.now()) }
    var startBoundary by rememberSaveable(stateSaver = finsibleMonthYearSaver) {
        mutableStateOf(FinsibleMonthYear.from(YearMonth.now().minusMonths(6)))
    }
    var endBoundary by rememberSaveable(stateSaver = finsibleMonthYearSaver) {
        mutableStateOf(FinsibleMonthYear.from(YearMonth.now().plusMonths(6)))
    }

    val startMonth = startBoundary.toYearMonthOrNull() ?: YearMonth.of(startBoundary.year, Month.JANUARY)
    val endMonth = endBoundary.toYearMonthOrNull() ?: YearMonth.of(endBoundary.year, Month.DECEMBER)
    val boundaryYearRange = (YearMonth.now().year - 5) .. (YearMonth.now().year + 5)

    val calendarConfig = remember(startMonth, endMonth) {
        CalendarConstraints(
            startMonth = startMonth,
            endMonth = endMonth,
        )
    }
    val startBoundaryConfig = remember(startBoundary.year, boundaryYearRange) {
        CalendarConstraints(
            displayYear = startBoundary.year,
            yearRange = boundaryYearRange,
            includeAllMonthsOption = false,
        )
    }
    val endBoundaryConfig = remember(endBoundary.year, boundaryYearRange) {
        CalendarConstraints(
            displayYear = endBoundary.year,
            yearRange = boundaryYearRange,
            includeAllMonthsOption = false,
        )
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = FinsibleTheme.dimes.d16, vertical = FinsibleTheme.dimes.d12),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12)
    ) {
        FinsibleDatePicker(
            onDateSelected = { selectedDate = it },
            selectedDate = selectedDate,
            constraints = calendarConfig,
        )

        FinsibleText(
            text = stringResource(R.string.component_playground_date_start_boundary),
            variant = FinsibleTextVariant.SmallLabelRegular,
            color = FinsibleTheme.colors.secondaryContent,
        )

        FinsibleMonthYearPicker(
            onMonthYearSelected = { monthYear ->
                val requestedStart = monthYear.toYearMonthOrNull() ?: return@FinsibleMonthYearPicker
                startBoundary = monthYear
                if (requestedStart.isAfter(endMonth)) {
                    endBoundary = FinsibleMonthYear.from(requestedStart)
                }
            },
            onDisplayYearChange = { year ->
                val newStart = startBoundary.copy(year = year)
                startBoundary = newStart
                val nextStart = newStart.toYearMonthOrNull() ?: return@FinsibleMonthYearPicker
                if (nextStart.isAfter(endMonth)) {
                    endBoundary = FinsibleMonthYear.from(nextStart)
                }
            },
            selectedMonthYear = startBoundary,
            constraints = startBoundaryConfig,
        )

        FinsibleText(
            text = stringResource(R.string.component_playground_date_end_boundary),
            variant = FinsibleTextVariant.SmallLabelRegular,
            color = FinsibleTheme.colors.secondaryContent,
        )

        FinsibleMonthYearPicker(
            onMonthYearSelected = { monthYear ->
                val requestedEnd = monthYear.toYearMonthOrNull() ?: return@FinsibleMonthYearPicker
                endBoundary = monthYear
                if (requestedEnd.isBefore(startMonth)) {
                    startBoundary = FinsibleMonthYear.from(requestedEnd)
                }
            },
            onDisplayYearChange = { year ->
                val newEnd = endBoundary.copy(year = year)
                endBoundary = newEnd
                val nextEnd = newEnd.toYearMonthOrNull() ?: return@FinsibleMonthYearPicker
                if (nextEnd.isBefore(startMonth)) {
                    startBoundary = FinsibleMonthYear.from(nextEnd)
                }
            },
            selectedMonthYear = endBoundary,
            constraints = endBoundaryConfig,
        )
    }
}
