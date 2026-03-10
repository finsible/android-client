package com.itsjeel01.finsiblefrontend.ui.component.historytab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium

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
        Text(
            text = if (isFilterActive) stringResource(R.string.no_matching_transactions)
            else stringResource(R.string.no_transactions_yet),
            style = FinsibleTheme.typography.t16.medium(),
            color = FinsibleTheme.colors.secondaryContent
        )
        if (isFilterActive) {
            Spacer(Modifier.height(FinsibleTheme.dimes.d12))
            Text(
                text = stringResource(R.string.try_adjusting_filters),
                style = FinsibleTheme.typography.t14,
                color = FinsibleTheme.colors.tertiaryContent
            )
            Spacer(Modifier.height(FinsibleTheme.dimes.d16))
            Text(
                text = stringResource(R.string.clear_filters),
                style = FinsibleTheme.typography.t14.medium(),
                color = FinsibleTheme.colors.link,
                modifier = Modifier
                    .clickable(onClick = onClearFilters)
                    .padding(FinsibleTheme.dimes.d8)
            )
        }
    }
}