package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleSemanticColors.Companion.dark
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleSemanticColors.Companion.light

/**
 * Layer 2 — Semantic color tokens.
 *
 * Named by UI purpose. Aliases a Layer 1 primitive (or a raw hex for one-off values).
 * Two theme instances: [light] and [dark]. Components consume Layer 3 tokens,
 * never Layer 2 directly.
 *
 * Pre-split tokens (inputSurface, cardSurface, icon*, inputBorder, cardBorder)
 * are intentionally independent aliases — they share initial values with their
 * parent tokens but can diverge independently across future theme iterations.
 */
@Immutable
data class FinsibleSemanticColors(

    // Surfaces
    val surfaceBase: Color,
    val surfaceDefault: Color,
    val surfaceRaised: Color,
    val surfaceOverlay: Color,
    val surfaceSunken: Color,
    val surfaceBrandSubtle: Color,
    val surfaceBrandTint: Color,

    // Pre-split component surfaces
    val inputSurface: Color,
    val cardSurface: Color,

    // Content
    val contentPrimary: Color,
    val contentSecondary: Color,
    val contentTertiary: Color,
    val contentPlaceholder: Color,
    val contentDisabled: Color,
    val contentOnBrand: Color,
    val contentLink: Color,
    val contentInverse: Color,

    // Pre-split icon tokens (may diverge from text content)
    val iconPrimary: Color,
    val iconSecondary: Color,
    val iconTertiary: Color,
    val iconDisabled: Color,
    val iconOnBrand: Color,
    val iconLink: Color,

    // Brand
    val brandInteractive: Color,
    val brandInteractiveHovered: Color,
    val brandInteractivePressed: Color,
    val brandAccent: Color,
    val brandTint: Color,
    val brandSubtle: Color,

    // Borders
    val borderSubtle: Color,
    val borderDefault: Color,
    val borderStrong: Color,
    val borderBrand: Color,
    val borderError: Color,

    // Pre-split component borders
    val inputBorder: Color,
    val cardBorder: Color,

    // Full-screen treatments
    val scrim: Color,
    val overlay: Color,

    // Feedback
    val feedbackError: Color,
    val feedbackErrorSurface: Color,
    val feedbackSuccess: Color,
    val feedbackSuccessSurface: Color,
    val feedbackWarning: Color,
    val feedbackWarningSurface: Color,
    val feedbackInfo: Color,
    val feedbackInfoSurface: Color,

    // Transaction
    val transactionIncome: Color,
    val transactionExpense: Color,
    val transactionTransfer: Color,
    val transactionIncomeSurface: Color,
    val transactionExpenseSurface: Color,
    val transactionTransferSurface: Color,

    ) {
    companion object {

        // Light theme
        val light = FinsibleSemanticColors(
            // Surfaces
            surfaceBase = FinsiblePrimitives.Neutral.n200,
            surfaceDefault = FinsiblePrimitives.Neutral.n300,
            surfaceRaised = FinsiblePrimitives.Neutral.n0,
            surfaceOverlay = FinsiblePrimitives.Neutral.n0,
            surfaceSunken = Color(0xFFF2F2F5),
            surfaceBrandSubtle = FinsiblePrimitives.Green.g50,
            surfaceBrandTint = FinsiblePrimitives.Green.g100,

            // Pre-split
            inputSurface = Color(0xFFF2F2F5),          // sunken — may diverge
            cardSurface = FinsiblePrimitives.Neutral.n0, // raised — may diverge

            // Content
            contentPrimary = FinsiblePrimitives.Neutral.n700,
            contentSecondary = FinsiblePrimitives.Neutral.n600,
            contentTertiary = FinsiblePrimitives.Neutral.n500,
            contentPlaceholder = FinsiblePrimitives.Neutral.n400,
            contentDisabled = FinsiblePrimitives.Neutral.n400,
            contentOnBrand = FinsiblePrimitives.Neutral.n0,
            contentLink = Color(0xFF236BAF),
            contentInverse = Color(0xFFFAFAEB),

            // Pre-split icons
            iconPrimary = FinsiblePrimitives.Neutral.n700,
            iconSecondary = FinsiblePrimitives.Neutral.n600,
            iconTertiary = FinsiblePrimitives.Neutral.n500,
            iconDisabled = FinsiblePrimitives.Neutral.n400,
            iconOnBrand = FinsiblePrimitives.Neutral.n0,
            iconLink = Color(0xFF236BAF),

            // Brand
            brandInteractive = FinsiblePrimitives.Green.g600,
            brandInteractiveHovered = FinsiblePrimitives.Green.g700,
            brandInteractivePressed = FinsiblePrimitives.Green.g800,
            brandAccent = FinsiblePrimitives.Green.g400,
            brandTint = FinsiblePrimitives.Green.g100,
            brandSubtle = FinsiblePrimitives.Green.g50,

            // Borders
            borderSubtle = Color(0xFFE4E4E8),
            borderDefault = Color(0xFFDCDCE1),
            borderStrong = Color(0xFFCCCCDD),
            borderBrand = FinsiblePrimitives.Green.g600,
            borderError = Color(0xFFCC4444),

            // Pre-split borders
            inputBorder = Color(0xFFDCDCE1),  // default — may diverge
            cardBorder = Color(0xFFDCDCE1),  // default — may diverge

            scrim = Color(0xA61A1A20),
            overlay = Color(0x7A1A1A20),

            // Feedback
            feedbackError = Color(0xFFCC4444),
            feedbackErrorSurface = Color(0xFFF5E0E0),
            feedbackSuccess = Color(0xFF28855E),
            feedbackSuccessSurface = Color(0xFFDAE7E2),
            feedbackWarning = Color(0xFFC27A1A),
            feedbackWarningSurface = Color(0xFFF5EDD8),
            feedbackInfo = Color(0xFF3574C4),
            feedbackInfoSurface = Color(0xFFE4EEF8),

            // Transaction
            transactionIncome = Color(0xFF27846A),
            transactionExpense = Color(0xFFB45A5A),
            transactionTransfer = Color(0xFF6B7B8D),
            transactionIncomeSurface = Color(0xFFDAE7E2),
            transactionExpenseSurface = Color(0xFFF5E0E0),
            transactionTransferSurface = Color(0xFFEAEAED),
        )

        // Dark theme
        val dark = FinsibleSemanticColors(
            // Surfaces
            surfaceBase = Color(0xFF101013),
            surfaceDefault = Color(0xFF18181C),
            surfaceRaised = Color(0xFF232328),
            surfaceOverlay = Color(0xFF1C1C20),
            surfaceSunken = Color(0xFF151518),
            surfaceBrandSubtle = Color(0xFF0E2318),
            surfaceBrandTint = Color(0xFF142E22),

            // Pre-split
            inputSurface = Color(0xFF151518),
            cardSurface = Color(0xFF232328),

            // Content
            contentPrimary = Color(0xFFEAEAED),
            contentSecondary = Color(0xFFA4A4AE),
            contentTertiary = Color(0xFF78788A),
            contentPlaceholder = Color(0xFF585864),
            contentDisabled = Color(0xFF585864),
            contentOnBrand = Color(0xFF101013),
            contentLink = Color(0xFF62C0E1),
            contentInverse = Color(0xFF1A1A20),

            // Pre-split icons
            iconPrimary = Color(0xFFEAEAED),
            iconSecondary = Color(0xFFA4A4AE),
            iconTertiary = Color(0xFF78788A),
            iconDisabled = Color(0xFF585864),
            iconOnBrand = Color(0xFF101013),
            iconLink = Color(0xFF62C0E1),

            // Brand
            brandInteractive = Color(0xFF34A064),
            brandInteractiveHovered = Color(0xFF3DB870),
            brandInteractivePressed = Color(0xFF50CC88),
            brandAccent = Color(0xFF2A7A50),
            brandTint = Color(0xFF142E22),
            brandSubtle = Color(0xFF0E2318),

            // Borders
            borderSubtle = Color(0xFF262630),
            borderDefault = Color(0xFF2E2E36),
            borderStrong = Color(0xFF3C3C46),
            borderBrand = Color(0xFF34A064),
            borderError = Color(0xFFDA9090),

            // Pre-split borders
            inputBorder = Color(0xFF2E2E36),
            cardBorder = Color(0xFF2E2E36),

            scrim = Color(0xD9101013),
            overlay = Color(0xBF101013),

            // Feedback
            feedbackError = Color(0xFFDA9090),
            feedbackErrorSurface = Color(0xFF3A1A1A),
            feedbackSuccess = Color(0xFF5CC08E),
            feedbackSuccessSurface = Color(0xFF142E22),
            feedbackWarning = Color(0xFFD9B060),
            feedbackWarningSurface = Color(0xFF332610),
            feedbackInfo = Color(0xFF82B4DA),
            feedbackInfoSurface = Color(0xFF1A2D3E),

            // Transaction
            transactionIncome = Color(0xFF5EC49E),
            transactionExpense = Color(0xFFD09090),
            transactionTransfer = Color(0xFF909CAC),
            transactionIncomeSurface = Color(0xFF142E22),
            transactionExpenseSurface = Color(0xFF3A1A1A),
            transactionTransferSurface = Color(0xFF232328),
        )
    }
}
