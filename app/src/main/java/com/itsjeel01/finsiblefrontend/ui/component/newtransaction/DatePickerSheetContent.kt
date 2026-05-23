package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleDatePicker
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleDatePickerDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun DatePickerSheetContent(
    initialDateMillis: Long?,
    accentColor: Color,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val zoneId = remember { ZoneId.systemDefault() }
    val initialDate = remember(initialDateMillis, zoneId) {
        initialDateMillis?.let { millis ->
            Instant.ofEpochMilli(millis).atZone(zoneId).toLocalDate()
        }
    }
    var selectedDate by remember(initialDate) { mutableStateOf(initialDate ?: LocalDate.now()) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = FinsibleTheme.spacing.insetLg)
            .padding(bottom = FinsibleTheme.spacing.insetLg)
    ) {
        FinsibleDatePicker(
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it },
            colors = FinsibleDatePickerDefaults.colors(
                selectedDayContainerColor = accentColor,
                selectedDayContentColor = FinsibleTheme.colors.surfaceBase,
                todayBorderColor = accentColor
            )
        )

        Spacer(modifier = Modifier.height(FinsibleTheme.spacing.inset2xl))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.gapMd)
        ) {
            FinsibleButton(
                text = "Cancel",
                onClick = onDismiss,
                variant = FinsibleButtonVariant.Outlined,
                size = FinsibleSize.Medium,
                fullWidth = true,
                modifier = Modifier.weight(1f)
            )
            FinsibleButton(
                text = "Confirm",
                onClick = {
                    val millis = selectedDate.atStartOfDay(zoneId).toInstant().toEpochMilli()
                    onDateSelected(millis)
                },
                colors = FinsibleButtonDefaults.colors(
                    variant = FinsibleButtonVariant.Filled,
                    containerColor = accentColor,
                    contentColor = FinsibleTheme.colors.surfaceBase,
                    rippleColor = accentColor.copy(alpha = 0.12f)
                ),
                size = FinsibleSize.Medium,
                fullWidth = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}