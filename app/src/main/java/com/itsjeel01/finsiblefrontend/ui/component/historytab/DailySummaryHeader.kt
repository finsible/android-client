package com.itsjeel01.finsiblefrontend.ui.component.historytab

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.data.repository.CurrencyRepository
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleDurations
import com.itsjeel01.finsiblefrontend.ui.model.DateFilterMode
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.expanded
import com.itsjeel01.finsiblefrontend.ui.theme.medium
import com.itsjeel01.finsiblefrontend.ui.theme.relaxed
import com.itsjeel01.finsiblefrontend.ui.util.DateUtils
import java.util.Locale.getDefault

@Composable
fun DailySummaryHeader(
    timestampMs: Long,
    filterMode: DateFilterMode,
    incomeSumCentis: Long,
    expenseSumCentis: Long,
    netSumCentis: Long,
    onToggleFilter: () -> Unit,
    currencyFormatter: CurrencyFormatter,
    currencyRepository: CurrencyRepository,
    currencyCode: String,
    modifier: Modifier = Modifier
) {
    val todayLabel = stringResource(R.string.history_date_header_today)
    val yesterdayLabel = stringResource(R.string.history_date_header_yesterday)

    val dateText = DateUtils.formatDateHeader(timestampMs, todayLabel, yesterdayLabel)

    val displayAmountCentis = when (filterMode) {
        DateFilterMode.NET -> netSumCentis
        DateFilterMode.INCOME -> incomeSumCentis
        DateFilterMode.EXPENSE -> expenseSumCentis
    }

    val targetColor = when (filterMode) {
        DateFilterMode.NET -> FinsibleTheme.colors.contentSecondary
        DateFilterMode.INCOME -> FinsibleTheme.colors.transactionIncome
        DateFilterMode.EXPENSE -> FinsibleTheme.colors.transactionExpense
    }

    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = FinsibleDurations.values.slideMs),
        label = "amount_color"
    )

    Row(
        modifier = modifier.padding(end = FinsibleTheme.spacing.insetLg),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FinsibleText(
            text = dateText.uppercase(getDefault()),
            color = FinsibleTheme.colors.contentTertiary,
            textStyle = FinsibleTheme.typography.bodySm.medium().expanded()
        )

        Row(
            modifier = Modifier
                .clickable(onClick = onToggleFilter)
                .padding(horizontal = FinsibleTheme.spacing.insetXs),
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.gapMicro),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FinsibleText(
                text = filterMode.name,
                textAlign = TextAlign.End,
                textStyle = FinsibleTheme.typography.bodySm.relaxed(),
                colorVariant = FinsibleTextColorVariant.Secondary,
            )
            Icon(
                modifier = Modifier.size(FinsibleTheme.spacing.insetLg),
                painter = painterResource(R.drawable.ic_caret_up_down),
                tint = FinsibleTheme.colors.contentSecondary,
                contentDescription = stringResource(R.string.cd_change_view)
            )
            FinsibleText(
                text = formatAmount(displayAmountCentis, filterMode, currencyFormatter, currencyRepository, currencyCode),
                textStyle = FinsibleTheme.typography.bodyMd.relaxed(),
                color = animatedColor,
            )
        }
    }
}

private fun formatAmount(
    amountCentis: Long,
    mode: DateFilterMode,
    currencyFormatter: CurrencyFormatter,
    currencyRepository: CurrencyRepository,
    currencyCode: String
): String {
    val absCentis = if (amountCentis < 0) -amountCentis else amountCentis
    val formattedAmount = currencyFormatter.format(
        centis = absCentis,
        currencyCode = currencyCode,
        options = CurrencyFormatter.CurrencyFormatOptions(
            includeCurrencySymbol = false,
            includeSign = false
        )
    )
    val symbol = currencyRepository.getByIsoCode(currencyCode)?.symbol ?: currencyCode
    return when (mode) {
        DateFilterMode.NET -> {
            val sign = if (amountCentis >= 0L) "+" else "-"
            "$sign $symbol$formattedAmount"
        }

        DateFilterMode.INCOME -> "+ $symbol$formattedAmount"
        DateFilterMode.EXPENSE -> "- $symbol$formattedAmount"
    }
}