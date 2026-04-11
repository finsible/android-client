package com.itsjeel01.finsiblefrontend.ui.component.historytab

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.Currency
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.common.centisToFormattedAmount
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.model.FilteredTransactionSummary
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun FilteredResultsSummary(
    summary: FilteredTransactionSummary,
    currencyFormatter: CurrencyFormatter,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(FinsibleTheme.dimes.d12))
            .background(FinsibleTheme.colors.surfaceContainerLow)
            .border(
                width = FinsibleTheme.dimes.d1,
                color = FinsibleTheme.colors.border,
                shape = RoundedCornerShape(FinsibleTheme.dimes.d12)
            )
            .padding(horizontal = FinsibleTheme.dimes.d16, vertical = FinsibleTheme.dimes.d12),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FinsibleText(
            text = stringResource(R.string.transactions_count, summary.totalCount),
            variant = FinsibleTextVariant.SmallBodyMedium,
            colorVariant = FinsibleTextColorVariant.Secondary
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FinsibleText(
                text = "+${Currency.INR.getSymbol()}${summary.totalIncomeCentis.centisToFormattedAmount(currencyFormatter)}",
                variant = FinsibleTextVariant.SmallBodyMedium,
                color = FinsibleTheme.colors.income
            )
            FinsibleText(
                text = "-${Currency.INR.getSymbol()}${summary.totalExpenseCentis.centisToFormattedAmount(currencyFormatter)}",
                variant = FinsibleTextVariant.SmallBodyMedium,
                color = FinsibleTheme.colors.expense
            )
        }
    }
}