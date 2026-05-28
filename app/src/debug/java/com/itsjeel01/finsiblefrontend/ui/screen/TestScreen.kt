package com.itsjeel01.finsiblefrontend.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.BuildConfig
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleCheckbox
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleToggle
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleTopNavigationBar
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleIconPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleCheckboxDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleHeaderState
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleToggleArrangement
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleToggleLabelPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleCheckboxVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium
import com.itsjeel01.finsiblefrontend.ui.viewmodel.OperationStatus
import com.itsjeel01.finsiblefrontend.ui.viewmodel.TestViewModel
import kotlinx.coroutines.delay
import com.composables.icons.lucide.R as LucideR

/** Data-driven checkbox action - executes when Launch clicked. */
private data class CheckboxAction(
    val label: String,
    val isDestructive: Boolean = false,
    val action: () -> Unit
)

/** Data-driven checkbox toggle. */
private data class CheckboxToggle(
    val label: String,
    val checked: Boolean,
    val onToggle: (Boolean) -> Unit
)

/** Debug test screen for development tools and API mocking. */
@Composable
fun TestScreen(
    onNavigateToApp: () -> Unit,
    onNavigateToPlayground: () -> Unit,
    viewModel: TestViewModel
) {
    val operationStatus by viewModel.operationStatus.collectAsStateWithLifecycle()
    val mockApiEnabled by viewModel.mockApiEnabled.collectAsStateWithLifecycle()
    val mockAuth by viewModel.mockAuth.collectAsStateWithLifecycle()
    val mockIncomeCategories by viewModel.mockIncomeCategories.collectAsStateWithLifecycle()
    val mockExpenseCategories by viewModel.mockExpenseCategories.collectAsStateWithLifecycle()
    val mockTransferCategories by viewModel.mockTransferCategories.collectAsStateWithLifecycle()
    val mockAccountGroups by viewModel.mockAccountGroups.collectAsStateWithLifecycle()
    val mockAccounts by viewModel.mockAccounts.collectAsStateWithLifecycle()
    val mockAccountsFresh by viewModel.mockAccountsFresh.collectAsStateWithLifecycle()
    val mockSnapshot by viewModel.mockSnapshot.collectAsStateWithLifecycle()
    val mockTransactions by viewModel.mockTransactions.collectAsStateWithLifecycle()
    val mockExchangeRates by viewModel.mockExchangeRates.collectAsStateWithLifecycle()

    // Track which actions are selected
    var selectedActions by remember { mutableStateOf(setOf<String>()) }

    val title = "Finsible Test Screen"
    val subtitle = "App Version v${BuildConfig.VERSION_NAME}"

    LaunchedEffect(operationStatus) {
        if (operationStatus is OperationStatus.Success || operationStatus is OperationStatus.Error) {
            delay(3000)
            viewModel.clearStatus()
        }
    }

    // Define checkbox actions - add new ones here
    val checkboxActions = remember {
        listOf(
            CheckboxAction("Clear all data", true) { viewModel.clearAllAppData() },
            CheckboxAction("Clear preferences", false) { viewModel.clearPreferences() },
            CheckboxAction("Flush database", true) { viewModel.flushEntireDatabase() },
            CheckboxAction("Flush categories", false) { viewModel.flushEntity("Categories") },
            CheckboxAction("Flush accounts", false) { viewModel.flushEntity("Accounts") },
            CheckboxAction("Flush account groups", false) { viewModel.flushEntity("Account Groups") },
            CheckboxAction("Reset settings", false) { viewModel.resetToDefaults() }
        )
    }

    // Define endpoint checkboxes
    val endpointToggles = remember(
        mockApiEnabled,
        mockAuth,
        mockIncomeCategories,
        mockExpenseCategories,
        mockTransferCategories,
        mockAccountGroups,
        mockAccounts,
        mockAccountsFresh,
        mockSnapshot,
        mockTransactions,
        mockExchangeRates
    ) {
        if (mockApiEnabled) listOf(
            CheckboxToggle("Authentication", mockAuth) { viewModel.toggleMockAuth(it) },
            CheckboxToggle("Income categories", mockIncomeCategories) { viewModel.toggleMockIncomeCategories(it) },
            CheckboxToggle("Expense categories", mockExpenseCategories) { viewModel.toggleMockExpenseCategories(it) },
            CheckboxToggle("Transfer categories", mockTransferCategories) { viewModel.toggleMockTransferCategories(it) },
            CheckboxToggle("Account groups", mockAccountGroups) { viewModel.toggleMockAccountGroups(it) },
            CheckboxToggle("Accounts", mockAccounts) { viewModel.toggleMockAccounts(it) },
            CheckboxToggle("Accounts (fresh)", mockAccountsFresh) { viewModel.toggleMockAccountsFresh(it) },
            CheckboxToggle("Snapshot", mockSnapshot) { viewModel.toggleMockSnapshot(it) },
            CheckboxToggle("Transactions", mockTransactions) { viewModel.toggleMockTransactions(it) },
            CheckboxToggle("Exchange rates", mockExchangeRates) { viewModel.toggleMockExchangeRates(it) }
        ) else emptyList()
    }

    val headerState = remember(title, subtitle) {
        FinsibleHeaderState(title = title, subtitle = subtitle)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = FinsibleTheme.colors.surfaceBase,
        contentWindowInsets = WindowInsets(0, 0, 0, 0), // Header handles its own insets safely
        topBar = {
            FinsibleTopNavigationBar(state = headerState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(FinsibleTheme.colors.surfaceBase)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = FinsibleTheme.spacing.gapMd)
                    .padding(top = FinsibleTheme.spacing.gapMd)
            ) {
                when (operationStatus) {
                    is OperationStatus.Loading -> StatusText((operationStatus as OperationStatus.Loading).message, false)
                    is OperationStatus.Success -> StatusText((operationStatus as OperationStatus.Success).message, false)
                    is OperationStatus.Error -> StatusText((operationStatus as OperationStatus.Error).message, true)
                    OperationStatus.Idle -> {}
                }

                Spacer(modifier = Modifier.height(FinsibleTheme.spacing.insetSm))

                Section("Actions") {
                    checkboxActions.forEach { action ->
                        CheckboxRow(
                            label = action.label,
                            checked = selectedActions.contains(action.label),
                            onToggle = { checked ->
                                selectedActions = if (checked) {
                                    selectedActions + action.label
                                } else {
                                    selectedActions - action.label
                                }
                            },
                            isDestructive = action.isDestructive
                        )
                    }
                }

                Section("Mock API") {
                    SwitchRow(mockApiEnabled) { viewModel.toggleMockApi(it) }
                    if (mockApiEnabled) {
                        Spacer(modifier = Modifier.height(FinsibleTheme.spacing.inlineMd))
                        endpointToggles.forEach { CheckboxRow(it.label, it.checked, it.onToggle) }
                    }
                }

                Section(title = stringResource(R.string.component_playground_section_title)) {
                    FinsibleButton(
                        onClick = onNavigateToPlayground,
                        text = stringResource(R.string.component_playground_section_button),
                        fullWidth = true,
                        size = FinsibleSize.Medium,
                        variant = FinsibleButtonVariant.Outlined,
                        iconPosition = FinsibleIconPosition.Trailing,
                        icon = {
                            Icon(
                                painter = painterResource(LucideR.drawable.lucide_ic_arrow_right),
                                contentDescription = null
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(FinsibleTheme.spacing.gapMd))
            }

            // Launch button at bottom - executes selected actions then navigates
            FinsibleButton(
                onClick = {
                    // Execute selected actions
                    checkboxActions.forEach { action ->
                        if (selectedActions.contains(action.label)) {
                            action.action()
                        }
                    }
                    // Navigate to app
                    onNavigateToApp()
                },
                text = "LAUNCH",
                fullWidth = true,
                shapeVariant = FinsibleShape.Rounded,
                size = FinsibleSize.Medium,
                variant = FinsibleButtonVariant.Filled,
                colors = FinsibleButtonDefaults.colors(
                    variant = FinsibleButtonVariant.Filled,
                    containerColor = FinsibleTheme.colors.brandInteractive,
                ),
                modifier = Modifier
                    .padding(horizontal = FinsibleTheme.spacing.gapMd)
                    .padding(bottom = FinsibleTheme.spacing.gapMd),
            )
        }
    }
}

@Composable
private fun Section(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = FinsibleTheme.spacing.insetSm),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackXs)
    ) {
        FinsibleText(
            text = title,
            textStyle = FinsibleTheme.typography.labelSm.medium(),
            color = FinsibleTheme.colors.contentSecondary,
            uppercase = true
        )
        Spacer(modifier = Modifier.height(FinsibleTheme.spacing.insetXs))
        content()
    }
}

@Composable
private fun SwitchRow(
    checked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    FinsibleToggle(
        checked = checked,
        onCheckedChange = onToggle,
        label = "Enable mocking",
        size = FinsibleSize.Small,
        arrangement = FinsibleToggleArrangement.SpaceBetween,
        labelPosition = FinsibleToggleLabelPosition.Leading,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = FinsibleTheme.spacing.insetXs)
    )
}

@Composable
private fun CheckboxRow(
    label: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit,
    isDestructive: Boolean = false
) {
    FinsibleCheckbox(
        checked = checked,
        onCheckedChange = onToggle,
        enforceMinTouchTarget = false,
        size = FinsibleSize.Small,
        variant = FinsibleCheckboxVariant.Colorful,
        colors = if (isDestructive) {
            FinsibleCheckboxDefaults.colors(
                variant = FinsibleCheckboxVariant.Colorful,
                labelColor = FinsibleTheme.colors.feedbackError
            )
        } else {
            FinsibleCheckboxDefaults.colors(variant = FinsibleCheckboxVariant.Colorful)
        },
        label = label
    )
    Spacer(modifier = Modifier.height(FinsibleTheme.spacing.gapMd))
}

@Composable
private fun StatusText(message: String, isError: Boolean) {
    FinsibleText(
        text = message,
        textStyle = FinsibleTheme.typography.bodySm,
        color = if (isError) FinsibleTheme.colors.feedbackError else FinsibleTheme.colors.feedbackSuccess,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = FinsibleTheme.spacing.insetXs)
    )
}
