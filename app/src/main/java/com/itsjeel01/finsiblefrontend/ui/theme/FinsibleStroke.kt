package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

/**
 * Layer 2 — Stroke / border-width tokens.
 *
 * Sub-pixel and thin stroke values are unscaled.
 * Semi-bold (1.5dp) and bold (2dp+) widths are scaled by [FinsibleUiScaler].
 */
@Immutable
data class FinsibleStroke(
    val hairline: Dp,   // 0.5dp — hairline borders, thin dividers
    val thin: Dp,        // 1dp — standard border, divider
    val semiBold: Dp,    // 1.5dp — emphasized border
    val bold: Dp,        // 2dp — strong border, progress indicators
    val heavy: Dp,       // 3dp — checkbox check mark
) {
    companion object {
        val values = FinsibleStroke(
            hairline = 0.5.dp,
            thin     = 1.dp,
            semiBold = 1.5.dp,
            bold     = 2.dp,
            heavy    = 3.dp,
        )
    }
}
