
package com.itsjeel01.finsiblefrontend.ui.component.historytab.filtersbottomsheet
import com.itsjeel01.finsiblefrontend.ui.theme.bold

import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant

@Composable
fun SheetHeader(hasAnyActive: Boolean, onClearAll: () -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FinsibleText(
            text = stringResource(R.string.filters_and_sort),
            textStyle = FinsibleTheme.typography.bodyLg.bold()
        )

        FinsibleButton(
            onClick = onClearAll,
            text = stringResource(R.string.clear_all),
            variant = FinsibleButtonVariant.Text,
            size = FinsibleSize.Small,
            enabled = hasAnyActive,
        )
    }
}