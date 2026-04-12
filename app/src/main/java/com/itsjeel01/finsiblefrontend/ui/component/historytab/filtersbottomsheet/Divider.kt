package com.itsjeel01.finsiblefrontend.ui.component.historytab.filtersbottomsheet

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Divider between sections. */
@Composable
fun SheetDivider() =
    HorizontalDivider(
        modifier = Modifier.padding(vertical = FilterSheetSpacing.sectionDividerPadding),
        color = FinsibleTheme.colors.divider,
        thickness = FinsibleTheme.dimes.d0dot5
    )