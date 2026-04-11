package com.itsjeel01.finsiblefrontend.ui.component.historytab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun TransactionEmptyContent(
    isFilterActive: Boolean,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        FinsibleText(
            text = if (isFilterActive) stringResource(R.string.no_matching_transactions)
            else stringResource(R.string.no_transactions_yet),
            variant = FinsibleTextVariant.BodyMedium,
            colorVariant = FinsibleTextColorVariant.Secondary
        )
        if (isFilterActive) {
            Spacer(Modifier.height(FinsibleTheme.dimes.d12))
            FinsibleText(
                text = stringResource(R.string.try_adjusting_filters),
                variant = FinsibleTextVariant.SmallBodyRegular,
                color = FinsibleTheme.colors.tertiaryContent
            )
            Spacer(Modifier.height(FinsibleTheme.dimes.d16))
            FinsibleText(
                text = stringResource(R.string.clear_filters),
                variant = FinsibleTextVariant.SmallBodyMedium,
                color = FinsibleTheme.colors.link,
                modifier = Modifier
                    .clickable(onClick = onClearFilters)
                    .padding(FinsibleTheme.dimes.d8)
            )
        }
    }
}