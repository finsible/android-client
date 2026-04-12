package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.navigation.NewTransactionSteps
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun StepControlButtons(
    canContinue: Boolean,
    stepIndex: Int,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val canGoBack = stepIndex > 0

    val isLastStep = stepIndex == NewTransactionSteps.lastIndex
    val continueButtonText = if (isLastStep) stringResource(R.string.confirm) else stringResource(R.string.next)

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d16),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = FinsibleTheme.dimes.d8)
            .windowInsetsPadding(WindowInsets.ime)
    ) {
        if (canGoBack) {
            FinsibleButton(
                text = stringResource(R.string.back),
                onClick = onBack,
                size = FinsibleSize.Medium,
                variant = FinsibleButtonVariant.Text,
                colors = FinsibleButtonDefaults.colors(
                    variant = FinsibleButtonVariant.Text,
                    contentColor = FinsibleTheme.colors.secondaryContent
                )
            )
        }

        FinsibleButton(
            text = continueButtonText,
            onClick = {
                keyboardController?.hide()
                focusManager.clearFocus(true)
                onNext()
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            size = FinsibleSize.Medium,
            variant = FinsibleButtonVariant.Filled,
            enabled = canContinue
        )
    }
}