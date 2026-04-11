package com.itsjeel01.finsiblefrontend.ui.component.historytab.filtersbottomsheet

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.FinsibleConstants
import com.itsjeel01.finsiblefrontend.common.FinsibleConstants.ALL_MONTHS
import com.itsjeel01.finsiblefrontend.common.toReadableDate
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleDropdown
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleRadioButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleTextField
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleDropdownDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleRadioButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleTextFieldDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDropdownOption
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.model.TimeFilterMode
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun TimeBasedFilter(
    timeMode: TimeFilterMode,
    onTimeModeChange: (TimeFilterMode) -> Unit,
    selectedMonth: Int,
    selectedYear: Int,
    currentMonth: Int,
    currentYear: Int,
    onMonthChange: (Int) -> Unit,
    onYearChange: (Int) -> Unit,
    customRangeStart: Long?,
    customRangeEnd: Long?,
    onCustomRangeClick: () -> Unit
) {
    Column {
        FinsibleText(
            text = stringResource(R.string.time_period),
            variant = FinsibleTextVariant.MicroLabelSemiBold,
            colorVariant = FinsibleTextColorVariant.Secondary,
            uppercase = true
        )

        Spacer(Modifier.height(FilterSheetSpacing.sectionHeaderGap))

        // Radio row
        Row(
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TimeFilterMode.entries.forEach { mode ->
                FinsibleRadioButton(
                    label = stringResource(mode.displayText),
                    selected = mode == timeMode,
                    onSelectedChange = { onTimeModeChange(mode) },
                    size = FinsibleSize.Small,
                    colors = FinsibleRadioButtonDefaults.colors()
                )
            }
        }

        // Inline picker — collapses entirely for ALL, swaps for others
        AnimatedContent(
            targetState = timeMode,
            transitionSpec = { fadeIn(tween(Duration.MS_150.toInt())) togetherWith fadeOut(tween(Duration.MS_100.toInt())) },
            label = "time_picker_swap"
        ) { mode ->

            Column {
                if (mode != TimeFilterMode.ALL) Spacer(Modifier.height(FinsibleTheme.dimes.d12))

                when (mode) {
                    TimeFilterMode.ALL -> Unit  // no picker — null range means all transactions
                    TimeFilterMode.MONTH_YEAR -> MonthYearRow(
                        selectedMonth = selectedMonth,
                        selectedYear = selectedYear,
                        currentMonth = currentMonth,
                        currentYear = currentYear,
                        onMonthChange = onMonthChange,
                        onYearChange = onYearChange
                    )

                    TimeFilterMode.CUSTOM -> CustomRangeField(
                        rangeStart = customRangeStart,
                        rangeEnd = customRangeEnd,
                        onClick = onCustomRangeClick
                    )
                }
            }
        }
    }
}

/** Dropdown that shows month name + year dropdowns side-by-side. */
@Composable
private fun MonthYearRow(
    selectedMonth: Int,
    selectedYear: Int,
    currentMonth: Int,
    currentYear: Int,
    onMonthChange: (Int) -> Unit,
    onYearChange: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d10),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val allMonthsLabel = stringResource(R.string.all_months)
        val monthLabel = if (selectedMonth == ALL_MONTHS) allMonthsLabel else FinsibleConstants.MONTHS[selectedMonth]
        val monthOptions = buildList {
            add(FinsibleDropdownOption(id = ALL_MONTHS.toString(), label = allMonthsLabel))
            FinsibleConstants.MONTHS.forEachIndexed { index, name ->
                val isDisabled = selectedYear == currentYear && index > currentMonth
                add(
                    FinsibleDropdownOption(
                        id = index.toString(),
                        label = name,
                        enabled = !isDisabled
                    )
                )
            }
        }

        FinsibleDropdown(
            options = monthOptions,
            selectedId = selectedMonth.toString(),
            onSelected = { onMonthChange(it.toInt()) },
            placeholder = monthLabel,
            modifier = Modifier.weight(1.6f),
            size = FinsibleSize.Small,
            shapeVariant = FinsibleShape.Rounded,
            colors = FinsibleDropdownDefaults.colors(
                containerColor = FinsibleTheme.colors.surfaceContainerLow,
                menuColor = FinsibleTheme.colors.surfaceContainer,
                borderColor = FinsibleTheme.colors.outlineVariant,
                selectedOptionColor = FinsibleTheme.colors.surfaceContainerHigh,
                selectedOptionTextColor = FinsibleTheme.colors.primaryContent,
                optionTextColor = FinsibleTheme.colors.secondaryContent,
                placeholderColor = FinsibleTheme.colors.primaryContent,
                iconTint = FinsibleTheme.colors.secondaryContent,
                selectedIconTint = FinsibleTheme.colors.secondaryContent,
                disabledIconTint = FinsibleTheme.colors.disabledContent
            )
        )

        val years = remember(currentYear) { (currentYear downTo currentYear - 10).toList() }
        FinsibleDropdown(
            options = years.map { FinsibleDropdownOption(id = it.toString(), label = it.toString()) },
            selectedId = selectedYear.toString(),
            onSelected = { onYearChange(it.toInt()) },
            placeholder = selectedYear.toString(),
            modifier = Modifier.weight(1f),
            size = FinsibleSize.Small,
            shapeVariant = FinsibleShape.Rounded,
            colors = FinsibleDropdownDefaults.colors(
                containerColor = FinsibleTheme.colors.surfaceContainerLow,
                menuColor = FinsibleTheme.colors.surfaceContainer,
                borderColor = FinsibleTheme.colors.outlineVariant,
                selectedOptionColor = FinsibleTheme.colors.surfaceContainerHigh,
                selectedOptionTextColor = FinsibleTheme.colors.primaryContent,
                optionTextColor = FinsibleTheme.colors.secondaryContent,
                placeholderColor = FinsibleTheme.colors.primaryContent,
                iconTint = FinsibleTheme.colors.secondaryContent,
                selectedIconTint = FinsibleTheme.colors.secondaryContent,
                disabledIconTint = FinsibleTheme.colors.disabledContent
            )
        )
    }
}

/**
 * A tap-to-open field showing the resolved range label.
 * Opens the M3 DateRangePicker dialog via [onClick].
 */
@Composable
private fun CustomRangeField(
    rangeStart: Long?,
    rangeEnd: Long?,
    onClick: () -> Unit
) {
    val label = when {
        rangeStart != null && rangeEnd != null ->
            stringResource(R.string.date_range_full, rangeStart.toReadableDate(), rangeEnd.toReadableDate())

        rangeStart != null -> stringResource(R.string.date_range_from, rangeStart.toReadableDate())
        else -> stringResource(R.string.select_date_range)
    }
    val hasValue = rangeStart != null || rangeEnd != null
    val borderColor = if (hasValue) FinsibleTheme.colors.outline else FinsibleTheme.colors.outlineVariant

    FinsibleTextField(
        value = if (hasValue) label else "",
        onValueChange = {},
        placeholder = label,
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        size = FinsibleSize.Small,
        shapeVariant = FinsibleShape.Rounded,
        colors = FinsibleTextFieldDefaults.colors(
            containerColor = FinsibleTheme.colors.surfaceContainerLow,
            contentColor = FinsibleTheme.colors.primaryContent,
            placeholderColor = FinsibleTheme.colors.placeholder,
            borderColor = borderColor,
            focusedBorderColor = borderColor,
            iconTint = if (hasValue) FinsibleTheme.colors.primaryContent else FinsibleTheme.colors.tertiaryContent,
            disabledIconTint = FinsibleTheme.colors.tertiaryContent
        ),
        trailingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_calendar),
                contentDescription = null,
                modifier = Modifier.size(FinsibleTheme.dimes.d18)
            )
        }
    )
}
