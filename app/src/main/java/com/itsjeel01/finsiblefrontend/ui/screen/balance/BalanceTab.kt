package com.itsjeel01.finsiblefrontend.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.navigation.Route
import com.itsjeel01.finsiblefrontend.ui.screen.balance.AccountsScreen
import com.itsjeel01.finsiblefrontend.ui.screen.balance.TransactionsScreen
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleDimes.Companion.inverted
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.extraBold
import com.itsjeel01.finsiblefrontend.ui.viewmodel.BalanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceTab(viewModel: BalanceViewModel) {

    // Hoist all state at the screen level
    val accountCards by viewModel.accountCards.collectAsStateWithLifecycle()
    val accountGroups by viewModel.accountGroups.collectAsStateWithLifecycle()
    val selectedGroupId by viewModel.selectedGroupId.collectAsStateWithLifecycle()
    val filteredAccounts by viewModel.filteredAccounts.collectAsStateWithLifecycle()

    val backStack = rememberNavBackStack(Route.Home.Balance.Accounts)
    val currentRoute = backStack.lastOrNull() ?: Route.Home.Balance.Accounts

    val selectedTab = when (currentRoute) {
        Route.Home.Balance.Accounts -> BalanceTabType.ACCOUNTS
        Route.Home.Balance.Transactions -> BalanceTabType.TRANSACTIONS
        else -> BalanceTabType.ACCOUNTS
    }

    Column(
        Modifier.padding(horizontal = FinsibleTheme.dimes.d16, vertical = FinsibleTheme.dimes.d12)
    ) {
        Text("Balance", style = FinsibleTheme.typography.t24.extraBold())

        Spacer(Modifier.height(FinsibleTheme.dimes.d16))

        BalanceTabSelector(
            selectedTab = selectedTab,
            onTabChange = { tab ->
                val targetRoute = when (tab) {
                    BalanceTabType.ACCOUNTS -> Route.Home.Balance.Accounts
                    BalanceTabType.TRANSACTIONS -> Route.Home.Balance.Transactions
                }
                if (currentRoute != targetRoute) {
                    backStack.clear()
                    backStack.add(targetRoute)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        NavDisplay(
            backStack = backStack,
            transitionSpec = {
                calculateBalanceTransition(initialState.key, targetState.key)
            },
            entryProvider = entryProvider {
                entry<Route.Home.Balance.Accounts> {
                    AccountsScreen(
                        flippableCards = accountCards,
                        accountGroups = accountGroups,
                        selectedGroupId = selectedGroupId,
                        filteredAccounts = filteredAccounts,
                        onGroupSelected = viewModel::selectGroupFilter
                    )
                }
                entry<Route.Home.Balance.Transactions> {
                    TransactionsScreen(viewModel)
                }
            }
        )
    }
}

@Composable
private fun BalanceTabSelector(
    selectedTab: BalanceTabType,
    onTabChange: (BalanceTabType) -> Unit,
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier,
        space = FinsibleTheme.dimes.d16.inverted()
    ) {
        BalanceTabType.entries.forEach { tab ->
            val isSelected = tab == selectedTab

            SegmentedButton(
                shape = RoundedCornerShape(FinsibleTheme.dimes.d12),
                onClick = { if (!isSelected) onTabChange(tab) },
                colors = SegmentedButtonDefaults.colors().copy(
                    activeContentColor = FinsibleTheme.colors.primaryContent,
                    activeContainerColor = FinsibleTheme.colors.primaryBackground,
                    inactiveContentColor = FinsibleTheme.colors.secondaryContent,
                    inactiveBorderColor = FinsibleTheme.colors.transparent,
                    activeBorderColor = FinsibleTheme.colors.primaryContent80,
                    inactiveContainerColor = FinsibleTheme.colors.secondaryBackground
                ),
                selected = isSelected,
                label = {
                    Text(
                        text = tab.displayText,
                        style = FinsibleTheme.typography.t16,
                        fontWeight = FontWeight.Medium
                    )
                },
                icon = {}
            )
        }
    }
}

enum class BalanceTabType(
    val displayText: String,
    @param:DrawableRes val icon: Int
) {
    ACCOUNTS("Accounts", R.drawable.ic_piggy_bank),
    TRANSACTIONS("Transactions", R.drawable.ic_transactions)
}

private fun calculateBalanceTransition(
    initialKey: Any?,
    targetKey: Any?
): ContentTransform {
    val tabOrder = listOf(Route.Home.Balance.Accounts, Route.Home.Balance.Transactions)

    val initialIndex = tabOrder.indexOfFirst { it == initialKey }
    val targetIndex = tabOrder.indexOfFirst { it == targetKey }

    val slideSpec = tween<IntOffset>(durationMillis = Duration.MS_300.toInt(), easing = FastOutSlowInEasing)

    return if (targetIndex > initialIndex) {
        slideInHorizontally(slideSpec) { width -> width } togetherWith slideOutHorizontally(slideSpec) { width -> -width }
    } else {
        slideInHorizontally(slideSpec) { width -> -width } togetherWith slideOutHorizontally(slideSpec) { width -> width }
    }
}

