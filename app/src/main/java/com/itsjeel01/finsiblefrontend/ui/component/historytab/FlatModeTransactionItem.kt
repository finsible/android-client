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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.TransactionUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.itsjeel01.finsiblefrontend.ui.theme.relaxed
import com.itsjeel01.finsiblefrontend.ui.util.DateUtils

/** Standalone card for flat mode — always full radius, shows date. */
@Composable
fun FlatModeTransactionItem(transaction: TransactionUIModel) {
    val shape = RoundedCornerShape(FinsibleTheme.spacing.insetLg)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(FinsibleTheme.colors.surfaceDefault)
            .border(width = FinsibleTheme.stroke.thin, color = FinsibleTheme.colors.borderSubtle, shape = shape)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = FinsibleTheme.spacing.insetLg, vertical = FinsibleTheme.spacing.inlineMd)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.gapMd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TransactionIcon(
                type = transaction.type,
                categoryIcon = transaction.categoryIcon,
                modifier = Modifier.padding(top = FinsibleTheme.spacing.insetMicro)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackMicro)
            ) {
                FinsibleText(
                    text = transaction.title,
                    textStyle = FinsibleTheme.typography.bodyLg.medium(),
                    colorVariant = FinsibleTextColorVariant.Primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                FinsibleText(
                    text = transaction.subtitle,
                    textStyle = FinsibleTheme.typography.bodySm,
                    color = FinsibleTheme.colors.contentTertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val formattedDate = DateUtils.readableDate(transaction.transactionDate)
                FinsibleText(
                    text = formattedDate,
                    textStyle = FinsibleTheme.typography.labelSm.medium(),
                    color = FinsibleTheme.colors.contentPlaceholder,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            FinsibleText(
                text = transaction.formattedAmount,
                color = when (transaction.type) {
                    TransactionType.INCOME -> FinsibleTheme.colors.transactionIncome
                    else -> FinsibleTheme.colors.contentSecondary
                },
                textStyle = FinsibleTheme.typography.bodyLg.bold().relaxed(),
                modifier = Modifier.padding(top = FinsibleTheme.spacing.insetMicro)
            )
        }
    }
}