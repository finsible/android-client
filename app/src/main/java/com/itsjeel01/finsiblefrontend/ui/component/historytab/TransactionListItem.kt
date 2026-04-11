package com.itsjeel01.finsiblefrontend.ui.component.historytab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.TransactionUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.itsjeel01.finsiblefrontend.ui.theme.relaxed

/** Transaction row for grouped mode — icon aligned to title row, no date line. */
@Composable
fun TransactionListItem(
    transaction: TransactionUIModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = FinsibleTheme.dimes.d16),
        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TransactionIcon(type = transaction.type, categoryIcon = transaction.categoryIcon)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d2)
        ) {
            FinsibleText(
                text = transaction.title,
                variant = FinsibleTextVariant.BodyMedium,
                colorVariant = FinsibleTextColorVariant.Primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            FinsibleText(
                text = transaction.subtitle,
                variant = FinsibleTextVariant.SmallLabelRegular,
                color = FinsibleTheme.colors.tertiaryContent,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        FinsibleText(
            text = transaction.formattedAmount,
            variant = FinsibleTextVariant.BodyBold,
            color = when (transaction.type) {
                TransactionType.INCOME -> FinsibleTheme.colors.income
                else -> FinsibleTheme.colors.primaryContent80
            },
            textStyleOverride = FinsibleTheme.typography.t16.bold().relaxed()
        )
    }
}