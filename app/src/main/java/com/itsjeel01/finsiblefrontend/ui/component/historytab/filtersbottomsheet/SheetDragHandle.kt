package com.itsjeel01.finsiblefrontend.ui.component.historytab.filtersbottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Composable function to draw a drag handle for the filters bottom sheet. */
@Composable
fun SheetDragHandle() {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(FinsibleTheme.dimes.d12))
        Box(
            Modifier
                .width(FinsibleTheme.dimes.d36)
                .height(FinsibleTheme.dimes.d4)
                .clip(RoundedCornerShape(FinsibleTheme.dimes.d2))
                .background(FinsibleTheme.colors.outlineVariant)
        )
        Spacer(Modifier.height(FinsibleTheme.dimes.d12))
    }
}