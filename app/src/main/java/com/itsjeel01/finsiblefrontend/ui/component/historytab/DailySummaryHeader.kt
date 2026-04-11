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
import com.itsjeel01.finsiblefrontend.common.Currency
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.common.centisToFormattedAmount
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.model.DateFilterMode
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.expanded
import com.itsjeel01.finsiblefrontend.ui.theme.medium
import com.itsjeel01.finsiblefrontend.ui.theme.normal
import com.itsjeel01.finsiblefrontend.ui.theme.relaxed
import java.util.Locale.getDefault

@Composable
fun DailySummaryHeader(
    dateText: String,
    filterMode: DateFilterMode,
    incomeSumCentis: Long,
    expenseSumCentis: Long,
    netSumCentis: Long,
    onToggleFilter: () -> Unit,
    currencyFormatter: CurrencyFormatter,
    modifier: Modifier = Modifier
) {
    val displayAmountCentis = when (filterMode) {
        DateFilterMode.NET -> netSumCentis
        DateFilterMode.INCOME -> incomeSumCentis
        DateFilterMode.EXPENSE -> expenseSumCentis
    }

    val targetColor = when (filterMode) {
        DateFilterMode.NET -> FinsibleTheme.colors.secondaryContent
        DateFilterMode.INCOME -> FinsibleTheme.colors.income
        DateFilterMode.EXPENSE -> FinsibleTheme.colors.expense
    }

    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = Duration.MS_200.toInt()),
        label = "amount_color"
    )

    Row(
        modifier = modifier.padding(end = FinsibleTheme.dimes.d16),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FinsibleText(
            text = dateText.uppercase(getDefault()),
            variant = FinsibleTextVariant.SmallLabelSemiBold,
            color = FinsibleTheme.colors.tertiaryContent,
            textStyleOverride = FinsibleTheme.typography.t12.medium().expanded()
        )

        Row(
            modifier = Modifier
                .clickable(onClick = onToggleFilter)
                .padding(horizontal = FinsibleTheme.dimes.d4),
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FinsibleText(
                text = filterMode.name,
                textAlign = TextAlign.End,
                variant = FinsibleTextVariant.SmallLabelSemiBold,
                colorVariant = FinsibleTextColorVariant.Secondary,
                textStyleOverride = FinsibleTheme.typography.t12.normal().relaxed()
            )
            Icon(
                modifier = Modifier.size(FinsibleTheme.dimes.d14),
                painter = painterResource(R.drawable.ic_caret_up_down),
                tint = FinsibleTheme.colors.secondaryContent,
                contentDescription = stringResource(R.string.cd_change_view)
            )
            FinsibleText(
                text = formatAmount(displayAmountCentis, filterMode, currencyFormatter),
                variant = FinsibleTextVariant.SmallBodyRegular,
                color = animatedColor,
                textStyleOverride = FinsibleTheme.typography.t14.normal().relaxed()
            )
        }
    }
}

private fun formatAmount(
    amountCentis: Long,
    mode: DateFilterMode,
    currencyFormatter: CurrencyFormatter,
    currency: Currency = Currency.INR
): String {
    val absCentis = if (amountCentis < 0) -amountCentis else amountCentis
    val formattedAmount = absCentis.centisToFormattedAmount(currencyFormatter)
    return when (mode) {
        DateFilterMode.NET -> {
            val sign = if (amountCentis >= 0L) "+" else "-"
            "$sign ${currency.getSymbol()}$formattedAmount"
        }

        DateFilterMode.INCOME -> "+ ${currency.getSymbol()}$formattedAmount"
        DateFilterMode.EXPENSE -> "- ${currency.getSymbol()}$formattedAmount"
    }
}