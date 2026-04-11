package com.itsjeel01.finsiblefrontend.ui.component.historytab.filtersbottomsheet

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Shared spacing contract for filter-sheet section rhythm. */
object FilterSheetSpacing {
    val sectionHeaderGap: Dp
        @Composable get() = FinsibleTheme.dimes.d8

    val sectionDividerPadding: Dp
        @Composable get() = FinsibleTheme.dimes.d12

    val headerToFirstSection: Dp
        @Composable get() = FinsibleTheme.dimes.d20

    val lastSectionToApplyButton: Dp
        @Composable get() = FinsibleTheme.dimes.d20
}

