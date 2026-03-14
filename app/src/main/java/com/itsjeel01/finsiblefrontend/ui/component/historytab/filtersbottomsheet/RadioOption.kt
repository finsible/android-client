package com.itsjeel01.finsiblefrontend.ui.component.historytab.filtersbottomsheet

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleRadioButton

/** A single labelled radio option. */
@Composable
fun RadioOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FinsibleRadioButton(
        label = label,
        selected = selected,
        onClick = onClick,
        modifier = modifier
    )
}