package com.itsjeel01.finsiblefrontend.ui.component.historytab

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.ui.model.TransactionUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.itsjeel01.finsiblefrontend.ui.theme.medium
import com.itsjeel01.finsiblefrontend.ui.theme.relaxed

/** Standalone card for flat mode — always full radius, shows date. */
@Composable
fun FlatModeTransactionItem(transaction: TransactionUIModel) {
    val shape = RoundedCornerShape(FinsibleTheme.dimes.d16)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(FinsibleTheme.colors.surfaceContainerLow)
            .border(width = FinsibleTheme.dimes.d1, color = FinsibleTheme.colors.divider, shape = shape)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = FinsibleTheme.dimes.d16, vertical = FinsibleTheme.dimes.d10)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TransactionIcon(
                type = transaction.type,
                categoryIcon = transaction.categoryIcon,
                modifier = Modifier.padding(top = FinsibleTheme.dimes.d2)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d2)
            ) {
                Text(
                    text = transaction.title,
                    style = FinsibleTheme.typography.t16.medium(),
                    color = FinsibleTheme.colors.primaryContent,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = transaction.subtitle,
                    style = FinsibleTheme.typography.t12,
                    color = FinsibleTheme.colors.tertiaryContent,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (transaction.formattedDate.isNotBlank()) {
                    Text(
                        text = transaction.formattedDate,
                        style = FinsibleTheme.typography.t10,
                        color = FinsibleTheme.colors.placeholder,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Text(
                text = transaction.formattedAmount,
                style = FinsibleTheme.typography.t16.bold().relaxed(),
                color = when (transaction.type) {
                    TransactionType.INCOME -> FinsibleTheme.colors.income
                    else -> FinsibleTheme.colors.primaryContent80
                },
                modifier = Modifier.padding(top = FinsibleTheme.dimes.d2)
            )
        }
    }
}