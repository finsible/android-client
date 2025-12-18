package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.fin.ComponentSize
import com.itsjeel01.finsiblefrontend.ui.component.fin.ComponentType
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleIconButton
import com.itsjeel01.finsiblefrontend.ui.component.fin.IconButtonConfig
import com.itsjeel01.finsiblefrontend.ui.component.fin.IconButtonShape
import com.itsjeel01.finsiblefrontend.ui.screen.newtransaction.Step1Amount
import com.itsjeel01.finsiblefrontend.ui.screen.newtransaction.Step2Date
import com.itsjeel01.finsiblefrontend.ui.screen.newtransaction.Step3Category
import com.itsjeel01.finsiblefrontend.ui.screen.newtransaction.Step4Accounts
import com.itsjeel01.finsiblefrontend.ui.screen.newtransaction.Step5Description
import com.itsjeel01.finsiblefrontend.ui.screen.newtransaction.StepControlButtons
import com.itsjeel01.finsiblefrontend.ui.screen.newtransaction.StepTitle
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.itsjeel01.finsiblefrontend.ui.viewmodel.NewTransactionViewModel

@Composable
fun NavigationNewTransaction(
    onNavigateBack: () -> Unit = {}
) {
    val viewModel: NewTransactionViewModel = hiltViewModel()
    val focusRequester = remember { FocusRequester() }
    val navState = rememberNewTransactionNavState()
    val navigator = remember(navState) {
        NewTransactionNavigator(
            state = navState
        )
    }
    val stepIndex = navState.currentStepIndex

    Box(
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
                StepTitle(stepIndex, navState.totalSteps)

                NavDisplay(
                    modifier = Modifier.weight(1f),
                    onBack = { navigator.back(onNavigateBack) },
                    entries = navState.toEntries(
                        entryProvider {
                            entry<Route.Home.NewTransaction.Amount> {
                                Step1Amount(
                                    modifier = Modifier.fillMaxSize(),
                                    focusRequester = focusRequester,
                                    viewModel = viewModel
                                )
                            }
                            entry<Route.Home.NewTransaction.Date> {
                                Step2Date(
                                    modifier = Modifier.fillMaxSize(),
                                    viewModel = viewModel
                                )
                            }
                            entry<Route.Home.NewTransaction.Category> {
                                Step3Category(
                                    modifier = Modifier.fillMaxSize(),
                                    viewModel = viewModel
                                )
                            }
                            entry<Route.Home.NewTransaction.Accounts> {
                                Step4Accounts(
                                    viewModel = viewModel
                                )
                            }
                            entry<Route.Home.NewTransaction.Description> {
                                Step5Description(
                                    viewModel = viewModel,
                                    focusRequester = focusRequester
                                )
                            }
                        }
                    )
                )

                val canContinue by viewModel.isStepValid(navState.currentStep).collectAsStateWithLifecycle(initialValue = false)

                StepControlButtons(
                    canContinue = canContinue,
                    stepIndex = stepIndex,
                    onBack = { navigator.back(onNavigateBack) },
                    onNext = { navigator.next { viewModel.submit(onNavigateBack) } }
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
