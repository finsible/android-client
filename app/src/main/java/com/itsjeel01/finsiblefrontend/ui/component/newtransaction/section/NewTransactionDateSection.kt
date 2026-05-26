package com.itsjeel01.finsiblefrontend.ui.component.newtransaction.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.TransactionRecurringFrequency
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.NewTransactionSectionDefaults
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.NewTransactionSectionLabel
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleChipsRow
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleFilterChip
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleToggle
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleIconPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleToggleDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleToggleArrangement
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleToggleLabelPosition
import com.itsjeel01.finsiblefrontend.ui.model.event.NewTransactionUiEvent
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionDateSelection
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionFormState
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionSheetMode
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionValidationError
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium
import com.itsjeel01.finsiblefrontend.ui.util.DateUtils
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun NewTransactionDateSection(
    state: NewTransactionFormState,
    onEvent: (NewTransactionUiEvent) -> Unit,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val hasError = state.validationErrors.contains(NewTransactionValidationError.DATE)
    val recurringFrequencies = remember { TransactionRecurringFrequency.toOrderedList() }

    val zoneId = remember { ZoneId.systemDefault() }
    val today = remember(zoneId) { LocalDate.now(zoneId) }
    val yesterday = remember(today) { today.minusDays(1) }

    val selectedLocalDate = remember(state.dateMillis, zoneId) {
        state.dateMillis?.let { Instant.ofEpochMilli(it).atZone(zoneId).toLocalDate() }
    }

    val isTodaySelected = state.dateSelection == NewTransactionDateSelection.TODAY ||
            (state.dateSelection == NewTransactionDateSelection.CUSTOM && selectedLocalDate == today)

    val isYesterdaySelected = state.dateSelection == NewTransactionDateSelection.YESTERDAY ||
            (state.dateSelection == NewTransactionDateSelection.CUSTOM && selectedLocalDate == yesterday)

    val isCustomDateSelected = state.dateSelection == NewTransactionDateSelection.CUSTOM &&
            state.dateMillis != null &&
            !isTodaySelected &&
            !isYesterdaySelected

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(NewTransactionSectionDefaults.headerSpacing)
    ) {
        NewTransactionSectionLabel(
            label = "Date",
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(FinsibleTheme.sizes.icon.xs),
                    painter = painterResource(id = com.composables.icons.lucide.R.drawable.lucide_ic_calendar),
                    contentDescription = null,
                    tint = FinsibleTheme.colors.contentSecondary
                )
            }
        )

        Column(verticalArrangement = Arrangement.spacedBy(NewTransactionSectionDefaults.contentSpacing)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.gapMd)) {
                DateQuickChip(
                    label = "Today",
                    selected = isTodaySelected,
                    selectedTint = accentColor,
                    onClick = { onEvent(NewTransactionUiEvent.DateQuickSelectionChanged(NewTransactionDateSelection.TODAY)) }
                )
                DateQuickChip(
                    label = "Yesterday",
                    selected = isYesterdaySelected,
                    selectedTint = accentColor,
                    onClick = { onEvent(NewTransactionUiEvent.DateQuickSelectionChanged(NewTransactionDateSelection.YESTERDAY)) }
                )
                DateQuickChip(
                    label = if (isCustomDateSelected) DateUtils.readableDate(state.dateMillis) else "Pick date",
                    selected = isCustomDateSelected,
                    selectedTint = accentColor,
                    onClick = { onEvent(NewTransactionUiEvent.SheetModeChanged(NewTransactionSheetMode.DATE_PICKER)) },
                    iconPosition = if (isCustomDateSelected) FinsibleIconPosition.Trailing else FinsibleIconPosition.Leading,
                    icon = {
                        if (isCustomDateSelected) {
                            Icon(
                                painter = painterResource(com.composables.icons.lucide.R.drawable.lucide_ic_x),
                                contentDescription = "Clear selected date",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable {
                                        onEvent(NewTransactionUiEvent.ClearCustomDate)
                                    }
                            )
                        } else {
                            Icon(
                                painter = painterResource(com.composables.icons.lucide.R.drawable.lucide_ic_calendar_days),
                                contentDescription = stringResource(R.string.cd_select_date_calendar_icon)
                            )
                        }
                    }
                )
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = FinsibleTheme.colors.borderSubtle,
                thickness = FinsibleTheme.stroke.thin
            )

            FinsibleToggle(
                checked = state.isRecurring,
                onCheckedChange = { onEvent(NewTransactionUiEvent.RecurringChanged(it)) },
                label = "Recurring",
                hint = "Repeat this transaction",
                arrangement = FinsibleToggleArrangement.SpaceBetween,
                labelPosition = FinsibleToggleLabelPosition.Leading,
                size = FinsibleSize.Small,
                colors = FinsibleToggleDefaults.colors(trackOnColor = accentColor)
            )

            if (state.isRecurring) {
                FinsibleChipsRow(
                    wrap = false,
                    chipKeys = recurringFrequencies,
                    chips = recurringFrequencies.map { frequency ->
                        {
                            DateQuickChip(
                                label = stringResource(frequency.displayText),
                                selected = state.recurringFrequency == frequency,
                                selectedTint = accentColor,
                                onClick = { onEvent(NewTransactionUiEvent.RecurringFrequencyChanged(frequency)) }
                            )
                        }
                    }
                )
            }

            if (hasError) {
                FinsibleText(
                    text = "Date is required",
                    textStyle = FinsibleTheme.typography.bodyMd.medium(),
                    color = FinsibleTheme.colors.feedbackError
                )
            }
        }
    }
}

@Composable
private fun DateQuickChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    selectedTint: Color,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    iconPosition: FinsibleIconPosition = FinsibleIconPosition.Leading
) {
    FinsibleFilterChip(
        modifier = modifier,
        selected = selected,
        onSelectedChange = { onClick() },
        label = label,
        size = FinsibleSize.Medium,
        shapeVariant = FinsibleShape.Rounded,
        selectedTint = selectedTint,
        icon = icon,
        iconPosition = iconPosition
    )
}

