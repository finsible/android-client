package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.model.event.NewTransactionUiEvent
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionFormState
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionValidationError
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.AccountUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium

/** Account section for the new transaction form.
 *
 * For Expense:
 *   - "FROM" section with [ExpenseIncomeAccountRow] + [AccountSectionLabelRow].
 * For Income:
 *   - "TO" section with [ExpenseIncomeAccountRow] + [AccountSectionLabelRow].
 * For Transfer:
 *   - [TransferAccountRow] with two equal-width cards, swap button, and mutual exclusion.
 *
 * All sections use [AccountSectionLabelRow] for the header with "Frequent" hint and "All" button.
 */
@Composable
fun NewTransactionAccountSection(
    state: NewTransactionFormState,
    fromAccounts: List<AccountUIModel>,
    toAccounts: List<AccountUIModel>,
    isFromExpanded: Boolean,
    onFromExpandedChange: (Boolean) -> Unit,
    isToExpanded: Boolean,
    onToExpandedChange: (Boolean) -> Unit,
    onOpenFromAccounts: () -> Unit,
    onOpenToAccounts: () -> Unit,
    accentColor: androidx.compose.ui.graphics.Color,
    onEvent: (NewTransactionUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val needsFrom = state.transactionType != TransactionType.INCOME
    val needsTo = state.transactionType != TransactionType.EXPENSE

    val selectedFromAccount = remember(fromAccounts, state.fromAccountId) {
        fromAccounts.find { it.id == state.fromAccountId }
    }
    val selectedToAccount = remember(toAccounts, state.toAccountId) {
        toAccounts.find { it.id == state.toAccountId }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(NewTransactionSectionDefaults.contentSpacing)
    ) {
        if (state.transactionType == TransactionType.TRANSFER) {
            // Transfer: use TransferAccountRow with FROM/TO
            TransferAccountRow(
                fromAccounts = fromAccounts,
                toAccounts = toAccounts,
                selectedFromId = state.fromAccountId,
                selectedToId = state.toAccountId,
                onFromSelected = { onEvent(NewTransactionUiEvent.FromAccountSelected(it)) },
                onToSelected = { onEvent(NewTransactionUiEvent.ToAccountSelected(it)) },
                accentColor = accentColor,
                fromExpanded = isFromExpanded,
                toExpanded = isToExpanded,
                onFromExpandedChange = onFromExpandedChange,
                onToExpandedChange = onToExpandedChange,
                onOpenFromAll = onOpenFromAccounts,
                onOpenToAll = onOpenToAccounts,
                onError = if (state.validationErrors.contains(NewTransactionValidationError.TRANSFER_ACCOUNTS_MUST_DIFFER)) {
                    {
                        FinsibleText(
                            text = "From and To accounts must be different",
                            textStyle = FinsibleTheme.typography.bodyMd.medium(),
                            color = FinsibleTheme.colors.feedbackError
                        )
                    }
                } else null
            )
        } else {
            // Expense or Income
            if (needsFrom) {
                Column(verticalArrangement = Arrangement.spacedBy(NewTransactionSectionDefaults.headerSpacing)) {
                    AccountSectionLabelRow(
                        label = "From",
                        onOpenAll = onOpenFromAccounts,
                        accentColor = accentColor
                    )

                    ExpenseIncomeAccountRow(
                        shortlistItems = fromAccounts,
                        selectedItem = selectedFromAccount,
                        isExpanded = isFromExpanded,
                        onExpandedChange = onFromExpandedChange,
                        onItemSelected = { onEvent(NewTransactionUiEvent.FromAccountSelected(it.id)) },
                        accentColor = accentColor,
                        onError = if (state.validationErrors.contains(NewTransactionValidationError.FROM_ACCOUNT)) {
                            {
                                FinsibleText(
                                    text = "From account is required",
                                    textStyle = FinsibleTheme.typography.bodyMd.medium(),
                                    color = FinsibleTheme.colors.feedbackError
                                )
                            }
                        } else null
                    )
                }
            }

            if (needsTo) {
                Column(verticalArrangement = Arrangement.spacedBy(NewTransactionSectionDefaults.headerSpacing)) {
                    AccountSectionLabelRow(
                        label = "To",
                        onOpenAll = onOpenToAccounts,
                        accentColor = accentColor
                    )

                    ExpenseIncomeAccountRow(
                        shortlistItems = toAccounts,
                        selectedItem = selectedToAccount,
                        isExpanded = isToExpanded,
                        onExpandedChange = onToExpandedChange,
                        onItemSelected = { onEvent(NewTransactionUiEvent.ToAccountSelected(it.id)) },
                        accentColor = accentColor,
                        onError = if (state.validationErrors.contains(NewTransactionValidationError.TO_ACCOUNT)) {
                            {
                                FinsibleText(
                                    text = "To account is required",
                                    textStyle = FinsibleTheme.typography.bodyMd.medium(),
                                    color = FinsibleTheme.colors.feedbackError
                                )
                            }
                        } else null
                    )
                }
            }
        }
    }
}
