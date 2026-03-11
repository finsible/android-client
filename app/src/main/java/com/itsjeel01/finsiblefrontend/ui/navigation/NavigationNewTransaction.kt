package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.itsjeel01.finsiblefrontend.common.TransactionRecurringFrequency
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.NewTransactionHeader
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.Step1Amount
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.Step2Date
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.Step3Category
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.Step4Accounts
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.Step5Description
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.StepControlButtons
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.StepTitle
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.viewmodel.NewTransactionViewModel

@Composable
fun NavigationNewTransaction(
    onNavigateBack: () -> Unit = {}
) {
    val viewModel: NewTransactionViewModel = hiltViewModel()
    val focusRequester = remember { FocusRequester() }
    val navState = rememberNewTransactionNavState()
    val navigator = remember(navState) {
        NewTransactionNavigator(state = navState)
    }
    val stepIndex = navState.currentStepIndex

    val state by viewModel.state.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    val availableToAccounts by viewModel.availableToAccounts.collectAsStateWithLifecycle()

    val onAmountChange = remember(viewModel) {
        { input: String ->
            viewModel.setTransactionAmountString(viewModel.validateAmount(input))
        }
    }
    val onDateChange = remember(viewModel) { { ms: Long -> viewModel.setTransactionDate(ms) } }
    val onIsRecurringChange = remember(viewModel) { { v: Boolean -> viewModel.setIsRecurring(v) } }
    val onRecurringFrequencyChange = remember(viewModel) { { f: TransactionRecurringFrequency -> viewModel.setRecurringFrequency(f) } }
    val onTransactionTypeChange = remember(viewModel) { { t: TransactionType -> viewModel.setTransactionType(t) } }
    val onCategorySelected = remember(viewModel) { { id: Long -> viewModel.setTransactionCategoryId(id) } }
    val onFromAccountSelected = remember(viewModel) { { id: Long -> viewModel.setTransactionFromAccountId(id) } }
    val onToAccountSelected = remember(viewModel) { { id: Long -> viewModel.setTransactionToAccountId(id) } }
    val onDescriptionChange = remember(viewModel) { { s: String -> viewModel.setTransactionDescription(s) } }

    val animDuration = Duration.MS_400.toInt()
    val emphasizedEasing = CubicBezierEasing(0.2f, 0.0f, 0f, 1.0f)
    val slideSpec = tween<IntOffset>(durationMillis = animDuration, easing = emphasizedEasing)
    val fadeSpec = tween<Float>(durationMillis = animDuration, easing = emphasizedEasing)

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
                NewTransactionHeader(onClose = onNavigateBack)
                StepTitle(stepIndex, navState.totalSteps)

                NavDisplay(
                    modifier = Modifier.weight(1f),
                    onBack = { navigator.back(onNavigateBack) },
                    transitionSpec = {
                        (slideInHorizontally(slideSpec) { w -> w } + fadeIn(fadeSpec))
                            .togetherWith(slideOutHorizontally(slideSpec) { w -> -w } + fadeOut(fadeSpec))
                    },
                    popTransitionSpec = {
                        (slideInHorizontally(slideSpec) { w -> -w } + fadeIn(fadeSpec))
                            .togetherWith(slideOutHorizontally(slideSpec) { w -> w } + fadeOut(fadeSpec))
                    },
                    entries = navState.toEntries(
                        entryProvider {
                            entry<Route.Home.NewTransaction.Amount> {
                                Step1Amount(
                                    amount = state.amountString,
                                    onAmountChange = onAmountChange,
                                    focusRequester = focusRequester,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            entry<Route.Home.NewTransaction.Date> {
                                Step2Date(
                                    dateMillis = state.dateMillis,
                                    isRecurring = state.isRecurring,
                                    recurringFrequency = state.recurringFrequency,
                                    onDateChange = onDateChange,
                                    onIsRecurringChange = onIsRecurringChange,
                                    onRecurringFrequencyChange = onRecurringFrequencyChange,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            entry<Route.Home.NewTransaction.Category> {
                                Step3Category(
                                    transactionType = state.transactionType,
                                    categories = categories,
                                    selectedCategoryId = state.categoryId,
                                    onTransactionTypeChange = onTransactionTypeChange,
                                    onCategorySelected = onCategorySelected,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            entry<Route.Home.NewTransaction.TransactionAccounts> {
                                Step4Accounts(
                                    transactionType = state.transactionType,
                                    fromAccountsOptions = accounts,
                                    toAccountsOptions = availableToAccounts,
                                    fromAccountId = state.fromAccountId,
                                    toAccountId = state.toAccountId,
                                    onFromAccountSelected = onFromAccountSelected,
                                    onToAccountSelected = onToAccountSelected
                                )
                            }
                            entry<Route.Home.NewTransaction.Description> {
                                Step5Description(
                                    description = state.description,
                                    onDescriptionChange = onDescriptionChange,
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
                    onNext = {
                        navigator.next {
                            viewModel.submit(
                                onSuccess = onNavigateBack,
                                onError = { /* TODO: Show error notification */ }
                            )
                        }
                    }
                )
            }
        }
    }
}