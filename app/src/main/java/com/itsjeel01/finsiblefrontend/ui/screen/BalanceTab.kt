package com.itsjeel01.finsiblefrontend.ui.screen

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.AccountsCard
import com.itsjeel01.finsiblefrontend.ui.navigation.TabBackHandler
import com.itsjeel01.finsiblefrontend.ui.theme.CardGradientType
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleDimes.Companion.inverted
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleGradients
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.extraBold
import com.itsjeel01.finsiblefrontend.ui.viewmodel.BalanceViewModel

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun BalanceTab() {
    val viewModel: BalanceViewModel = hiltViewModel()
    var selectedTab by rememberSaveable { mutableStateOf(BalanceTabType.ACCOUNTS) }

    TabBackHandler()

    Scaffold {
        Column(
            Modifier
                .padding(horizontal = FinsibleTheme.dimes.d16, vertical = FinsibleTheme.dimes.d12)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Balance", style = FinsibleTheme.typography.t24.extraBold())

            Spacer(Modifier.height(FinsibleTheme.dimes.d16))

            BalanceTabSelector(
                selectedTab = selectedTab,
                onTabChange = { selectedTab = it },
                modifier = Modifier.fillMaxWidth()
            )

            when (selectedTab) {
                BalanceTabType.ACCOUNTS -> AccountsContent(viewModel)
                BalanceTabType.TRANSACTIONS -> TransactionsContent()
            }
        }
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

@Composable
private fun AccountsContent(viewModel: BalanceViewModel) {
    val accountCards by viewModel.accountCards.collectAsStateWithLifecycle()

    if (accountCards.isNotEmpty()) {
        Spacer(Modifier.height(FinsibleTheme.dimes.d16))

        // Pre-compute gradients in Composable context
        val gradients = accountCards.mapIndexed { index, _ ->
            val gradientType = when (index) {
                0 -> CardGradientType.NET_WORTH
                1 -> CardGradientType.ASSETS
                2 -> CardGradientType.LIABILITIES
                else -> CardGradientType.BRAND
            }
            FinsibleGradients.getLinearGradient(gradientType)
        }

        AccountsCard(
            cards = accountCards,
            gradients = gradients
        )
    }
}

@Composable
private fun TransactionsContent() {
    // TODO: Implement transactions list
    Text("Transactions content", modifier = Modifier.padding(FinsibleTheme.dimes.d16))
}


enum class BalanceTabType(
    val displayText: String,
    @DrawableRes val icon: Int
) {
    ACCOUNTS("Accounts", R.drawable.ic_piggy_bank),
    TRANSACTIONS("Transactions", R.drawable.ic_transactions)
}