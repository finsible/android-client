package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleIconButton
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleIconButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold

@Composable
fun NewTransactionHeader(onClose: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        FinsibleIconButton(
            icon = R.drawable.ic_close,
            onClick = onClose,
            modifier = Modifier.align(Alignment.CenterStart),
            contentDescription = stringResource(R.string.cd_close),
            colors = FinsibleIconButtonDefaults.tertiaryColors(),
            sizes = FinsibleIconButtonDefaults.largeSizes()
        )
        Text(
            stringResource(R.string.new_transaction),
            style = FinsibleTheme.typography.t20.bold(),
            color = FinsibleTheme.colors.primaryContent,
            textAlign = TextAlign.Center,
        )
    }
}