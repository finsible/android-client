package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerShapes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerSizes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerTypography
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold

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
        val s = FinsibleTheme.colors

        return FinsibleDatePickerColors(
            containerColor = if (containerColor != Color.Unspecified) containerColor else s.surfaceSunken,
            monthHeaderColor = if (monthHeaderColor != Color.Unspecified) monthHeaderColor else s.surfaceDefault,
            monthHeaderContentColor = if (monthHeaderContentColor != Color.Unspecified) monthHeaderContentColor else s.contentPrimary,
            weekdayContentColor = if (weekdayContentColor != Color.Unspecified) weekdayContentColor else s.contentSecondary,
            dayContainerColor = if (dayContainerColor != Color.Unspecified) dayContainerColor else Color.Transparent,
            dayContentColor = if (dayContentColor != Color.Unspecified) dayContentColor else s.contentPrimary,
            outOfMonthDayContentColor = if (outOfMonthDayContentColor != Color.Unspecified) outOfMonthDayContentColor else s.contentTertiary,
            disabledDayContentColor = if (disabledDayContentColor != Color.Unspecified) disabledDayContentColor else s.contentDisabled,
            selectedDayContainerColor = if (selectedDayContainerColor != Color.Unspecified) selectedDayContainerColor else s.contentPrimary,
            selectedDayContentColor = if (selectedDayContentColor != Color.Unspecified) selectedDayContentColor else s.contentInverse,
            inRangeDayContainerColor = if (inRangeDayContainerColor != Color.Unspecified) inRangeDayContainerColor else s.surfaceRaised,
            inRangeDayContentColor = if (inRangeDayContentColor != Color.Unspecified) inRangeDayContentColor else s.contentPrimary,
            todayBorderColor = if (todayBorderColor != Color.Unspecified) todayBorderColor else s.contentPrimary,
            disabledMonthOpacity = if (!disabledMonthOpacity.isNaN()) disabledMonthOpacity else 0.62f,
        )
    }

    @Composable
    fun sizes(): FinsibleDatePickerSizes {
        val sp = FinsibleTheme.spacing
        return FinsibleDatePickerSizes(
            containerElevation = sp.inlineMd,
            containerCornerRadius = FinsibleTheme.radius.lg,
            contentPadding = sp.inlineMd,
            monthHeaderVerticalPadding = sp.inlineMd,
            monthHeaderCornerRadius = FinsibleTheme.radius.md,
            weekdayHeaderBottomPadding = FinsibleTheme.radius.none,
            contentVerticalSpacing = sp.insetXs,
            monthGridHorizontalSpacing = sp.inlineMd,
            daysVerticalSpacing = sp.gapMd,
            dayCellSize = FinsibleTheme.sizes.touch.md,
            dayCellInnerSpacing = sp.insetXs,
            dayCellCornerRadius = FinsibleTheme.radius.md,
            monthGridItemCornerRadius = FinsibleTheme.radius.md,
            monthYearPickerSpacing = sp.inlineMd,
            todayBorderWidth = FinsibleTheme.stroke.thin,
        )
    }

    @Composable
    fun typography(
        weekdayTextStyle: TextStyle = FinsibleTheme.typography.bodySm,
        daySelectedTextStyle: TextStyle = FinsibleTheme.typography.bodyMd.bold(),
        dayDefaultTextStyle: TextStyle = FinsibleTheme.typography.bodyMd,
        monthGridYearTextStyle: TextStyle = FinsibleTheme.typography.bodyMd.bold(),
        monthGridItemTextStyle: TextStyle = FinsibleTheme.typography.bodyMd,
    ): FinsibleDatePickerTypography {
        return FinsibleDatePickerTypography(
            weekdayTextStyle = weekdayTextStyle,
            daySelectedTextStyle = daySelectedTextStyle,
            dayDefaultTextStyle = dayDefaultTextStyle,
            monthGridYearTextStyle = monthGridYearTextStyle,
            monthGridItemTextStyle = monthGridItemTextStyle,
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
