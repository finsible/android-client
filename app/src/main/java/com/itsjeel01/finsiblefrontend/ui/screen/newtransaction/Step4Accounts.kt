@file:OptIn(ExperimentalMaterial3Api::class)

package com.itsjeel01.finsiblefrontend.ui.screen.newtransaction

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.extraBold
import com.itsjeel01.finsiblefrontend.ui.theme.medium
import com.itsjeel01.finsiblefrontend.ui.util.resolveIcon
import com.itsjeel01.finsiblefrontend.ui.viewmodel.NewTransactionViewModel

/** Stateless account selection step with hoisted state. */
@Composable
fun Step4Accounts(
    transactionType: TransactionType,
    accounts: List<AccountEntity>,
    fromAccountId: Long?,
    toAccountId: Long?,
    onFromAccountSelected: (Long) -> Unit,
    onToAccountSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(vertical = FinsibleTheme.dimes.d8),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d16)
    ) {
        if (transactionType != TransactionType.INCOME) {
            AccountSelector(
                title = "From Account",
                description = when (transactionType) {
                    TransactionType.EXPENSE -> "Select where the money will be spent from"
                    TransactionType.TRANSFER -> "Select the source account"
                    else -> ""
                },
                accounts = accounts,
                selectedAccountId = fromAccountId,
                accentColor = transactionType.getColor(),
                onAccountSelected = onFromAccountSelected
            )
        }
        if (transactionType != TransactionType.EXPENSE) {
            AccountSelector(
                title = "To Account",
                description = when (transactionType) {
                    TransactionType.INCOME -> "Select where the money will be received"
                    TransactionType.TRANSFER -> "Select the destination account"
                    else -> ""
                },
                accounts = accounts.filter { transactionType != TransactionType.TRANSFER || it.id != fromAccountId },
                selectedAccountId = toAccountId,
                accentColor = transactionType.getColor(),
                onAccountSelected = onToAccountSelected
            )
        }
    }
}

/** ViewModel wrapper preserving original signature. */
@Composable
fun Step4Accounts(viewModel: NewTransactionViewModel) {
    val transactionType = viewModel.transactionType.collectAsStateWithLifecycle().value
    val fromAccountId = viewModel.transactionFromAccountId.collectAsStateWithLifecycle().value
    val toAccountId = viewModel.transactionToAccountId.collectAsStateWithLifecycle().value
    val accounts = viewModel.accounts.collectAsStateWithLifecycle().value
    Step4Accounts(
        transactionType = transactionType,
        accounts = accounts,
        fromAccountId = fromAccountId,
        toAccountId = toAccountId,
        onFromAccountSelected = { viewModel.setTransactionFromAccountId(it) },
        onToAccountSelected = { viewModel.setTransactionToAccountId(it) }
    )
}

/** Account selector section with title, description, and account chips. */
@Composable
private fun AccountSelector(
    title: String,
    description: String,
    accounts: List<AccountEntity>,
    selectedAccountId: Long?,
    accentColor: Color,
    onAccountSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(FinsibleTheme.colors.surfaceContainer, RoundedCornerShape(FinsibleTheme.dimes.d12))
            .padding(FinsibleTheme.dimes.d16),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12)
    ) {
        // Section header
        Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4)) {
            Text(
                text = title,
                style = FinsibleTheme.typography.t18.extraBold(),
                color = FinsibleTheme.colors.primaryContent
            )
            if (description.isNotEmpty()) {
                Text(
                    text = description,
                    style = FinsibleTheme.typography.t14,
                    color = FinsibleTheme.colors.secondaryContent
                )
            }
        }

        // Account chips
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8),
            verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)
        ) {
            for (acc in accounts) {
                AccountChip(
                    account = acc,
                    isSelected = selectedAccountId == acc.id,
                    accentColor = accentColor,
                    onSelected = { onAccountSelected(acc.id) }
                )
            }
        }
    }
}

/** Individual account chip with animated selection state. */
@Composable
private fun AccountChip(
    account: AccountEntity,
    isSelected: Boolean,
    accentColor: Color,
    onSelected: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) accentColor else FinsibleTheme.colors.border,
        animationSpec = tween(durationMillis = 200),
        label = "borderColor"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) accentColor.copy(alpha = 0.15f) else FinsibleTheme.colors.transparent,
        animationSpec = tween(durationMillis = 200),
        label = "backgroundColor"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(FinsibleTheme.dimes.d8))
            .border(
                color = borderColor,
                width = FinsibleTheme.dimes.d1,
                shape = RoundedCornerShape(FinsibleTheme.dimes.d8)
            )
            .background(backgroundColor)
            .clickable(onClick = onSelected)
            .padding(
                horizontal = FinsibleTheme.dimes.d12,
                vertical = FinsibleTheme.dimes.d10
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d6)
        ) {
            val iconDrawable = resolveIcon(account.icon, fallbackIcon = R.drawable.ic_piggy_bank)
            Icon(
                modifier = Modifier.size(FinsibleTheme.dimes.d20),
                painter = painterResource(id = iconDrawable),
                contentDescription = null,
                tint = if (isSelected) accentColor else FinsibleTheme.colors.primaryContent60
            )
            Text(
                text = account.name,
                style = FinsibleTheme.typography.t16.medium(),
                color = if (isSelected) FinsibleTheme.colors.primaryContent else FinsibleTheme.colors.primaryContent80
            )
        }
    }
}