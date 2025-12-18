package com.itsjeel01.finsiblefrontend.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.toLocaleCurrency
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountGroupEntity
import com.itsjeel01.finsiblefrontend.ui.component.FlippableCard
import com.itsjeel01.finsiblefrontend.ui.model.FlippableCardData
import com.itsjeel01.finsiblefrontend.ui.theme.CardGradientType
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleDimes.Companion.inverted
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleGradients
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.itsjeel01.finsiblefrontend.ui.theme.extraBold
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold
import com.itsjeel01.finsiblefrontend.ui.util.resolveIcon
import com.itsjeel01.finsiblefrontend.ui.viewmodel.BalanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceTab(viewModel: BalanceViewModel) {

    // Hoist all state at the screen level
    val accountCards by viewModel.accountCards.collectAsStateWithLifecycle()
    val accountGroups by viewModel.accountGroups.collectAsStateWithLifecycle()
    val selectedGroupId by viewModel.selectedGroupId.collectAsStateWithLifecycle()
    val filteredAccounts by viewModel.filteredAccounts.collectAsStateWithLifecycle()
    var selectedTab by rememberSaveable { mutableStateOf(BalanceTabType.ACCOUNTS) }


    Column(
        Modifier.padding(horizontal = FinsibleTheme.dimes.d16, vertical = FinsibleTheme.dimes.d12)
    ) {
        Text("Balance", style = FinsibleTheme.typography.t24.extraBold())

        Spacer(Modifier.height(FinsibleTheme.dimes.d16))

        BalanceTabSelector(
            selectedTab = selectedTab,
            onTabChange = { selectedTab = it },
            modifier = Modifier.fillMaxWidth()
        )

        when (selectedTab) {
            BalanceTabType.ACCOUNTS -> AccountsContent(
                flippableCards = accountCards,
                accountGroups = accountGroups,
                selectedGroupId = selectedGroupId,
                filteredAccounts = filteredAccounts,
                onGroupSelected = viewModel::selectGroupFilter
            )

            BalanceTabType.TRANSACTIONS -> TransactionsContent(viewModel)
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
private fun AccountsContent(
    flippableCards: List<FlippableCardData>,
    accountGroups: List<AccountGroupEntity>,
    selectedGroupId: Long?,
    filteredAccounts: List<AccountEntity>,
    onGroupSelected: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    if (flippableCards.isEmpty()) return

    // Pre-compute gradients in Composable context, cache with remember
    val gradients =
        flippableCards.mapIndexed { index, _ ->
            val gradientType = when (index) {
                0 -> CardGradientType.NET_WORTH
                1 -> CardGradientType.ASSETS
                2 -> CardGradientType.LIABILITIES
                else -> CardGradientType.BRAND
            }
            FinsibleGradients.getLinearGradient(gradientType)
        }

    // Flatten accounts into list items with headers
    val accountListItems = remember(filteredAccounts, selectedGroupId) {
        filteredAccounts
            .groupBy { account -> account.accountGroup.target?.name ?: "Others" }
            .flatMap { (groupName, accountsInGroup) ->
                buildList {
                    if (selectedGroupId == null) {
                        add(AccountListItem.Header(groupName))
                    }
                    accountsInGroup.forEach { account ->
                        add(AccountListItem.Account(account))
                    }
                }
            }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)
    ) {
        item(key = "accounts_card") {
            FlippableCard(
                items = flippableCards,
                gradients = gradients,
                modifier = Modifier.padding(top = FinsibleTheme.dimes.d16)
            )
        }

        stickyHeader(key = "filter_chips") {
            AccountGroupFilterRow(
                groups = accountGroups,
                selectedGroupId = selectedGroupId,
                onGroupSelected = onGroupSelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FinsibleTheme.colors.primaryBackground)
                    .padding(vertical = FinsibleTheme.dimes.d8)
            )
        }

        items(
            items = accountListItems,
            key = { item ->
                when (item) {
                    is AccountListItem.Header -> "header_${item.groupName}"
                    is AccountListItem.Account -> item.account.id
                }
            }
        ) { item ->
            when (item) {
                is AccountListItem.Header -> {
                    AccountGroupHeader(groupName = item.groupName)
                }

                is AccountListItem.Account -> {
                    AccountItem(account = item.account)
                    Spacer(Modifier.height(FinsibleTheme.dimes.d8))
                }
            }
        }

        item(key = "bottom_spacing") {
            Spacer(Modifier.height(FinsibleTheme.dimes.d80))
        }
    }
}

@Composable
private fun AccountGroupHeader(groupName: String) {
    Text(
        text = groupName.uppercase(),
        style = FinsibleTheme.typography.t12.semiBold(),
        color = FinsibleTheme.colors.secondaryContent,
        modifier = Modifier.padding(FinsibleTheme.dimes.d8)
    )
}

@Composable
private fun AccountGroupFilterRow(
    groups: List<AccountGroupEntity>,
    selectedGroupId: Long?,
    onGroupSelected: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)
    ) {
        item(key = "all") {
            AccountGroupFilterChip(
                text = "All",
                isSelected = selectedGroupId == null,
                onClick = { onGroupSelected(null) }
            )
        }

        items(items = groups, key = { it.id }) { group ->
            AccountGroupFilterChip(
                text = group.name,
                isSelected = selectedGroupId == group.id,
                onClick = { onGroupSelected(group.id) }
            )
        }
    }
}

@Composable
private fun AccountGroupFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        modifier = modifier,
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                style = FinsibleTheme.typography.t14,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        },
        shape = RoundedCornerShape(FinsibleTheme.dimes.d20),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = FinsibleTheme.colors.inverse,
            selectedLabelColor = FinsibleTheme.colors.same,
            containerColor = FinsibleTheme.colors.surfaceContainer,
            labelColor = FinsibleTheme.colors.primaryContent
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = FinsibleTheme.colors.border,
            selectedBorderColor = FinsibleTheme.colors.brandAccent,
            enabled = true,
            selected = isSelected
        )
    )
}

@Composable
private fun TransactionsContent(viewModel: BalanceViewModel) {
    /** TODO: Implement transactions UI. */
}

@Composable
private fun AccountItem(
    account: AccountEntity,
    modifier: Modifier = Modifier
) {
    val isPositiveBalance = account.balance.signum() >= 0
    val cornerRadius = FinsibleTheme.dimes.d12
    val borderWidth = FinsibleTheme.dimes.d4

    // Resolve account group color, fallback to income/expense based on balance
    val groupColorToken = account.accountGroup.target?.color
    val fallbackColor = if (isPositiveBalance) FinsibleTheme.colors.income else FinsibleTheme.colors.expense
    val borderColor = if (groupColorToken != null) {
        FinsibleTheme.resolveColor(groupColorToken, fallbackColor)
    } else {
        fallbackColor
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadius))
            .background(borderColor) // Border color as background
    ) {
        // Content container with left padding to create border effect
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = borderWidth) // This creates the "border"
                .background(
                    color = FinsibleTheme.colors.surfaceContainerLow,
                    shape = RoundedCornerShape(FinsibleTheme.dimes.d8)
                ) // Actual background
                .padding(
                    vertical = FinsibleTheme.dimes.d16,
                    horizontal = FinsibleTheme.dimes.d12
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Account icon
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(FinsibleTheme.dimes.d12))
                    .background(borderColor.copy(alpha = 0.2f))
                    .padding(FinsibleTheme.dimes.d12),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(resolveIcon(account.icon, R.drawable.ic_piggy_bank)),
                    contentDescription = "Account icon for ${account.name}",
                    modifier = Modifier.size(FinsibleTheme.dimes.d24),
                    tint = FinsibleTheme.colors.primaryContent,
                )
            }

            Spacer(Modifier.width(FinsibleTheme.dimes.d12))

            // Account details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = account.name,
                    style = FinsibleTheme.typography.t16.semiBold(),
                    color = FinsibleTheme.colors.primaryContent,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (account.description.isNotBlank()) {
                    Spacer(Modifier.height(FinsibleTheme.dimes.d2))
                    Text(
                        text = account.description,
                        style = FinsibleTheme.typography.t14,
                        color = FinsibleTheme.colors.secondaryContent,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.width(FinsibleTheme.dimes.d8))

            // Balance
            Text(
                text = account.balance.toLocaleCurrency(),
                style = FinsibleTheme.typography.t16.bold(),
                color = FinsibleTheme.colors.primaryContent,
            )
        }
    }
}


/** Sealed class for representing items in the accounts list. */
private sealed class AccountListItem {
    /** Header item for account group name. */
    data class Header(val groupName: String) : AccountListItem()

    /** Account item. */
    data class Account(val account: AccountEntity) : AccountListItem()
}

enum class BalanceTabType(
    val displayText: String,
    @param:DrawableRes val icon: Int
) {
    ACCOUNTS("Accounts", R.drawable.ic_piggy_bank),
    TRANSACTIONS("Transactions", R.drawable.ic_transactions)
}