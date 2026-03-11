package com.itsjeel01.finsiblefrontend.ui.component.historytab.filtersbottomsheet

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold

/** Label composable for sections. */
@Composable
fun SectionLabel(text: String) = Text(
    text = text,
    style = FinsibleTheme.typography.t10.semiBold(),
    color = FinsibleTheme.colors.tertiaryContent
)