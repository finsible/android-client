package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Layer 2 — Radius tokens.
 *
 * Every component derives its corner radius from these semantic tokens.
 * No component hardcodes a raw dp radius.
 *
 * Accessed via [FinsibleTheme.radius].
 */
@Immutable
data class FinsibleRadius(
    /** No rounding. */
    val none: Dp,
    /** Extra small — chips, tags, badges, status pills. */
    val xs: Dp,
    /** Small — buttons, input fields, small cards. */
    val sm: Dp,
    /** Medium — standard cards, list containers. */
    val md: Dp,
    /** Large — financial cards, modals, sheets. */
    val lg: Dp,
    /** Extra large — hero cards, prominent bottom sheets. */
    val xl: Dp,
    /** Fully rounded — FAB, pill chips, badges. */
    val pill: Dp,
    /** Component-specific radius recommendations. */
    val component: FinsibleComponentRadii,
) {
    companion object {
        /** Base (unscaled) radius values. */
        val values = FinsibleRadius(
            none = 0.dp,
            xs   = 4.dp,
            sm   = 8.dp,
            md   = 12.dp,
            lg   = 16.dp,
            xl   = 20.dp,
            pill = 999.dp,
            component = FinsibleComponentRadii(
                button      = 8.dp,
                input       = 8.dp,
                card        = 12.dp,
                cardFinance = 16.dp,
                sheet       = 20.dp,
                chip        = 4.dp,
                chipPill    = 999.dp,
                fab         = 999.dp,
                badge       = 999.dp,
                snackbar    = 8.dp,
                dialog      = 16.dp,
            ),
        )

        // ── Static backward-compatible accessors ────────────────────────
        val none get() = values.none
        val xs get() = values.xs
        val sm get() = values.sm
        val md get() = values.md
        val lg get() = values.lg
        val xl get() = values.xl
        val pill get() = values.pill
        val Component get() = values.component
    }
}

/**
 * Recommended radius per component type.
 *
 * These map semantic roles to specific component shapes.
 * Accessed via [FinsibleRadius.component].
 */
@Immutable
data class FinsibleComponentRadii(
    val button: Dp,
    val input: Dp,
    val card: Dp,
    val cardFinance: Dp,
    val sheet: Dp,
    val chip: Dp,
    val chipPill: Dp,
    val fab: Dp,
    val badge: Dp,
    val snackbar: Dp,
    val dialog: Dp,
)
