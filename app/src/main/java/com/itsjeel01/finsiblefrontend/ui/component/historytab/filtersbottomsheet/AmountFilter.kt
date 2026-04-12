package com.itsjeel01.finsiblefrontend.ui.component.historytab.filtersbottomsheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleTextField
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleTextFieldDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

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
        FinsibleText(
            text = stringResource(R.string.amount_range),
            variant = FinsibleTextVariant.MicroLabelSemiBold,
            colorVariant = FinsibleTextColorVariant.Secondary,
            uppercase = true
        )

        Spacer(Modifier.height(FilterSheetSpacing.sectionHeaderGap))

        Row(
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d10),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FinsibleTextField(
                value = minAmount,
                onValueChange = { onMinChange(sanitizeDecimalInput(it)) },
                modifier = Modifier.weight(1f),
                placeholder = stringResource(R.string.amount_min_placeholder),
                size = FinsibleSize.Small,
                inputConfig = FinsibleTextFieldDefaults.inputConfig(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
            FinsibleText(
                text = stringResource(R.string.amount_range_dash),
                variant = FinsibleTextVariant.BodyMedium,
                color = FinsibleTheme.colors.tertiaryContent
            )
            FinsibleTextField(
                value = maxAmount,
                onValueChange = { onMaxChange(sanitizeDecimalInput(it)) },
                modifier = Modifier.weight(1f),
                placeholder = stringResource(R.string.amount_max_placeholder),
                size = FinsibleSize.Small,
                inputConfig = FinsibleTextFieldDefaults.inputConfig(
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
            FinsibleText(
                text = stringResource(R.string.min_exceeds_max_error),
                variant = FinsibleTextVariant.SmallLabelMedium,
                colorVariant = FinsibleTextColorVariant.Error
            )
        }
    }
}