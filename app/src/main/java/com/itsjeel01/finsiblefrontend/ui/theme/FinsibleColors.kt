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
    val surfaceContainerLow: Color,
    val surfaceContainerDim: Color,
    val card: Color,
    val input: Color,

    // Content / Text hierarchy
    val primaryContent: Color,
    val primaryContent80: Color = primaryContent.copy(alpha = 0.8f),
    val primaryContent60: Color = primaryContent.copy(alpha = 0.6f),
    val primaryContent40: Color = primaryContent.copy(alpha = 0.4f),
    val primaryContent20: Color = primaryContent.copy(alpha = 0.2f),
    val primaryContent10: Color = primaryContent.copy(alpha = 0.1f),
    val primaryContent5: Color = primaryContent.copy(alpha = 0.05f),
    val secondaryContent: Color,
    val tertiaryContent: Color,
    val onSurfaceVariant: Color,
    val placeholder: Color,
    val disabledContent: Color,

    // Interactive / Controls
    val primaryButton: Color,
    val secondaryButton: Color,
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

    // Semantic containers (10% tonal backgrounds)
    val infoContainer: Color,
    val successContainer: Color,
    val warningContainer: Color,
    val errorContainer: Color,

    // Brand accent tonal steps
    val brandAccent: Color = Color(0xFF2E8B57),
    val brandAccent90: Color,
    val brandAccent80: Color,
    val brandAccent70: Color,
    val brandAccent60: Color,
    val brandAccent50: Color,
    val brandAccent40: Color,
    val brandAccent30: Color,
    val brandAccent20: Color,
    val brandAccent10: Color,

    // Transaction types
    val income: Color,
    val expense: Color,
    val transfer: Color,

    // Financial card gradients (pairs)
    val gradientBrand1: Color,
    val gradientBrand2: Color,
    val gradientSuccess1: Color,
    val gradientSuccess2: Color,
    val gradientWarning1: Color,
    val gradientWarning2: Color,
    val gradientIncome1: Color,
    val gradientIncome2: Color,
    val gradientExpense1: Color,
    val gradientExpense2: Color,
    val gradientSavings1: Color,
    val gradientSavings2: Color,
    val gradientInvestment1: Color,
    val gradientInvestment2: Color,
    val gradientBudget1: Color,
    val gradientBudget2: Color,
    val gradientNeutral1: Color,
    val gradientNeutral2: Color,
    val gradientPremium1: Color,
    val gradientPremium2: Color,
) {
    companion object {
        val light = FinsibleColors(
            // Structural
            inverse = Color(0xFF000000),
            same = Color(0xFFFFFFFF),

            // Backgrounds & Surfaces
            primaryBackground = Color(0xFFFEFEFA),
            secondaryBackground = Color(0xFFF8F8F4),
            surface = Color(0xFFFFFFFF),
            surfaceContainer = Color(0xFFF8F8F4),
            surfaceContainerHigh = Color(0xFFFFFFFF),
            surfaceContainerLow = Color(0xFFFBFBF7),
            surfaceContainerDim = Color(0xFFF8F8F4).copy(alpha = 0.5f),
            card = Color(0xFFFFFFFF),
            input = Color(0xFFFBFBF7),

            // Content / Text
            primaryContent = Color(0xFF010B13),
            secondaryContent = Color(0xFF4A4A4A),
            tertiaryContent = Color(0xFF6E6E6E),
            onSurfaceVariant = Color(0xFF5A5A5A),
            placeholder = Color(0xFF9E9E9E),
            disabledContent = Color(0xFF9E9E9E),

            // Interactive / Controls
            primaryButton = Color(0xFF2E8B57),
            secondaryButton = Color(0xFFF8F8F4),
            link = Color(0xFF1B5E20),
            selection = Color(0xFFC8E6C9),
            hover = Color(0xFFE8F5E8),
            hoverStrong = Color(0xFFD0E8D0),
            pressed = Color(0xFFD4E8D4),
            focused = Color(0xFFE8F5E8),
            disabled = Color(0xFFBDBDBD),
            ripple = Color(0x41636363),
            shadow = Color(0x26010B13),
            overlay = Color(0x99010B13),
            scrim = Color(0xB3000000),

            // Borders / Dividers / Outlines
            border = Color(0xFFE5E5E1),
            outline = Color(0xFFD0D0CC),
            outlineVariant = Color(0xFFEAEAE6),
            divider = Color(0xFFEFEFEB),

            // Semantic tokens
            error = Color(0xFFD72D25),
            success = Color(0xFF03C03C),
            warning = Color(0xFFFFBF00),
            info = Color(0xFF1976D2),

            // Semantic containers
            infoContainer = Color(0xFF1976D2).copy(alpha = 0.1f),
            successContainer = Color(0xFF03C03C).copy(alpha = 0.1f),
            warningContainer = Color(0xFFFFBF00).copy(alpha = 0.1f),
            errorContainer = Color(0xFFD72D25).copy(alpha = 0.1f),

            // Brand accent tonal steps
            brandAccent = Color(0xFF2E8B57),
            brandAccent90 = Color(0xFF3A9A65),
            brandAccent80 = Color(0xFF47A973),
            brandAccent70 = Color(0xFF5BB987),
            brandAccent60 = Color(0xFF73C79B),
            brandAccent50 = Color(0xFF8FD5AF),
            brandAccent40 = Color(0xFFABE3C3),
            brandAccent30 = Color(0xFFC2EDD4),
            brandAccent20 = Color(0xFFD9F5E5),
            brandAccent10 = Color(0xFFEDFAF3),

            // Transaction types
            income = Color(0xFF4A8B2E),
            expense = Color(0xFFC13C26),
            transfer = Color(0xFF348599),

            // Financial card gradients
            gradientBrand1 = Color(0xFF2E8B57),
            gradientBrand2 = Color(0xFF73D09C),
            gradientSuccess1 = Color(0xFF2ECC71),
            gradientSuccess2 = Color(0xFF27AE60),
            gradientWarning1 = Color(0xFFE74C3C),
            gradientWarning2 = Color(0xFFC0392B),
            gradientIncome1 = Color(0xFF4CAF50),
            gradientIncome2 = Color(0xFF2E7D32),
            gradientExpense1 = Color(0xFFFF6B6B),
            gradientExpense2 = Color(0xFFEE5A52),
            gradientSavings1 = Color(0xFFFFB300),
            gradientSavings2 = Color(0xFFFF8F00),
            gradientInvestment1 = Color(0xFF1976D2),
            gradientInvestment2 = Color(0xFF1565C0),
            gradientBudget1 = Color(0xFF4A47E8),
            gradientBudget2 = Color(0xFF8E6DD9),
            gradientNeutral1 = Color(0xFF607D8B),
            gradientNeutral2 = Color(0xFF455A64),
            gradientPremium1 = Color(0xFFE67E22),
            gradientPremium2 = Color(0xFFD35400),
        )

        val dark = FinsibleColors(
            // Structural
            inverse = Color(0xFFFFFFFF),
            same = Color(0xFF000000),

            // Backgrounds & Surfaces
            primaryBackground = Color(0xFF1B1B1B),
            secondaryBackground = Color(0xFF242424),
            surface = Color(0xFF2A2A2A),
            surfaceContainer = Color(0xFF242424),
            surfaceContainerHigh = Color(0xFF333333),
            surfaceContainerLow = Color(0xFF1F1F1F),
            surfaceContainerDim = Color(0xFF242424).copy(alpha = 0.5f),
            card = Color(0xFF2A2A2A),
            input = Color(0xFF1F1F1F),

            // Content / Text
            primaryContent = Color(0xFFF7F7F7),
            secondaryContent = Color(0xFFB8B8B8),
            tertiaryContent = Color(0xFF8E8E8E),
            onSurfaceVariant = Color(0xFF9E9E9E),
            placeholder = Color(0xFF6E6E6E),
            disabledContent = Color(0xFF737373),

            // Interactive / Controls
            primaryButton = Color(0xFF2E8B57),
            secondaryButton = Color(0xFF404040),
            link = Color(0xFF4CAF50),
            selection = Color(0xFF1F3A2B),
            hover = Color(0xFF1F3A2B),
            hoverStrong = Color(0xFF26452F),
            pressed = Color(0xFF17301F),
            focused = Color(0xFF1F3A2B),
            disabled = Color(0xFF525252),
            ripple = Color(0x4DBDBDBD),
            shadow = Color(0x40D7D7D7),
            overlay = Color(0xB3000000),
            scrim = Color(0xCC000000),

            // Borders / Dividers / Outlines
            border = Color(0xFF404040),
            outline = Color(0xFF525252),
            outlineVariant = Color(0xFF353535),
            divider = Color(0xFF303030),

            // Semantic tokens
            error = Color(0xFFFF513E),
            success = Color(0xFF03C03C),
            warning = Color(0xFFFFBF00),
            info = Color(0xFF42A5F5),

            // Semantic containers
            infoContainer = Color(0xFF42A5F5).copy(alpha = 0.1f),
            successContainer = Color(0xFF03C03C).copy(alpha = 0.1f),
            warningContainer = Color(0xFFFFBF00).copy(alpha = 0.1f),
            errorContainer = Color(0xFFFF513E).copy(alpha = 0.1f),

            // Brand accent tonal steps
            brandAccent = Color(0xFF5BB95E),
            brandAccent90 = Color(0xFF2A7A4D),
            brandAccent80 = Color(0xFF267044),
            brandAccent70 = Color(0xFF22653C),
            brandAccent60 = Color(0xFF1E5A35),
            brandAccent50 = Color(0xFF1A502E),
            brandAccent40 = Color(0xFF154026),
            brandAccent30 = Color(0xFF11351F),
            brandAccent20 = Color(0xFF0D2A18),
            brandAccent10 = Color(0xFF091F11),

            // Transaction types
            income = Color(0xFF7BC86F),
            expense = Color(0xFFFF6E54),
            transfer = Color(0xFF61A4B5),

            // Financial card gradients
            gradientBrand1 = Color(0xFF5BB95E),
            gradientBrand2 = Color(0xFF1E5A35),
            gradientSuccess1 = Color(0xFF66BB6A),
            gradientSuccess2 = Color(0xFF2E7D32),
            gradientWarning1 = Color(0xFFEF5350),
            gradientWarning2 = Color(0xFFC62828),
            gradientIncome1 = Color(0xFF7BC86F),
            gradientIncome2 = Color(0xFF4A8B3D),
            gradientExpense1 = Color(0xFFFF8A80),
            gradientExpense2 = Color(0xFFFF5252),
            gradientSavings1 = Color(0xFFFFD54F),
            gradientSavings2 = Color(0xFFFFB300),
            gradientInvestment1 = Color(0xFF42A5F5),
            gradientInvestment2 = Color(0xFF1976D2),
            gradientBudget1 = Color(0xFF7C7ADB),
            gradientBudget2 = Color(0xFF5753C9),
            gradientNeutral1 = Color(0xFF90A4AE),
            gradientNeutral2 = Color(0xFF607D8B),
            gradientPremium1 = Color(0xFFFF9800),
            gradientPremium2 = Color(0xFFE67E22),
        )
    }
}