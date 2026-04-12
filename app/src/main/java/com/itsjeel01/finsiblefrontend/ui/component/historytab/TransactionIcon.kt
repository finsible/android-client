package com.itsjeel01.finsiblefrontend.ui.component.historytab

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleIconBadge
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleIconBadgeDefaults
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
        modifier = modifier,
        icon = {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null
            )
        },
        size = FinsibleSize.Medium,
        shapeVariant = FinsibleShape.Circle,
        colors = FinsibleIconBadgeDefaults.colors(
            iconTint = tint,
            backgroundTint = tint
        ),
        backgroundAlpha = 0.1f
    )
}