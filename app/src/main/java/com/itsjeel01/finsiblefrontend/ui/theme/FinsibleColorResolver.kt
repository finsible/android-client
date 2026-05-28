package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.itsjeel01.finsiblefrontend.common.logging.Logger

/**
 * Resolves Finsible semantic colors to Material3 ColorScheme and string-based lookups.
 *
 * [resolve] supports both hex strings ("#RRGGBB" / "#AARRGGBB") and named tokens
 * from the semantic design system (e.g. "contentPrimary", "brandInteractive").
 */
class FinsibleColorResolver(
    private val semantic: FinsibleSemanticColors,
) {

    private val colorCache = mutableMapOf<String, Color>()

    /**
     * Resolve a color reference string to a [Color].
     * Accepts hex strings ("#RRGGBB" / "#AARRGGBB") and named semantic tokens.
     * Falls back to [fallbackColor], then [Color.Gray].
     */
    fun resolve(colorReference: String, fallbackColor: Color? = null): Color {
        return colorCache.getOrPut(colorReference) {
            resolveNamedToken(colorReference)
                ?: parseHexColor(colorReference)
                ?: fallbackColor
                ?: Color.Gray
        }
    }

    // ── M3 ColorScheme mappings ──────────────────────────────────────────

    fun lightColors(): ColorScheme = ColorScheme(
        primary = semantic.brandInteractive,
        onPrimary = semantic.contentOnBrand,
        primaryContainer = semantic.surfaceBrandSubtle,
        onPrimaryContainer = semantic.brandInteractivePressed,
        inversePrimary = semantic.brandAccent,

        secondary = semantic.surfaceDefault,
        onSecondary = semantic.contentPrimary,
        secondaryContainer = semantic.surfaceSunken,
        onSecondaryContainer = semantic.contentSecondary,

        tertiary = semantic.surfaceSunken,
        onTertiary = semantic.contentOnBrand,
        tertiaryContainer = semantic.surfaceBrandSubtle,
        onTertiaryContainer = semantic.brandInteractive,

        error = semantic.feedbackError,
        onError = semantic.contentOnBrand,
        errorContainer = semantic.feedbackErrorSurface,
        onErrorContainer = semantic.feedbackError,

        background = semantic.surfaceBase,
        onBackground = semantic.contentPrimary,

        surface = semantic.surfaceDefault,
        onSurface = semantic.contentPrimary,
        surfaceVariant = semantic.surfaceSunken,
        onSurfaceVariant = semantic.contentSecondary,
        surfaceTint = semantic.brandInteractive,

        inverseSurface = semantic.contentPrimary,
        inverseOnSurface = semantic.surfaceDefault,

        outline = semantic.borderStrong,
        outlineVariant = semantic.borderDefault,
        scrim = semantic.scrim,

        surfaceBright = Color(0xFFFCFCFD),
        surfaceContainer = semantic.surfaceSunken,
        surfaceContainerHigh = semantic.surfaceRaised,
        surfaceContainerHighest = semantic.surfaceRaised,
        surfaceContainerLow = semantic.surfaceDefault,
        surfaceContainerLowest = semantic.surfaceBase,
        surfaceDim = semantic.surfaceBase,

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

    fun darkColors(): ColorScheme = ColorScheme(
        primary = semantic.brandInteractive,
        onPrimary = semantic.contentOnBrand,
        primaryContainer = semantic.surfaceBrandSubtle,
        onPrimaryContainer = semantic.brandInteractivePressed,
        inversePrimary = semantic.brandAccent,

        secondary = semantic.surfaceDefault,
        onSecondary = semantic.contentPrimary,
        secondaryContainer = semantic.surfaceSunken,
        onSecondaryContainer = semantic.contentSecondary,

        tertiary = semantic.surfaceSunken,
        onTertiary = semantic.contentOnBrand,
        tertiaryContainer = semantic.surfaceBrandSubtle,
        onTertiaryContainer = semantic.brandInteractive,

        error = semantic.feedbackError,
        onError = semantic.contentOnBrand,
        errorContainer = semantic.feedbackErrorSurface,
        onErrorContainer = semantic.feedbackError,

        background = semantic.surfaceBase,
        onBackground = semantic.contentPrimary,

        surface = semantic.surfaceDefault,
        onSurface = semantic.contentPrimary,
        surfaceVariant = semantic.surfaceSunken,
        onSurfaceVariant = semantic.contentSecondary,
        surfaceTint = semantic.brandInteractive,

        inverseSurface = semantic.contentPrimary,
        inverseOnSurface = semantic.surfaceBase,

        outline = semantic.borderStrong,
        outlineVariant = semantic.borderDefault,
        scrim = semantic.scrim,

        surfaceBright = Color(0xFF343440),
        surfaceContainer = semantic.surfaceSunken,
        surfaceContainerHigh = semantic.surfaceRaised,
        surfaceContainerHighest = semantic.surfaceRaised,
        surfaceContainerLow = semantic.surfaceDefault,
        surfaceContainerLowest = semantic.surfaceBase,
        surfaceDim = semantic.surfaceBase,

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

    // ── Named token resolution ───────────────────────────────────────────

    private fun resolveNamedToken(token: String): Color? {
        val t = token.lowercase()
        return when (// Surfaces
            t) {
            "surfacebase" -> semantic.surfaceBase
            "surfacedefault" -> semantic.surfaceDefault
            "surfaceraised" -> semantic.surfaceRaised
            "surfaceoverlay" -> semantic.surfaceOverlay
            "surfacesunken" -> semantic.surfaceSunken
            "surfacebrandsubtle" -> semantic.surfaceBrandSubtle
            "surfacebrandtint" -> semantic.surfaceBrandTint
            "inputsurface" -> semantic.inputSurface
            "cardsurface" -> semantic.cardSurface

            // Content
            "contentprimary" -> semantic.contentPrimary
            "contentsecondary" -> semantic.contentSecondary
            "contenttertiary" -> semantic.contentTertiary
            "contentplaceholder" -> semantic.contentPlaceholder
            "contentdisabled" -> semantic.contentDisabled
            "contentonbrand" -> semantic.contentOnBrand
            "contentlink" -> semantic.contentLink
            "contentinverse" -> semantic.contentInverse

            // Icons
            "iconprimary" -> semantic.iconPrimary
            "iconsecondary" -> semantic.iconSecondary
            "icontertiary" -> semantic.iconTertiary
            "icondisabled" -> semantic.iconDisabled
            "icononbrand" -> semantic.iconOnBrand
            "iconlink" -> semantic.iconLink

            // Brand
            "brandinteractive" -> semantic.brandInteractive
            "brandinteractivehovered" -> semantic.brandInteractiveHovered
            "brandinteractivepressed" -> semantic.brandInteractivePressed
            "brandaccent" -> semantic.brandAccent
            "brandtint" -> semantic.brandTint
            "brandsubtle" -> semantic.brandSubtle

            // Borders
            "bordersubtle" -> semantic.borderSubtle
            "borderdefault" -> semantic.borderDefault
            "borderstrong" -> semantic.borderStrong
            "borderbrand" -> semantic.borderBrand
            "bordererror" -> semantic.borderError
            "inputborder" -> semantic.inputBorder
            "cardborder" -> semantic.cardBorder
            "scrim" -> semantic.scrim
            "overlay" -> semantic.overlay

            // Feedback
            "feedbackerror" -> semantic.feedbackError
            "feedbackerrorsurface" -> semantic.feedbackErrorSurface
            "feedbacksuccess" -> semantic.feedbackSuccess
            "feedbacksuccesssurface" -> semantic.feedbackSuccessSurface
            "feedbackwarning" -> semantic.feedbackWarning
            "feedbackwarningsurface" -> semantic.feedbackWarningSurface
            "feedbackinfo" -> semantic.feedbackInfo
            "feedbackinfosurface" -> semantic.feedbackInfoSurface

            // Transaction
            "transactionincome" -> semantic.transactionIncome
            "transactionexpense" -> semantic.transactionExpense
            "transactiontransfer" -> semantic.transactionTransfer
            "transactionincomesurface" -> semantic.transactionIncomeSurface
            "transactionexpensesurface" -> semantic.transactionExpenseSurface
            "transactiontransfersurface" -> semantic.transactionTransferSurface

            // Legacy aliases (backward compat for string-based consumers)
            "primarybackground" -> semantic.surfaceBase
            "secondarybackground" -> semantic.surfaceDefault
            "surface" -> semantic.surfaceRaised
            "surfacecontainer" -> semantic.surfaceSunken
            "input" -> semantic.inputSurface
            "card" -> semantic.cardSurface
            "primarycontent" -> semantic.contentPrimary
            "secondarycontent" -> semantic.contentSecondary
            "tertiarycontent" -> semantic.contentTertiary
            "onsurfacevariant" -> semantic.contentSecondary
            "placeholder" -> semantic.contentPlaceholder
            "disabledcontent" -> semantic.contentDisabled
            "primarybutton" -> semantic.brandInteractive
            "link" -> semantic.contentLink
            "selection" -> semantic.surfaceBrandTint
            "hover" -> semantic.surfaceDefault
            "pressed" -> semantic.surfaceSunken
            "focused" -> semantic.surfaceBrandSubtle
            "disabled" -> semantic.surfaceSunken
            "border" -> semantic.borderDefault
            "outline" -> semantic.borderStrong
            "outlinevariant" -> semantic.borderDefault
            "divider" -> semantic.borderSubtle
            "error" -> semantic.feedbackError
            "success" -> semantic.feedbackSuccess
            "warning" -> semantic.feedbackWarning
            "info" -> semantic.feedbackInfo
            "errorcontainer" -> semantic.feedbackErrorSurface
            "successcontainer" -> semantic.feedbackSuccessSurface
            "warningcontainer" -> semantic.feedbackWarningSurface
            "infocontainer" -> semantic.feedbackInfoSurface
            "brandaccent" -> semantic.brandInteractive
            "income" -> semantic.transactionIncome
            "expense" -> semantic.transactionExpense
            "transfer" -> semantic.transactionTransfer
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
