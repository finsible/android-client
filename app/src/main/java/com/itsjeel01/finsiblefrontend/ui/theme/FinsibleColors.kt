package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.ui.graphics.Color

data class FinsibleColors(
    // Structural
    val transparent: Color = Color.Transparent,
    val black: Color = Color(0xFF000000),
    val white: Color = Color(0xFFFFFFFF),
    val same: Color,
    val inverse: Color,

    // Backgrounds & Surfaces
    val primaryBackground: Color,
    val secondaryBackground: Color,
    val surface: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val surfaceContainerHighest: Color,
    val surfaceContainerLow: Color,
    val surfaceContainerLowest: Color,
    val surfaceBright: Color,
    val surfaceDim: Color,
    val card: Color,
    val input: Color,

    // Content / Text hierarchy
    val primaryContent: Color,
    val primaryContent80: Color = primaryContent.copy(alpha = 0.8f),
    val primaryContent60: Color = primaryContent.copy(alpha = 0.6f),
    val primaryContent40: Color = primaryContent.copy(alpha = 0.4f),
    val secondaryContent: Color,
    val tertiaryContent: Color,
    val onSurfaceVariant: Color,
    val placeholder: Color,
    val disabledContent: Color,

    // Interactive / Controls
    val primaryButton: Color,
    val secondaryButton: Color,
    val tertiaryButton: Color,
    val onTertiaryButton: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,
    val link: Color,
    val selection: Color,
    val hover: Color,
    val hoverStrong: Color,
    val pressed: Color,
    val focused: Color,
    val disabled: Color,
    val ripple: Color,
    val shadow: Color,
    val overlay: Color,
    val scrim: Color,

    // Borders / Dividers / Outlines
    val border: Color,
    val outline: Color,
    val outlineVariant: Color,
    val divider: Color,

    // Semantic tokens
    val error: Color,
    val success: Color,
    val warning: Color,
    val info: Color,

    // Semantic containers (tonal backgrounds)
    val infoContainer: Color,
    val successContainer: Color,
    val warningContainer: Color,
    val errorContainer: Color,

    // Brand accent tonal steps (only the stops actually used)
    val brandAccent: Color = Color(0xFF2E8B57),
    val brandAccent50: Color,
    val brandAccent40: Color,
    val brandAccent20: Color,
    val brandAccent10: Color,

    // Transaction types
    val income: Color,
    val expense: Color,
    val transfer: Color,

    // Financial card gradients (pairs)
    val netWorthGradientStart: Color,
    val netWorthGradientEnd: Color,
    val assetsGradientStart: Color,
    val assetsGradientEnd: Color,
    val liabilitiesGradientStart: Color,
    val liabilitiesGradientEnd: Color,
) {
    companion object {
        val light = FinsibleColors(
            // Structural
            inverse = Color(0xFF1A1A20),
            same = Color(0xFFFAFAFB),

            // Backgrounds & Surfaces
            primaryBackground = Color(0xFFEFEFF2),
            secondaryBackground = Color(0xFFE8E8EC),
            surface = Color(0xFFFAFAFB),
            surfaceContainerLowest = Color(0xFFF5F5F7),
            surfaceContainerLow = Color(0xFFF7F7F9),
            surfaceContainer = Color(0xFFF2F2F5),
            surfaceContainerHigh = Color(0xFFFAFAFB),
            surfaceContainerHighest = Color(0xFFE8E8EC),
            surfaceBright = Color(0xFFFCFCFD),
            surfaceDim = Color(0xFFD6D6DB),
            card = Color(0xFFFAFAFB),
            input = Color(0xFFF2F2F5),

            // Content / Text
            primaryContent = Color(0xFF1A1A20),
            secondaryContent = Color(0xFF4E4E58),
            tertiaryContent = Color(0xFF6E6E78),
            onSurfaceVariant = Color(0xFF6E6E78),
            placeholder = Color(0xFF9E9EA8),
            disabledContent = Color(0xFF9E9EA8),

            // Interactive / Controls
            primaryButton = Color(0xFF2E8B57),
            secondaryButton = Color(0xFFE8E8EC),
            tertiaryButton = Color(0xFF3F8DA0),
            onTertiaryButton = Color(0xFFFAFAFB),
            tertiaryContainer = Color(0xFFE0F2F6),
            onTertiaryContainer = Color(0xFF0C3640),
            link = Color(0xFF236BAF),
            selection = Color(0xFFE2EDE6),
            hover = Color(0xFFE8E8EC),
            hoverStrong = Color(0xFFDFDFE4),
            pressed = Color(0xFFD6D6DB),
            focused = Color(0xFFE5EBE6),
            disabled = Color(0xFFD4D4D8),
            ripple = Color(0x2471717A),
            shadow = Color(0x1A1A1A20),
            overlay = Color(0x7A1A1A20),
            scrim = Color(0xA61A1A20),

            // Borders / Dividers
            border = Color(0xFFDCDCE1),
            outline = Color(0xFFCCCCD2),
            outlineVariant = Color(0xFFDCDCE1),
            divider = Color(0xFFE4E4E8),

            // Semantic tokens
            error = Color(0xFFCC4444),
            success = Color(0xFF28855E),
            warning = Color(0xFFC27A1A),
            info = Color(0xFF3574C4),

            // Semantic containers
            infoContainer = Color(0xFFE4EEF8),
            successContainer = Color(0xFFDAEDE2),
            warningContainer = Color(0xFFF5EDD8),
            errorContainer = Color(0xFFF5E0E0),

            // Brand accent tonal steps
            brandAccent = Color(0xFF2E8B57),
            brandAccent50 = Color(0xFF74C69A),
            brandAccent40 = Color(0xFF8AD1AB),
            brandAccent20 = Color(0xFFD0ECE0),
            brandAccent10 = Color(0xFFEBF6F1),

            // Transaction types
            income = Color(0xFF27846A),
            expense = Color(0xFFB45A5A),
            transfer = Color(0xFF6B7B8D),

            // Financial card gradients
            netWorthGradientStart = Color(0xFF174E30),
            netWorthGradientEnd = Color(0xFF3A8856),
            assetsGradientStart = Color(0xFF7A5D26),
            assetsGradientEnd = Color(0xFFC49640),
            liabilitiesGradientStart = Color(0xFF3A3A42),
            liabilitiesGradientEnd = Color(0xFF5C5C66),
        )

        val dark = FinsibleColors(
            // Structural
            inverse = Color(0xFFE4E4E8),
            same = Color(0xFF101013),

            // Backgrounds & Surfaces
            primaryBackground = Color(0xFF101013),
            secondaryBackground = Color(0xFF18181C),
            surface = Color(0xFF232328),
            surfaceContainerLowest = Color(0xFF0C0C0F),
            surfaceContainerLow = Color(0xFF1C1C20),
            surfaceContainer = Color(0xFF18181C),
            surfaceContainerHigh = Color(0xFF232328),
            surfaceContainerHighest = Color(0xFF2E2E34),
            surfaceBright = Color(0xFF343440),
            surfaceDim = Color(0xFF101013),
            card = Color(0xFF232328),
            input = Color(0xFF151518),

            // Content / Text
            primaryContent = Color(0xFFEAEAED),
            secondaryContent = Color(0xFFA4A4AE),
            tertiaryContent = Color(0xFF78788A),
            onSurfaceVariant = Color(0xFFA4A4AE),
            placeholder = Color(0xFF585864),
            disabledContent = Color(0xFF585864),

            // Interactive / Controls
            primaryButton = Color(0xFF34A064),
            secondaryButton = Color(0xFF232328),
            tertiaryButton = Color(0xFF4A9DB0),
            onTertiaryButton = Color(0xFF0C2A32),
            tertiaryContainer = Color(0xFF1A4650),
            onTertiaryContainer = Color(0xFFBBDDE6),
            link = Color(0xFF62C0E1),
            selection = Color(0xFF1C2C22),
            hover = Color(0xFF282830),
            hoverStrong = Color(0xFF2E2E34),
            pressed = Color(0xFF151518),
            focused = Color(0xFF1C2C22),
            disabled = Color(0xFF2E2E34),
            ripple = Color(0x28A4A4AE),
            shadow = Color(0x00000000),
            overlay = Color(0xBF101013),
            scrim = Color(0xD9101013),

            // Borders / Dividers
            border = Color(0xFF2E2E36),
            outline = Color(0xFF3C3C46),
            outlineVariant = Color(0xFF2E2E36),
            divider = Color(0xFF262630),

            // Semantic tokens
            error = Color(0xFFDA9090),
            success = Color(0xFF5CC08E),
            warning = Color(0xFFD9B060),
            info = Color(0xFF82B4DA),

            // Semantic containers
            infoContainer = Color(0xFF1A2D3E),
            successContainer = Color(0xFF142E22),
            warningContainer = Color(0xFF332610),
            errorContainer = Color(0xFF3A1A1A),

            // Brand accent tonal steps
            brandAccent = Color(0xFF34A064),
            brandAccent50 = Color(0xFF133C24),
            brandAccent40 = Color(0xFF10301C),
            brandAccent20 = Color(0xFF091C10),
            brandAccent10 = Color(0xFF0E2318),

            // Transaction types
            income = Color(0xFF5EC49E),
            expense = Color(0xFFD09090),
            transfer = Color(0xFF909CAC),

            // Financial card gradients
            netWorthGradientStart = Color(0xFF164228),
            netWorthGradientEnd = Color(0xFF34A064),
            assetsGradientStart = Color(0xFF5C4418),
            assetsGradientEnd = Color(0xFFA88040),
            liabilitiesGradientStart = Color(0xFF1C1C20),
            liabilitiesGradientEnd = Color(0xFF3C3C46),
        )
    }
}