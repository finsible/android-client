package com.itsjeel01.finsiblefrontend.ui.component.historytab.filtersbottomsheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleTextField
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleTextFieldDefaults
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium

@Composable
fun AmountFilter(
    minAmount: String,
    maxAmount: String,
    onMinChange: (String) -> Unit,
    onMaxChange: (String) -> Unit,
    isError: Boolean = false
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        SectionLabel(stringResource(R.string.amount_range))
        Row(
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d10),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FinsibleTextField(
                value = minAmount,
                onValueChange = { onMinChange(sanitizeDecimalInput(it)) },
                modifier = Modifier.weight(1f),
                placeholder = stringResource(R.string.amount_min_placeholder),
                sizes = FinsibleTextFieldDefaults.smallSizes(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
            Text(
                stringResource(R.string.amount_range_dash),
                style = FinsibleTheme.typography.t16.medium(),
                color = FinsibleTheme.colors.tertiaryContent
            )
            FinsibleTextField(
                value = maxAmount,
                onValueChange = { onMaxChange(sanitizeDecimalInput(it)) },
                modifier = Modifier.weight(1f),
                placeholder = stringResource(R.string.amount_max_placeholder),
                sizes = FinsibleTextFieldDefaults.smallSizes(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
        }
        AnimatedVisibility(
            visible = isError,
            enter = fadeIn(tween(Duration.MS_150.toInt())),
            exit = fadeOut(tween(Duration.MS_100.toInt()))
        ) {
            Text(
                text = stringResource(R.string.min_exceeds_max_error),
                style = FinsibleTheme.typography.t12.medium(),
                color = FinsibleTheme.colors.error
            )
        }
    }
}