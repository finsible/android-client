package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Layer 2 — Elevation and shadow tokens.
 *
 * Four elevation levels. In light mode, elevation is conveyed via shadow;
 * in dark mode, via surface tint (lighter fill = higher elevation).
 * Shadow specs have [color] set to [Color.Unspecified] in dark mode when
 * the shadow should not render (use [FinsibleDropShadow.None] for that).
 *
 * All offset and blur values are derived from the design token system:
 * - [FinsibleSizes] shadow sizes — semantic elevation sizes
 *
 * Access via [FinsibleTheme.elevation].
 */
@Immutable
data class FinsibleElevation(
    /** Flat / zero-depth surfaces (no shadow). */
    val flatShadow: FinsibleDropShadow,
    /** Slightly raised — cards, list items. */
    val raisedShadow: FinsibleDropShadow,
    /** Floating — FABs, dropdowns, popovers. */
    val floatingShadow: FinsibleDropShadow,
    /** Modal — dialogs, bottom sheets. */
    val modalShadow: FinsibleDropShadow,
    /** Focus / active ring (coloured glow, no blur). */
    val focusShadow: FinsibleDropShadow,

    /** Surface colour for flat / zero-depth surfaces. */
    val flatSurface: Color,
    /** Surface colour for slightly raised elements. */
    val raisedSurface: Color,
    /** Surface colour for floating elements. */
    val floatingSurface: Color,
    /** Surface colour for modal elements. */
    val modalSurface: Color,
) {
    companion object {
        fun light(sizes: FinsibleSizeTokens): FinsibleElevation = FinsibleElevation(
            flatShadow = FinsibleDropShadow.None,
            raisedShadow = FinsibleDropShadow(
                elevation = sizes.shadow.raised,
                color = Color(0x12000000)
            ),
            floatingShadow = FinsibleDropShadow(
                elevation = sizes.shadow.floating,
                color = Color(0x1A000000)
            ),
            modalShadow = FinsibleDropShadow(
                elevation = sizes.shadow.modal,
                color = Color(0x24000000)
            ),
            focusShadow = FinsibleDropShadow(
                elevation = sizes.shadow.focus,
                color = Color(0x992E8B57)
            ),

            flatSurface = Color(0xFFEFEFF2),
            raisedSurface = Color(0xFFFFFFFF),
            floatingSurface = Color(0xFFFFFFFF),
            modalSurface = Color(0xFFFFFFFF),
        )

        fun dark(sizes: FinsibleSizeTokens): FinsibleElevation = FinsibleElevation(
            flatShadow = FinsibleDropShadow.None,
            raisedShadow = FinsibleDropShadow(
                elevation = sizes.shadow.raised,
                color = Color(0x14000000)
            ),
            floatingShadow = FinsibleDropShadow(
                elevation = sizes.shadow.floating,
                color = Color(0x1F000000)
            ),
            modalShadow = FinsibleDropShadow(
                elevation = sizes.shadow.modal,
                color = Color(0x26000000)
            ),
            focusShadow = FinsibleDropShadow(
                elevation = sizes.shadow.focus,
                color = Color(0x5934A064)
            ),

            flatSurface = Color(0xFF101013),
            raisedSurface = Color(0xFF232328),
            floatingSurface = Color(0xFF2E2E34),
            modalSurface = Color(0xFF343440),
        )
    }
}
