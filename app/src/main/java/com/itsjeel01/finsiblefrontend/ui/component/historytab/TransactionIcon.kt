package com.itsjeel01.finsiblefrontend.ui.component.historytab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleIconBadge
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

    val tint = when (type) {
        TransactionType.INCOME -> FinsibleTheme.colors.income
        TransactionType.EXPENSE -> FinsibleTheme.colors.expense
        TransactionType.TRANSFER -> FinsibleTheme.colors.transfer
    }

    FinsibleIconBadge(
        icon = iconRes,
        tint = tint,
        modifier = modifier
    )
}