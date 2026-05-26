package com.itsjeel01.finsiblefrontend.ui.component.historytab.filtersbottomsheet

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

object FilterSheetSpacing {
    val sectionHeaderGap: Dp
        @Composable get() = FinsibleTheme.spacing.inlineMd

    val sectionDividerPadding: Dp
        @Composable get() = FinsibleTheme.spacing.gapMd

    val headerToFirstSection: Dp
        @Composable get() = FinsibleTheme.spacing.insetXl

    val lastSectionToApplyButton: Dp
        @Composable get() = FinsibleTheme.spacing.insetXl
}

