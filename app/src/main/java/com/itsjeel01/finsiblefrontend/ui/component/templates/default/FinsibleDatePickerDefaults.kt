package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerShapes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerSizes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerTypography
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

object FinsibleDatePickerDefaults {

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        monthHeaderColor: Color = Color.Unspecified,
        monthHeaderContentColor: Color = Color.Unspecified,
        weekdayContentColor: Color = Color.Unspecified,
        dayContainerColor: Color = Color.Unspecified,
        dayContentColor: Color = Color.Unspecified,
        outOfMonthDayContentColor: Color = Color.Unspecified,
        disabledDayContentColor: Color = Color.Unspecified,
        selectedDayContainerColor: Color = Color.Unspecified,
        selectedDayContentColor: Color = Color.Unspecified,
        inRangeDayContainerColor: Color = Color.Unspecified,
        inRangeDayContentColor: Color = Color.Unspecified,
        todayBorderColor: Color = Color.Unspecified,
        disabledMonthOpacity: Float = Float.NaN,
    ): FinsibleDatePickerColors {
        val theme = FinsibleTheme.colors

        return FinsibleDatePickerColors(
            containerColor = if (containerColor != Color.Unspecified) containerColor else theme.surfaceContainer,
            monthHeaderColor = if (monthHeaderColor != Color.Unspecified) monthHeaderColor else theme.surfaceContainerLow,
            monthHeaderContentColor = if (monthHeaderContentColor != Color.Unspecified) monthHeaderContentColor else theme.primaryContent,
            weekdayContentColor = if (weekdayContentColor != Color.Unspecified) weekdayContentColor else theme.secondaryContent,
            dayContainerColor = if (dayContainerColor != Color.Unspecified) dayContainerColor else theme.transparent,
            dayContentColor = if (dayContentColor != Color.Unspecified) dayContentColor else theme.primaryContent,
            outOfMonthDayContentColor = if (outOfMonthDayContentColor != Color.Unspecified) outOfMonthDayContentColor else theme.tertiaryContent,
            disabledDayContentColor = if (disabledDayContentColor != Color.Unspecified) disabledDayContentColor else theme.disabledContent,
            selectedDayContainerColor = if (selectedDayContainerColor != Color.Unspecified) selectedDayContainerColor else theme.primaryContent,
            selectedDayContentColor = if (selectedDayContentColor != Color.Unspecified) selectedDayContentColor else theme.same,
            inRangeDayContainerColor = if (inRangeDayContainerColor != Color.Unspecified) inRangeDayContainerColor else theme.surfaceContainerHighest,
            inRangeDayContentColor = if (inRangeDayContentColor != Color.Unspecified) inRangeDayContentColor else theme.primaryContent,
            todayBorderColor = if (todayBorderColor != Color.Unspecified) todayBorderColor else theme.primaryContent,
            disabledMonthOpacity = if (!disabledMonthOpacity.isNaN()) disabledMonthOpacity else 0.62f,
        )
    }

    @Composable
    fun sizes(): FinsibleDatePickerSizes {
        val d = FinsibleTheme.dimes
        return FinsibleDatePickerSizes(
            containerElevation = d.d8,
            containerCornerRadius = d.d16,
            contentPadding = d.d8,
            monthHeaderVerticalPadding = d.d8,
            monthHeaderCornerRadius = d.d12,
            weekdayHeaderBottomPadding = d.d0,
            contentVerticalSpacing = d.d4,
            monthGridHorizontalSpacing = d.d8,
            daysVerticalSpacing = d.d12,
            dayCellSize = d.d48,
            dayCellInnerSpacing = d.d4,
            dayCellCornerRadius = d.d12,
            monthGridItemCornerRadius = d.d12,
            monthYearPickerSpacing = d.d8,
            todayBorderWidth = d.d1,
        )
    }

    fun typography(
        weekdayTextVariant: FinsibleTextVariant = FinsibleTextVariant.SmallLabelRegular,
        daySelectedTextVariant: FinsibleTextVariant = FinsibleTextVariant.SmallBodyBold,
        dayDefaultTextVariant: FinsibleTextVariant = FinsibleTextVariant.SmallBodyRegular,
        monthGridYearTextVariant: FinsibleTextVariant = FinsibleTextVariant.SmallBodyBold,
        monthGridItemTextVariant: FinsibleTextVariant = FinsibleTextVariant.SmallBodyRegular,
    ): FinsibleDatePickerTypography {
        return FinsibleDatePickerTypography(
            weekdayTextVariant = weekdayTextVariant,
            daySelectedTextVariant = daySelectedTextVariant,
            dayDefaultTextVariant = dayDefaultTextVariant,
            monthGridYearTextVariant = monthGridYearTextVariant,
            monthGridItemTextVariant = monthGridItemTextVariant,
        )
    }

    fun shapes(
        navigationIconButtonShape: FinsibleShape = FinsibleShape.Circle,
        dayShape: FinsibleShape = FinsibleShape.Circle,
        monthGridItemShape: FinsibleShape = FinsibleShape.Rounded,
    ): FinsibleDatePickerShapes {
        return FinsibleDatePickerShapes(
            navigationIconButtonShape = navigationIconButtonShape,
            dayShape = dayShape,
            monthGridItemShape = monthGridItemShape,
        )
    }
}