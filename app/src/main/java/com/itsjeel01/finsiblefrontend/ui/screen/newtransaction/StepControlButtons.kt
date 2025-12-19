package com.itsjeel01.finsiblefrontend.ui.screen.newtransaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.itsjeel01.finsiblefrontend.ui.component.fin.ButtonConfig
import com.itsjeel01.finsiblefrontend.ui.component.fin.ComponentSize
import com.itsjeel01.finsiblefrontend.ui.component.fin.ComponentType
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleButton
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
    val continueButtonText = if (isLastStep) "Confirm" else "Continue"

    Row(
        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d16),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = FinsibleTheme.dimes.d8)
            .windowInsetsPadding(WindowInsets.ime)
    ) {
        if (canGoBack) {
            FinsibleButton(
                text = "Back",
                onClick = onBack,
                config = ButtonConfig(
                    type = ComponentType.Tertiary,
                    size = ComponentSize.Medium,
                    fullWidth = false,
                    enabled = true
                )
            )
        }

        FinsibleButton(
            text = continueButtonText,
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            config = ButtonConfig(
                type = ComponentType.Primary,
                size = ComponentSize.Medium,
                enabled = canContinue
            )
        )
    }
}