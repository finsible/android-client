package com.itsjeel01.finsiblefrontend.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.fin.ButtonConfig
import com.itsjeel01.finsiblefrontend.ui.component.fin.ComponentSize
import com.itsjeel01.finsiblefrontend.ui.component.fin.ComponentType
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.fin.IconPosition
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.itsjeel01.finsiblefrontend.ui.theme.medium
import com.itsjeel01.finsiblefrontend.ui.theme.normal
import com.itsjeel01.finsiblefrontend.ui.viewmodel.OperationStatus
import com.itsjeel01.finsiblefrontend.ui.viewmodel.TestViewModel
import kotlinx.coroutines.delay

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

    // Track which actions are selected
    var selectedActions by remember { mutableStateOf(setOf<String>()) }

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
        mockAccountsFresh,
        mockTransactions
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
            CheckboxToggle("Transactions", mockTransactions) { viewModel.toggleMockTransactions(it) }
        ) else emptyList()
    }

    val safeDrawingPadding = WindowInsets.safeDrawing.asPaddingValues()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FinsibleTheme.colors.primaryBackground)
            .padding(safeDrawingPadding)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = FinsibleTheme.dimes.d16)
        ) {
            Spacer(modifier = Modifier.height(FinsibleTheme.dimes.d8))

            Text(
                text = "Test Screen",
                style = FinsibleTheme.typography.t18.bold(),
                color = FinsibleTheme.colors.primaryContent
            )

            when (operationStatus) {
                is OperationStatus.Loading -> StatusText((operationStatus as OperationStatus.Loading).message, false)
                is OperationStatus.Success -> StatusText((operationStatus as OperationStatus.Success).message, false)
                is OperationStatus.Error -> StatusText((operationStatus as OperationStatus.Error).message, true)
                OperationStatus.Idle -> {}
            }

            Spacer(modifier = Modifier.height(FinsibleTheme.dimes.d12))

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
                SwitchRow("Enable mocking", mockApiEnabled) { viewModel.toggleMockApi(it) }
                if (mockApiEnabled) {
                    Spacer(modifier = Modifier.height(FinsibleTheme.dimes.d6))
                    HorizontalDivider(color = FinsibleTheme.colors.divider)
                    Spacer(modifier = Modifier.height(FinsibleTheme.dimes.d6))
                    endpointToggles.forEach { CheckboxRow(it.label, it.checked, it.onToggle) }
                }
            }

            Spacer(modifier = Modifier.height(FinsibleTheme.dimes.d16))
        }

        // Launch button at bottom - executes selected actions then navigates
        FinsibleButton(
            text = "Launch",
            config = ButtonConfig(
                type = ComponentType.Brand,
                iconPosition = IconPosition.Trailing,
                icon = R.drawable.ic_right_arrow_dotted,
                size = ComponentSize.Medium
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = FinsibleTheme.dimes.d16)
                .padding(bottom = FinsibleTheme.dimes.d16),
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
        )
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
            .padding(vertical = FinsibleTheme.dimes.d8)
    ) {
        Text(
            text = title.uppercase(),
            style = FinsibleTheme.typography.t10.medium(),
            color = FinsibleTheme.colors.secondaryContent
        )
        Spacer(modifier = Modifier.height(FinsibleTheme.dimes.d6))
        content()
    }
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = FinsibleTheme.dimes.d4),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = FinsibleTheme.typography.t14.normal(),
            color = FinsibleTheme.colors.primaryContent,
            modifier = Modifier.weight(1f)
        )
        Switch(checked = checked, onCheckedChange = onToggle)
    }
}

@Composable
private fun CheckboxRow(
    label: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit,
    isDestructive: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onToggle
        )
        Text(
            text = label,
            style = FinsibleTheme.typography.t14.normal(),
            color = if (isDestructive) FinsibleTheme.colors.error else FinsibleTheme.colors.primaryContent,
            modifier = Modifier.padding(start = FinsibleTheme.dimes.d8)
        )
    }
}

@Composable
private fun StatusText(message: String, isError: Boolean) {
    Text(
        text = message,
        style = FinsibleTheme.typography.t12.normal(),
        color = if (isError) FinsibleTheme.colors.error else FinsibleTheme.colors.success,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = FinsibleTheme.dimes.d4)
    )
}
