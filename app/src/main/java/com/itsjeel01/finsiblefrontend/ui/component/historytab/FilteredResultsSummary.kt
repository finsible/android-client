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
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.data.repository.CurrencyRepository
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.model.FilteredTransactionSummary
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium

@Composable
fun FilteredResultsSummary(
    summary: FilteredTransactionSummary,
    currencyFormatter: CurrencyFormatter,
    currencyRepository: CurrencyRepository,
    modifier: Modifier = Modifier,
    defaultCurrencyCode: String,
) {
    val symbol = currencyRepository.getByIsoCode(defaultCurrencyCode)?.symbol ?: defaultCurrencyCode

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(FinsibleTheme.spacing.gapMd))
            .background(FinsibleTheme.colors.surfaceDefault)
            .border(
                width = FinsibleTheme.stroke.thin,
                color = FinsibleTheme.colors.borderDefault,
                shape = RoundedCornerShape(FinsibleTheme.spacing.gapMd)
            )
            .padding(horizontal = FinsibleTheme.spacing.insetLg, vertical = FinsibleTheme.spacing.gapMd),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FinsibleText(
            text = stringResource(R.string.transactions_count, summary.totalCount),
            textStyle = FinsibleTheme.typography.bodyMd.medium(),
            colorVariant = FinsibleTextColorVariant.Secondary
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.gapMd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FinsibleText(
                text = "+$symbol${
                    currencyFormatter.format(
                        centis = summary.totalIncomeCentis,
                        currencyCode = defaultCurrencyCode,
                        options = CurrencyFormatter.CurrencyFormatOptions(
                            includeCurrencySymbol = false,
                            includeSign = false
                        )
                    )
                }",
                textStyle = FinsibleTheme.typography.bodyMd.medium(),
                color = FinsibleTheme.colors.transactionIncome
            )
            FinsibleText(
                text = "-$symbol${
                    currencyFormatter.format(
                        centis = summary.totalExpenseCentis,
                        currencyCode = defaultCurrencyCode,
                        options = CurrencyFormatter.CurrencyFormatOptions(
                            includeCurrencySymbol = false,
                            includeSign = false
                        )
                    )
                }",
                textStyle = FinsibleTheme.typography.bodyMd.medium(),
                color = FinsibleTheme.colors.transactionExpense
            )
        }
    }
}