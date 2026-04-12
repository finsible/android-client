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
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.TransactionUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
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
                if (transaction.formattedDate.isNotBlank()) {
                    FinsibleText(
                        text = transaction.formattedDate,
                        variant = FinsibleTextVariant.MicroLabelMedium,
                        color = FinsibleTheme.colors.placeholder,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            FinsibleText(
                text = transaction.formattedAmount,
                variant = FinsibleTextVariant.BodyBold,
                color = when (transaction.type) {
                    TransactionType.INCOME -> FinsibleTheme.colors.income
                    else -> FinsibleTheme.colors.primaryContent80
                },
                textStyleOverride = FinsibleTheme.typography.t16.bold().relaxed(),
                modifier = Modifier.padding(top = FinsibleTheme.dimes.d2)
            )
        }
    }
}