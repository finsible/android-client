package com.itsjeel01.finsiblefrontend.ui.view.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.theme.finsibleDatePickerColors
import com.itsjeel01.finsiblefrontend.ui.theme.finsibleDatePickerDialogColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class DatePickerSize { SMALL, LARGE }

@Composable
fun FinsibleDatePicker(
    value: Long,
    onDateSelected: (Long) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = {
        Icon(
            painter = painterResource(id = R.drawable.calendar_icon),
            contentDescription = "Select Date",
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )
    },
    errorIcon: @Composable (() -> Unit)? = null,
    supportingTextIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    supportingText: String? = null,
    enabled: Boolean = true,
    size: DatePickerSize = DatePickerSize.SMALL,
    accentColor: Color = MaterialTheme.colorScheme.primary,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    // Determine the height and text styles based on the size
    val (fieldHeight, textStyle, helpTextStyle) = when (size) {
        DatePickerSize.SMALL -> Triple(48.dp, MaterialTheme.typography.bodyMedium, MaterialTheme.typography.bodySmall)
        DatePickerSize.LARGE -> Triple(56.dp, MaterialTheme.typography.bodyLarge, MaterialTheme.typography.bodyMedium)
    }

    // Determine the color of the label and border based on focus and error state
    val focusColor = when {
        isError -> MaterialTheme.colorScheme.error
        isFocused -> accentColor
        else -> MaterialTheme.colorScheme.outline
    }

    fun onClick() {
        showDatePicker = true
        isFocused = true
    }

    Column(modifier = modifier) {
        // Label
        Text(
            text = label,
            style = helpTextStyle,
            color = focusColor,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )

        // Date Field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(fieldHeight)
                .border(1.dp, focusColor, RoundedCornerShape(2.dp))
                .clickable(
                    enabled = enabled,
                    onClick = { onClick() }
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Leading icon (optional)
                if (leadingIcon != null) {
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(24.dp)
                    ) { leadingIcon() }
                }

                // Date display
                Box(modifier = Modifier.weight(1f)) {
                    Text(
                        text = formatDate(value),
                        style = textStyle,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                // Trailing icon (optional)
                if (trailingIcon != null) {
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(24.dp)
                    ) { trailingIcon() }
                }
            }
        }

        // Error or supporting text (if provided)
        if (isError && errorMessage != null) {
            HelpRow(
                text = errorMessage,
                icon = errorIcon,
                textColor = MaterialTheme.colorScheme.error,
                textStyle = helpTextStyle
            )
        } else if (supportingText != null && !isError) {
            HelpRow(
                text = supportingText,
                icon = supportingTextIcon,
                textColor = MaterialTheme.colorScheme.onSecondaryContainer,
                textStyle = helpTextStyle
            )
        }
    }

    // Date picker dialog
    if (showDatePicker) {
        DatePickerDialog(
            initialDate = value,
            onDateSelected = {
                onDateSelected(it)
                isFocused = false
            },
            onDismiss = {
                showDatePicker = false
                isFocused = false
            },
            accentColor = accentColor
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialog(
    initialDate: Long,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
    accentColor: Color,
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDate)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        colors = finsibleDatePickerDialogColors(),
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                    onDismiss()
                },
                colors = ButtonDefaults.textButtonColors().copy(
                    containerColor = accentColor.copy(alpha = 0.5F),
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSecondaryContainer)
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            colors = finsibleDatePickerColors(accentColor = accentColor)
        )
    }
}

private fun formatDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}