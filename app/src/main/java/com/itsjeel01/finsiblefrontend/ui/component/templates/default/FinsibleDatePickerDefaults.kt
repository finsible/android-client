package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerSizes
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

object FinsibleDatePickerDefaults {

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        monthHeaderColor: Color = Color.Unspecified,
        monthHeaderContentColor: Color = Color.Unspecified,
        weekdayContentColor: Color = Color.Unspecified,
        dayContentColor: Color = Color.Unspecified,
        outOfMonthDayContentColor: Color = Color.Unspecified,
        disabledDayContentColor: Color = Color.Unspecified,
        selectedDayContainerColor: Color = Color.Unspecified,
        selectedDayContentColor: Color = Color.Unspecified,
        inRangeDayContainerColor: Color = Color.Unspecified,
        inRangeDayContentColor: Color = Color.Unspecified,
        todayBorderColor: Color = Color.Unspecified,
    ): FinsibleDatePickerColors {
        val theme = FinsibleTheme.colors

        return FinsibleDatePickerColors(
            containerColor = if (containerColor != Color.Unspecified) containerColor else theme.surfaceContainer,
            monthHeaderColor = if (monthHeaderColor != Color.Unspecified) monthHeaderColor else theme.surfaceContainerLow,
            monthHeaderContentColor = if (monthHeaderContentColor != Color.Unspecified) monthHeaderContentColor else theme.primaryContent,
            weekdayContentColor = if (weekdayContentColor != Color.Unspecified) weekdayContentColor else theme.secondaryContent,
            dayContentColor = if (dayContentColor != Color.Unspecified) dayContentColor else theme.primaryContent,
            outOfMonthDayContentColor = if (outOfMonthDayContentColor != Color.Unspecified) outOfMonthDayContentColor else theme.tertiaryContent,
            disabledDayContentColor = if (disabledDayContentColor != Color.Unspecified) disabledDayContentColor else theme.disabledContent,
            selectedDayContainerColor = if (selectedDayContainerColor != Color.Unspecified) selectedDayContainerColor else theme.primaryContent,
            selectedDayContentColor = if (selectedDayContentColor != Color.Unspecified) selectedDayContentColor else theme.same,
            inRangeDayContainerColor = if (inRangeDayContainerColor != Color.Unspecified) inRangeDayContainerColor else theme.surfaceContainerHighest,
            inRangeDayContentColor = if (inRangeDayContentColor != Color.Unspecified) inRangeDayContentColor else theme.primaryContent,
            todayBorderColor = if (todayBorderColor != Color.Unspecified) todayBorderColor else theme.primaryContent
        )
    }

    @Composable
    fun sizes(): FinsibleDatePickerSizes {
        val d = FinsibleTheme.dimes
        return FinsibleDatePickerSizes(
            containerCornerRadius = d.d16,
            contentPadding = d.d12,
            monthHeaderVerticalPadding = d.d8,
            weekdayHeaderBottomPadding = d.d2,
            contentVerticalSpacing = d.d8,
            monthGridHorizontalSpacing = d.d8,
            daysVerticalSpacing = d.d12,
            dayCellSize = d.d40,
            dayCellCornerRadius = d.d12,
            monthYearPickerSpacing = d.d8,
        )
    }
}