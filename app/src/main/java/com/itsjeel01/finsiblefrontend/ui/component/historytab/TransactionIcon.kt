package com.itsjeel01.finsiblefrontend.ui.component.historytab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.util.resolveIcon

/** Icon representing transaction type/category. */
@Composable
fun TransactionIcon(
    type: TransactionType,
    categoryIcon: String,
    modifier: Modifier = Modifier
) {
    val iconRes = remember(categoryIcon) {
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