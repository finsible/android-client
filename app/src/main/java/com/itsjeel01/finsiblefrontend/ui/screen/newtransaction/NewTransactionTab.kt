package com.itsjeel01.finsiblefrontend.ui.screen.newtransaction

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.fin.ButtonConfig
import com.itsjeel01.finsiblefrontend.ui.component.fin.ComponentSize
import com.itsjeel01.finsiblefrontend.ui.component.fin.ComponentType
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleIconButton
import com.itsjeel01.finsiblefrontend.ui.component.fin.IconButtonConfig
import com.itsjeel01.finsiblefrontend.ui.component.fin.IconButtonShape
import com.itsjeel01.finsiblefrontend.ui.navigation.TabBackHandler
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.itsjeel01.finsiblefrontend.ui.theme.extraBold
import com.itsjeel01.finsiblefrontend.ui.viewmodel.NewTransactionViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewTransactionTab(
    onNavigateBack: () -> Unit = {},
) {
    val viewModel: NewTransactionViewModel = hiltViewModel()
    val focusRequester = remember { FocusRequester() }

    // Hoist all state at the screen level
    val currentStep by viewModel.currentStep.collectAsStateWithLifecycle()
    val showBackButton by viewModel.showBackButton.collectAsStateWithLifecycle()
    val canContinue by viewModel.canContinue.collectAsStateWithLifecycle()
    val canGoBack by viewModel.canGoBack.collectAsStateWithLifecycle()
    val totalSteps = viewModel.totalSteps()

    TabBackHandler {
        onNavigateBack()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = FinsibleTheme.dimes.d24)
            .background(FinsibleTheme.colors.primaryBackground)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    FinsibleTheme.colors.secondaryBackground,
                    shape = RoundedCornerShape(
                        topStart = FinsibleTheme.dimes.d24,
                        topEnd = FinsibleTheme.dimes.d24
                    )
                )
                .padding(
                    vertical = FinsibleTheme.dimes.d16,
                    horizontal = FinsibleTheme.dimes.d24
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                TabHeader(onClose = onNavigateBack)

                StepTitle(currentStep = currentStep, totalSteps = totalSteps)

                Column(modifier = Modifier.weight(1f)) {
                    when (currentStep) {
                        0 -> Step1Amount(
                            modifier = Modifier.weight(1f),
                            focusRequester = focusRequester,
                            viewModel = viewModel
                        )

                        1 -> Step2Date(
                            modifier = Modifier.weight(1f),
                            viewModel = viewModel
                        )

                        2 -> Step3Category(
                            modifier = Modifier.weight(1f),
                            viewModel = viewModel
                        )

                        3 -> Step4Accounts(
                            viewModel = viewModel
                        )

                        4 -> Step5Description(
                            viewModel = viewModel,
                            focusRequester = focusRequester
                        )

                        else -> {}
                    }
                }

                StepControlButtons(
                    currentStep = currentStep,
                    showBackButton = showBackButton,
                    canContinue = canContinue,
                    canGoBack = canGoBack,
                    onBack = viewModel::previousStep,
                    onContinue = viewModel::nextStep,
                    onSubmit = { viewModel.submit(onNavigateBack) }
                )
            }
        }
    }
}

@Composable
private fun TabHeader(onClose: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        FinsibleIconButton(
            icon = R.drawable.ic_close,
            onClick = onClose,
            modifier = Modifier.align(Alignment.CenterStart),
            contentDescription = "Close",
            config = IconButtonConfig(
                size = ComponentSize.Large,
                type = ComponentType.Tertiary,
                shape = IconButtonShape.Circle
            )
        )
        Text(
            "New Transaction",
            style = FinsibleTheme.typography.t20.bold(),
            color = FinsibleTheme.colors.primaryContent,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun StepTitle(currentStep: Int, totalSteps: Int) {
    val title = when (currentStep) {
        0 -> "Enter Amount"
        1 -> "Date & Schedule"
        2 -> "Select Category"
        3 -> "Select Accounts"
        4 -> "Add Description (Optional)"
        else -> "Enter Amount"
    }

    Spacer(Modifier.height(FinsibleTheme.dimes.d16))
    Column(Modifier.padding(vertical = FinsibleTheme.dimes.d16)) {
        Text(
            title,
            style = FinsibleTheme.typography.t24.extraBold(),
            color = FinsibleTheme.colors.primaryContent,
        )
        Spacer(Modifier.height(FinsibleTheme.dimes.d4))

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)) {
            Text(
                "Step ${currentStep + 1} of $totalSteps",
                style = FinsibleTheme.typography.t16,
                color = FinsibleTheme.colors.secondaryContent,
            )

            val progress by animateFloatAsState(
                targetValue = currentStep.toFloat() / totalSteps.toFloat(),
                animationSpec = tween(
                    durationMillis = 400,
                    easing = FastOutSlowInEasing
                ),
                label = "progress"
            )

            // Linear step progress bar with animation
            Box(
                modifier = Modifier
                    .height(FinsibleTheme.dimes.d3)
                    .width(FinsibleTheme.dimes.d96)
                    .clip(RoundedCornerShape(FinsibleTheme.dimes.d2))
                    .background(FinsibleTheme.colors.border)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .clip(RoundedCornerShape(FinsibleTheme.dimes.d2))
                        .background(FinsibleTheme.colors.brandAccent)
                        .animateContentSize()
                )
            }
        }
    }
}

/** Stateless step control buttons with hoisted state. */
@Composable
private fun StepControlButtons(
    currentStep: Int,
    showBackButton: Boolean,
    canContinue: Boolean,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onContinue: () -> Unit,
    onSubmit: () -> Unit
) {
    val continueButtonText = if (currentStep == 4) "Confirm" else "Continue"

    Row(
        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d16),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = FinsibleTheme.dimes.d8)
            .windowInsetsPadding(WindowInsets.ime)
    ) {
        if (showBackButton) {
            FinsibleButton(
                text = "Back",
                onClick = onBack,
                config = ButtonConfig(
                    type = ComponentType.Tertiary,
                    size = ComponentSize.Medium,
                    fullWidth = false,
                    enabled = canGoBack
                )
            )
        }

        FinsibleButton(
            text = continueButtonText,
            onClick = { if (currentStep < 4) onContinue() else onSubmit() },
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

