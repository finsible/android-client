package com.itsjeel01.finsiblefrontend.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.ui.model.TransactionUiModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.itsjeel01.finsiblefrontend.ui.theme.medium
import com.itsjeel01.finsiblefrontend.ui.theme.relaxed
import com.itsjeel01.finsiblefrontend.ui.util.resolveIcon

@Composable
fun TransactionListItem(
    transaction: TransactionUiModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = FinsibleTheme.dimes.d16),
        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon/Category indicator
        TransactionIcon(
            type = transaction.type,
            categoryIcon = transaction.categoryIcon
        )

        // Transaction details
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4)
        ) {
            Text(
                text = transaction.title,
                style = FinsibleTheme.typography.t16.medium(),
                color = FinsibleTheme.colors.primaryContent,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = transaction.subtitle,
                    style = FinsibleTheme.typography.t12,
                    color = FinsibleTheme.colors.tertiaryContent,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
            }
        }

        // Amount
        Text(
            text = transaction.formattedAmount,
            style = FinsibleTheme.typography.t16.bold().relaxed(),
            color = when (transaction.type) {
                TransactionType.INCOME -> FinsibleTheme.colors.income
                else -> FinsibleTheme.colors.primaryContent80
            }
        )
    }
}

/** Icon representing transaction type/category. */
@Composable
private fun TransactionIcon(
    type: TransactionType,
    categoryIcon: String,
    modifier: Modifier = Modifier
) {
    // Keep remember here as resolveIcon might involve resource lookups
    val iconRes = remember(categoryIcon, type) {
        resolveIcon(
            token = categoryIcon.ifBlank { null },
            fallbackIcon = type.icon
        )
    }

    Box(
        modifier = modifier
            .size(FinsibleTheme.dimes.d40)
            .clip(CircleShape)
            .background(
                when (type) {
                    TransactionType.INCOME -> FinsibleTheme.colors.income.copy(alpha = 0.1F)
                    TransactionType.EXPENSE -> FinsibleTheme.colors.expense.copy(alpha = 0.1F)
                    TransactionType.TRANSFER -> FinsibleTheme.colors.transfer.copy(alpha = 0.1F)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(FinsibleTheme.dimes.d20),
            tint = when (type) {
                TransactionType.INCOME -> FinsibleTheme.colors.income
                TransactionType.EXPENSE -> FinsibleTheme.colors.expense
                TransactionType.TRANSFER -> FinsibleTheme.colors.transfer
            }
        )
    }
}