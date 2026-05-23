package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

/**
 * Layer 2 — Spacing tokens.
 *
 * Four roles cover every layout scenario:
 * - **inset** — internal padding of a component (distance from edge to content)
 * - **stack** — vertical rhythm between elements
 * - **gap**   — horizontal rhythm between elements in a row
 * - **inline** — space between an icon and its adjacent label
 *
 * Each role has five sizes: micro (2dp), xs (4dp) … 3xl (32dp).
 * Sub-pixel values are unscaled; the [FinsibleUiScaler] applies scaling.
 */
@Immutable
data class FinsibleSpacing(
    // ── Inset — internal padding ────────────────────────────────────────
    val insetNone: Dp,  // 0dp — removes padding
    val insetMicro: Dp, // 2dp — tight badge/chip inner padding
    val insetXs: Dp,    // 4dp — dense chip/badge padding
    val insetSm: Dp,    // 8dp — compact chip padding
    val insetMd: Dp,    // 12dp — compact list row padding
    val insetLg: Dp,    // 16dp — standard card / section padding
    val insetXl: Dp,    // 20dp — generous card padding
    val inset2xl: Dp,   // 24dp — screen-level edge padding
    val inset3xl: Dp,   // 32dp — header / hero padding

    // ── Stack — vertical rhythm ─────────────────────────────────────────
    val stackMicro: Dp, // 2dp  — tight label-to-value gap
    val stackXs: Dp,    // 4dp  — icon–label vertical gap
    val stackSm: Dp,    // 8dp  — related items, tight groups
    val stackMd: Dp,    // 12dp — standard vertical rhythm
    val stackLg: Dp,    // 16dp — section content groups
    val stackXl: Dp,    // 24dp — between sections
    val stack2xl: Dp,   // 32dp — screen-level sections

    // ── Gap — horizontal rhythm ─────────────────────────────────────────
    val gapMicro: Dp,   // 2dp  — tight icon-to-label inline gap
    val gapXs: Dp,      // 4dp  — icon–text inline gap
    val gapSm: Dp,      // 8dp  — chips in a row
    val gapMd: Dp,      // 12dp — standard row gaps
    val gapLg: Dp,      // 16dp — card grid gap

    // ── Inline — icon-to-label spacing ──────────────────────────────────
    val inlineXs: Dp,   // 4dp
    val inlineSm: Dp,   // 6dp
    val inlineMd: Dp,   // 8dp
) {
    companion object {
        val values = FinsibleSpacing(
            insetNone = 0.dp,
            insetMicro = 2.dp,
            insetXs    = 4.dp,
            insetSm    = 8.dp,
            insetMd    = 12.dp,
            insetLg    = 16.dp,
            insetXl    = 20.dp,
            inset2xl   = 24.dp,
            inset3xl   = 32.dp,

            stackMicro = 2.dp,
            stackXs    = 4.dp,
            stackSm    = 8.dp,
            stackMd    = 12.dp,
            stackLg    = 16.dp,
            stackXl    = 24.dp,
            stack2xl   = 32.dp,

            gapMicro = 2.dp,
            gapXs    = 4.dp,
            gapSm    = 8.dp,
            gapMd    = 12.dp,
            gapLg    = 16.dp,

            inlineXs = 4.dp,
            inlineSm = 6.dp,
            inlineMd = 8.dp,
        )
    }
}
