package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.itsjeel01.finsiblefrontend.common.logging.Logger

/** Resolves FinsibleColors tokens to Material3 ColorScheme and string-based lookups. */
class FinsibleColorResolver(private val colors: FinsibleColors) {

    private val colorCache = mutableMapOf<String, Color>()

    fun resolve(colorReference: String, fallbackColor: Color? = null): Color {
        return colorCache.getOrPut(colorReference) {
            resolveColorToken(colorReference)
                ?: parseHexColor(colorReference)
                ?: fallbackColor
                ?: Color.Gray
        }
    }

    /** Map FinsibleColors → Material3 light ColorScheme. Every parameter is explicitly overridden. */
    fun lightColors(): ColorScheme = ColorScheme(
        // Primary
        primary = colors.primaryButton,
        onPrimary = colors.same,
        primaryContainer = colors.surfaceContainerHigh,
        onPrimaryContainer = colors.primaryContent,
        inversePrimary = colors.brandAccent50,

        // Secondary
        secondary = colors.secondaryButton,
        onSecondary = colors.primaryContent,
        secondaryContainer = colors.surfaceContainer,
        onSecondaryContainer = colors.secondaryContent,

        // Tertiary
        tertiary = colors.tertiaryButton,
        onTertiary = colors.onTertiaryButton,
        tertiaryContainer = colors.tertiaryContainer,
        onTertiaryContainer = colors.onTertiaryContainer,

        // Error
        error = colors.error,
        onError = colors.same,
        errorContainer = colors.errorContainer,
        onErrorContainer = colors.error,

        // Backgrounds
        background = colors.primaryBackground,
        onBackground = colors.primaryContent,

        // Surfaces
        surface = colors.secondaryBackground,
        onSurface = colors.primaryContent,
        surfaceVariant = colors.surfaceContainer,
        onSurfaceVariant = colors.onSurfaceVariant,
        surfaceTint = colors.brandAccent,

        // Inverse
        inverseSurface = colors.primaryContent,
        inverseOnSurface = colors.secondaryBackground,

        // Outlines
        outline = colors.outline,
        outlineVariant = colors.outlineVariant,
        scrim = colors.scrim,

        // Surface elevation scale
        surfaceBright = colors.surfaceBright,
        surfaceContainer = colors.surfaceContainer,
        surfaceContainerHigh = colors.surfaceContainerHigh,
        surfaceContainerHighest = colors.surfaceContainerHighest,
        surfaceContainerLow = colors.surfaceContainerLow,
        surfaceContainerLowest = colors.surfaceContainerLowest,
        surfaceDim = colors.surfaceDim,

        // Fixed accent palette — derived from brand, not stored in FinsibleColors
        primaryFixed = Color(0xFFCDE9D7),
        primaryFixedDim = Color(0xFFA3D4B3),
        onPrimaryFixed = Color(0xFF0A2E1A),
        onPrimaryFixedVariant = Color(0xFF1D6B40),
        secondaryFixed = Color(0xFFE4E4E8),
        secondaryFixedDim = Color(0xFFD4D4D8),
        onSecondaryFixed = Color(0xFF1A1A20),
        onSecondaryFixedVariant = Color(0xFF3F3F46),
        tertiaryFixed = Color(0xFFDEF0F5),
        tertiaryFixedDim = Color(0xFFBBDDE6),
        onTertiaryFixed = Color(0xFF0C3640),
        onTertiaryFixedVariant = Color(0xFF266070),
    )

    /** Map FinsibleColors → Material3 dark ColorScheme. Every parameter is explicitly overridden. */
    fun darkColors(): ColorScheme = ColorScheme(
        // Primary
        primary = colors.primaryButton,
        onPrimary = colors.same,
        primaryContainer = colors.surfaceContainerHigh,
        onPrimaryContainer = colors.primaryContent,
        inversePrimary = colors.brandAccent50,

        // Secondary
        secondary = colors.secondaryButton,
        onSecondary = colors.primaryContent,
        secondaryContainer = colors.surfaceContainer,
        onSecondaryContainer = colors.secondaryContent,

        // Tertiary
        tertiary = colors.tertiaryButton,
        onTertiary = colors.onTertiaryButton,
        tertiaryContainer = colors.tertiaryContainer,
        onTertiaryContainer = colors.onTertiaryContainer,

        // Error
        error = colors.error,
        onError = colors.same,
        errorContainer = colors.errorContainer,
        onErrorContainer = colors.error,

        // Backgrounds
        background = colors.primaryBackground,
        onBackground = colors.primaryContent,

        // Surfaces
        surface = colors.secondaryBackground,
        onSurface = colors.primaryContent,
        surfaceVariant = colors.surfaceContainer,
        onSurfaceVariant = colors.onSurfaceVariant,
        surfaceTint = colors.brandAccent,

        // Inverse
        inverseSurface = colors.primaryContent,
        inverseOnSurface = colors.primaryBackground,

        // Outlines
        outline = colors.outline,
        outlineVariant = colors.outlineVariant,
        scrim = colors.scrim,

        // Surface elevation scale
        surfaceBright = colors.surfaceBright,
        surfaceContainer = colors.surfaceContainer,
        surfaceContainerHigh = colors.surfaceContainerHigh,
        surfaceContainerHighest = colors.surfaceContainerHighest,
        surfaceContainerLow = colors.surfaceContainerLow,
        surfaceContainerLowest = colors.surfaceContainerLowest,
        surfaceDim = colors.surfaceDim,

        // Fixed accent palette — derived from brand, same values both themes (cross-theme by spec)
        primaryFixed = Color(0xFFCDE9D7),
        primaryFixedDim = Color(0xFFA3D4B3),
        onPrimaryFixed = Color(0xFF0A2E1A),
        onPrimaryFixedVariant = Color(0xFF1D6B40),
        secondaryFixed = Color(0xFFE4E4E8),
        secondaryFixedDim = Color(0xFFD4D4D8),
        onSecondaryFixed = Color(0xFF1A1A20),
        onSecondaryFixedVariant = Color(0xFF3F3F46),
        tertiaryFixed = Color(0xFFDEF0F5),
        tertiaryFixedDim = Color(0xFFBBDDE6),
        onTertiaryFixed = Color(0xFF0C3640),
        onTertiaryFixedVariant = Color(0xFF266070),
    )

    private fun resolveColorToken(token: String): Color? {
        return when (token.lowercase()) {
            // Backgrounds & Surfaces
            "primarybackground" -> colors.primaryBackground
            "secondarybackground" -> colors.secondaryBackground
            "surface" -> colors.surface
            "surfacecontainer" -> colors.surfaceContainer
            "surfacecontainerhigh" -> colors.surfaceContainerHigh
            "surfacecontainerhighest" -> colors.surfaceContainerHighest
            "surfacecontainerlow" -> colors.surfaceContainerLow
            "surfacecontainerlowest" -> colors.surfaceContainerLowest
            "surfacebright" -> colors.surfaceBright
            "surfacedim" -> colors.surfaceDim
            "card" -> colors.card
            "input" -> colors.input

            // Content hierarchy
            "primarycontent" -> colors.primaryContent
            "secondarycontent" -> colors.secondaryContent
            "tertiarycontent" -> colors.tertiaryContent
            "onsurfacevariant" -> colors.onSurfaceVariant
            "placeholder" -> colors.placeholder
            "disabledcontent" -> colors.disabledContent

            // Interactive / Controls
            "primarybutton" -> colors.primaryButton
            "secondarybutton" -> colors.secondaryButton
            "tertiarybutton" -> colors.tertiaryButton
            "ontertiarybutton" -> colors.onTertiaryButton
            "tertiarycontainer" -> colors.tertiaryContainer
            "ontertiarycontainer" -> colors.onTertiaryContainer
            "link" -> colors.link
            "selection" -> colors.selection
            "hover" -> colors.hover
            "hoverstrong" -> colors.hoverStrong
            "pressed" -> colors.pressed
            "focused" -> colors.focused
            "disabled" -> colors.disabled
            "shadow" -> colors.shadow
            "overlay" -> colors.overlay

            // Borders / Outlines
            "border" -> colors.border
            "outline" -> colors.outline
            "outlinevariant" -> colors.outlineVariant
            "scrim" -> colors.scrim
            "divider" -> colors.divider

            // Semantic
            "error" -> colors.error
            "success" -> colors.success
            "warning" -> colors.warning
            "info" -> colors.info
            "infocontainer" -> colors.infoContainer
            "successcontainer" -> colors.successContainer
            "warningcontainer" -> colors.warningContainer
            "errorcontainer" -> colors.errorContainer

            // Brand
            "brandaccent" -> colors.brandAccent

            // Transaction types
            "income" -> colors.income
            "expense" -> colors.expense
            "transfer" -> colors.transfer

            else -> null
        }
    }

    private fun parseHexColor(colorString: String): Color? {
        return try {
            val hex = colorString.removePrefix("#")
            if (hex.length == 6 || hex.length == 8)
                Color("#$hex".toColorInt())
            else null
        } catch (e: IllegalArgumentException) {
            Logger.UI.e("Invalid color format: $colorString", e)
            null
        }
    }
}