package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.TransactionRecurringFrequency
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step2Date(
    dateMillis: Long?,
    isRecurring: Boolean,
    recurringFrequency: TransactionRecurringFrequency,
    onDateChange: (Long) -> Unit,
    onIsRecurringChange: (Boolean) -> Unit,
    onRecurringFrequencyChange: (TransactionRecurringFrequency) -> Unit,
    modifier: Modifier = Modifier
) {
    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker,
        initialSelectedDateMillis = dateMillis ?: System.currentTimeMillis()
    )

    LaunchedEffect(datePickerState) {
        snapshotFlow { datePickerState.selectedDateMillis }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { selectedDate ->
                onDateChange(selectedDate)
            }
    }

    Column(
        modifier = modifier.padding(vertical = FinsibleTheme.dimes.d8),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d16)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = FinsibleTheme.dimes.d360, max = FinsibleTheme.dimes.d480)
                .clip(RoundedCornerShape(FinsibleTheme.dimes.d16))
                .background(FinsibleTheme.colors.surface)
        ) {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.matchParentSize(),
                showModeToggle = false,
                title = null,
                headline = null,
                colors = DatePickerDefaults.colors().copy(
                    containerColor = FinsibleTheme.colors.surface,
                    selectedDayContainerColor = FinsibleTheme.colors.brandAccent,
                    todayContentColor = FinsibleTheme.colors.link,
                    todayDateBorderColor = FinsibleTheme.colors.brandAccent,
                    dayContentColor = FinsibleTheme.colors.primaryContent,
                    weekdayContentColor = FinsibleTheme.colors.secondaryContent,
                    currentYearContentColor = FinsibleTheme.colors.link,
                    selectedYearContainerColor = FinsibleTheme.colors.brandAccent,
                    yearContentColor = FinsibleTheme.colors.primaryContent
                )
            )
        }

        Spacer(Modifier.height(FinsibleTheme.dimes.d16))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    FinsibleTheme.colors.surface,
                    RoundedCornerShape(FinsibleTheme.dimes.d12)
                )
                .padding(FinsibleTheme.dimes.d16),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                stringResource(R.string.make_recurring),
                style = FinsibleTheme.typography.t18.medium(),
                color = FinsibleTheme.colors.primaryContent
            )
            Switch(
                checked = isRecurring,
                onCheckedChange = { onIsRecurringChange(it) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = FinsibleTheme.colors.white,
                    checkedTrackColor = FinsibleTheme.colors.brandAccent,
                    uncheckedThumbColor = FinsibleTheme.colors.white,
                    uncheckedTrackColor = FinsibleTheme.colors.border,
                    uncheckedBorderColor = FinsibleTheme.colors.border
                )
            )
        }

        AnimatedVisibility(
            visible = isRecurring,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(Modifier.padding(top = FinsibleTheme.dimes.d12)) {
                Text(
                    stringResource(R.string.frequency),
                    style = FinsibleTheme.typography.t16.medium(),
                    color = FinsibleTheme.colors.secondaryContent,
                    modifier = Modifier.padding(bottom = FinsibleTheme.dimes.d8)
                )

                RecurringFrequencyDropdown(
                    currentFrequency = recurringFrequency,
                    onFrequencySelected = { onRecurringFrequencyChange(it) }
                )
            }
        }
    }
}


/** Dropdown for recurring frequency with hoisted selection. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecurringFrequencyDropdown(
    currentFrequency: TransactionRecurringFrequency,
    onFrequencySelected: (TransactionRecurringFrequency) -> Unit
) {
    val options = remember { TransactionRecurringFrequency.toOrderedList() }

    var expanded by remember { mutableStateOf(false) }

    val frequencyText = stringResource(currentFrequency.displayText)
    val textFieldState = remember(currentFrequency) {
        TextFieldState(frequencyText)
    }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            state = textFieldState,
            readOnly = true,
            lineLimits = TextFieldLineLimits.SingleLine,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = FinsibleTheme.colors.input,
                unfocusedContainerColor = FinsibleTheme.colors.input,
                focusedIndicatorColor = FinsibleTheme.colors.brandAccent,
                unfocusedIndicatorColor = FinsibleTheme.colors.border,
                focusedTextColor = FinsibleTheme.colors.primaryContent,
                unfocusedTextColor = FinsibleTheme.colors.primaryContent
            ),
            shape = RoundedCornerShape(FinsibleTheme.dimes.d12)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(FinsibleTheme.colors.surface)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            stringResource(option.displayText),
                            style = FinsibleTheme.typography.t18,
                            color = FinsibleTheme.colors.primaryContent
                        )
                    },
                    onClick = {
                        expanded = false
                        onFrequencySelected(option)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    modifier = Modifier.background(FinsibleTheme.colors.surface)
                )
            }
        }
    }
}