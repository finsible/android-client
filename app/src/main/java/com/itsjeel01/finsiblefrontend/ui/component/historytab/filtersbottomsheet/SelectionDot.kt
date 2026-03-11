package com.itsjeel01.finsiblefrontend.ui.component.historytab.filtersbottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Small dot shown before the currently selected item in dropdowns. */
@Composable
fun SelectionDot(visible: Boolean) {
    if (visible) {
        Box(
            Modifier
                .size(FinsibleTheme.dimes.d4)
                .clip(CircleShape)
                .background(FinsibleTheme.colors.primaryContent)
        )
    }
}