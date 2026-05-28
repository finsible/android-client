package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.AccountUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Transfer account row with two equal-width FROM/TO cards, swap button, and mutual exclusion. */
@Composable
fun TransferAccountRow(
    fromAccounts: List<AccountUIModel>,
    toAccounts: List<AccountUIModel>,
    selectedFromId: Long?,
    selectedToId: Long?,
    onFromSelected: (Long) -> Unit,
    onToSelected: (Long) -> Unit,
    accentColor: Color,
    fromExpanded: Boolean,
    toExpanded: Boolean,
    onFromExpandedChange: (Boolean) -> Unit,
    onToExpandedChange: (Boolean) -> Unit,
    onOpenFromAll: () -> Unit,
    onOpenToAll: () -> Unit,
    modifier: Modifier = Modifier,
    onError: (@Composable () -> Unit)? = null
) {
    val selectedFromItem = remember(fromAccounts, selectedFromId) {
        fromAccounts.find { it.id == selectedFromId }
    }
    val selectedToItem = remember(toAccounts, selectedToId) {
        toAccounts.find { it.id == selectedToId }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.inlineMd)
    ) {
        // FROM Section
        Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackSm)) {
            AccountSectionLabelRow(
                label = "From",
                onOpenAll = onOpenFromAll,
                accentColor = accentColor,
                hint = null
            )
            ExpenseIncomeAccountRow(
                shortlistItems = fromAccounts,
                selectedItem = selectedFromItem,
                isExpanded = fromExpanded,
                onExpandedChange = onFromExpandedChange,
                onItemSelected = { onFromSelected(it.id) },
                accentColor = accentColor
            )
        }

        // TO Section
        Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackSm)) {
            AccountSectionLabelRow(
                label = "To",
                onOpenAll = onOpenToAll,
                accentColor = accentColor,
                hint = null
            )
            ExpenseIncomeAccountRow(
                shortlistItems = toAccounts,
                selectedItem = selectedToItem,
                isExpanded = toExpanded,
                onExpandedChange = onToExpandedChange,
                onItemSelected = { onToSelected(it.id) },
                accentColor = accentColor
            )
        }

        if (onError != null) {
            onError()
        }
    }
}
