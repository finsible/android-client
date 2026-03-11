package com.itsjeel01.finsiblefrontend.ui.component.historytab.filtersbottomsheet

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.FinsibleConstants
import com.itsjeel01.finsiblefrontend.common.FinsibleConstants.ALL_MONTHS
import com.itsjeel01.finsiblefrontend.common.toReadableDate
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.model.TimeFilterMode
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium

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
        SectionLabel(stringResource(R.string.time_period))

        Spacer(Modifier.height(FinsibleTheme.dimes.d8))

        // Radio row
        Row(
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d20),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TimeFilterMode.entries.forEach { mode ->
                RadioOption(
                    label = stringResource(mode.displayText),
                    selected = mode == timeMode,
                    onClick = { onTimeModeChange(mode) }
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
                if (mode != TimeFilterMode.ALL) Spacer(Modifier.height(FinsibleTheme.dimes.d8))

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
        // Month dropdown
        val allMonthsLabel = stringResource(R.string.all_months)
        val monthLabel = if (selectedMonth == ALL_MONTHS) allMonthsLabel else FinsibleConstants.MONTHS[selectedMonth]
        InlineDropdown(value = monthLabel, modifier = Modifier.weight(1.6f)) { closeMenu ->
            // "All months" entry — year-only filtering
            DropdownMenuItem(
                text = {
                    Row {
                        Text(
                            text = allMonthsLabel,
                            style = FinsibleTheme.typography.t14.medium(),
                            color = if (selectedMonth == ALL_MONTHS) FinsibleTheme.colors.link
                            else FinsibleTheme.colors.secondaryContent
                        )
                    }
                },
                onClick = { onMonthChange(ALL_MONTHS); closeMenu() },
                colors = MenuDefaults.itemColors(textColor = FinsibleTheme.colors.primaryContent)
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = FinsibleTheme.dimes.d4),
                color = FinsibleTheme.colors.divider,
                thickness = FinsibleTheme.dimes.d0dot5
            )
            FinsibleConstants.MONTHS.forEachIndexed { index, name ->
                val isDisabled = selectedYear == currentYear && index > currentMonth
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4)) {
                            SelectionDot(selectedMonth == index)
                            Text(
                                text = name,
                                style = FinsibleTheme.typography.t14.medium(),
                                color = when {
                                    isDisabled -> FinsibleTheme.colors.disabledContent
                                    index == selectedMonth -> FinsibleTheme.colors.primaryContent
                                    else -> FinsibleTheme.colors.secondaryContent
                                }
                            )
                        }
                    },
                    onClick = { if (!isDisabled) onMonthChange(index); closeMenu() },
                    enabled = !isDisabled,
                    colors = MenuDefaults.itemColors(
                        textColor = FinsibleTheme.colors.primaryContent,
                        disabledTextColor = FinsibleTheme.colors.disabledContent
                    )
                )
            }
        }

        // Year dropdown
        val years = remember(currentYear) { (currentYear downTo currentYear - 10).toList() }
        InlineDropdown(value = selectedYear.toString(), modifier = Modifier.weight(1f)) { closeMenu ->
            years.forEach { year ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4)) {
                            SelectionDot(selectedYear == year)
                            Text(
                                text = year.toString(),
                                style = FinsibleTheme.typography.t14.medium(),
                                color = if (year == selectedYear) FinsibleTheme.colors.primaryContent
                                else FinsibleTheme.colors.secondaryContent
                            )
                        }
                    },
                    onClick = { onYearChange(year); closeMenu() },
                    colors = MenuDefaults.itemColors(textColor = FinsibleTheme.colors.primaryContent)
                )
            }
        }
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(FinsibleTheme.dimes.d10))
            .background(FinsibleTheme.colors.surfaceContainerLow)
            .border(
                width = FinsibleTheme.dimes.d0dot5,
                color = if (hasValue) FinsibleTheme.colors.outline
                else FinsibleTheme.colors.outlineVariant,
                shape = RoundedCornerShape(FinsibleTheme.dimes.d10)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = FinsibleTheme.dimes.d14, vertical = FinsibleTheme.dimes.d12),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = FinsibleTheme.typography.t14.medium(),
            color = if (hasValue) FinsibleTheme.colors.primaryContent
            else FinsibleTheme.colors.placeholder,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(FinsibleTheme.dimes.d8))
        Icon(
            painter = painterResource(R.drawable.ic_calendar),
            contentDescription = null,
            tint = if (hasValue) FinsibleTheme.colors.primaryContent
            else FinsibleTheme.colors.tertiaryContent,
            modifier = Modifier.size(FinsibleTheme.dimes.d18)
        )
    }
}

/**
 * Reusable inline dropdown trigger + menu.
 * [content] receives a callback to close the menu.
 */
@Composable
private fun InlineDropdown(
    value: String,
    modifier: Modifier = Modifier,
    content: @Composable (closeMenu: () -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(200), label = "chevron_rot"
    )

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(FinsibleTheme.dimes.d10))
                .background(FinsibleTheme.colors.surfaceContainerLow)
                .border(
                    width = FinsibleTheme.dimes.d0dot5,
                    color = if (expanded) FinsibleTheme.colors.outline
                    else FinsibleTheme.colors.outlineVariant,
                    shape = RoundedCornerShape(FinsibleTheme.dimes.d10)
                )
                .clickable { expanded = true }
                .padding(horizontal = FinsibleTheme.dimes.d14, vertical = FinsibleTheme.dimes.d12),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = value,
                style = FinsibleTheme.typography.t14.medium(),
                color = FinsibleTheme.colors.primaryContent,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(FinsibleTheme.dimes.d6))
            Icon(
                painter = painterResource(com.composables.icons.lucide.R.drawable.lucide_ic_chevron_down),
                contentDescription = null,
                tint = FinsibleTheme.colors.secondaryContent,
                modifier = Modifier
                    .size(FinsibleTheme.dimes.d16)
                    .rotate(chevronRotation)
            )
        }

        if (expanded) {
            DropdownMenu(
                expanded = true,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(FinsibleTheme.colors.surfaceContainer)
                    .wrapContentHeight()
            ) {
                content { expanded = false }
            }
        }
    }
}
