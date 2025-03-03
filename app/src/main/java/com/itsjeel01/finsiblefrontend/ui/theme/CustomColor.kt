package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object CustomColors {
    val lightModeColors = mapOf(
        CustomColorKey.Income to Color(0xFF388E3C),
        CustomColorKey.Expense to Color(0xFFD32F2F),
        CustomColorKey.Transfer to Color(0xFF1976D2),
        CustomColorKey.SecondaryBackground to Color(0xFFF5F5F5),
        CustomColorKey.CategoryColor1 to Color(0xFFE0594D),
        CustomColorKey.CategoryColor2 to Color(0xFF3F7540),
        CustomColorKey.CategoryColor3 to Color(0xFF2488D8),
        CustomColorKey.CategoryColor4 to Color(0xFFAC8100),
        CustomColorKey.CategoryColor5 to Color(0xFFD37510),
        CustomColorKey.CategoryColor6 to Color(0xFFD45BE9),
        CustomColorKey.CategoryColor7 to Color(0xFF009EB3),
        CustomColorKey.CategoryColor8 to Color(0xFF797979),
        CustomColorKey.BtnPrimaryBackgroundEnabled to Color(0xFF222222),
        CustomColorKey.BtnPrimaryForegroundEnabled to Color(0xFFF1F1F1),
        CustomColorKey.BtnPrimaryBorder to Color.Transparent,
        CustomColorKey.BtnPrimaryBackgroundDisabled to Color(0xFFBDBDBD),
        CustomColorKey.BtnPrimaryForegroundDisabled to Color(0xFF757575),
        CustomColorKey.BtnSecondaryBackground to Color.Transparent,
        CustomColorKey.BtnSecondaryForegroundEnabled to Color(0xFF222222),
        CustomColorKey.BtnSecondaryBorderEnabled to Color(0xFF222222),
        CustomColorKey.BtnSecondaryForegroundDisabled to Color(0xFFBDBDBD),
        CustomColorKey.BtnSecondaryBorderDisabled to Color(0xFFBDBDBD),
        CustomColorKey.OnboardingGradientColor1 to Color(0xFFC5FFF9),
        CustomColorKey.OnboardingGradientColor2 to Color(0xFFDEE3E3)
    )

    val darkModeColors = mapOf(
        CustomColorKey.Income to Color(0xFF69F0AE),
        CustomColorKey.Expense to Color(0xFFFF8A80),
        CustomColorKey.Transfer to Color(0xFF82B1FF),
        CustomColorKey.SecondaryBackground to Color(0xFF222222),
        CustomColorKey.CategoryColor1 to Color(0xFFFF897E),
        CustomColorKey.CategoryColor2 to Color(0xFF74C976),
        CustomColorKey.CategoryColor3 to Color(0xFF56B2FD),
        CustomColorKey.CategoryColor4 to Color(0xFFF5D165),
        CustomColorKey.CategoryColor5 to Color(0xFFF8A750),
        CustomColorKey.CategoryColor6 to Color(0xFFEA82FC),
        CustomColorKey.CategoryColor7 to Color(0xFF56E8FA),
        CustomColorKey.CategoryColor8 to Color(0xFFBDBDBD),
        CustomColorKey.BtnPrimaryBackgroundEnabled to Color(0xFFF1F1F1),
        CustomColorKey.BtnPrimaryForegroundEnabled to Color(0xFF222222),
        CustomColorKey.BtnPrimaryBorder to Color.Transparent,
        CustomColorKey.BtnPrimaryBackgroundDisabled to Color(0xFF757575),
        CustomColorKey.BtnPrimaryForegroundDisabled to Color(0xFFBDBDBD),
        CustomColorKey.BtnSecondaryBackground to Color.Transparent,
        CustomColorKey.BtnSecondaryForegroundEnabled to Color(0xFFF1F1F1),
        CustomColorKey.BtnSecondaryBorderEnabled to Color(0xFFF1F1F1),
        CustomColorKey.BtnSecondaryForegroundDisabled to Color(0xFF757575),
        CustomColorKey.BtnSecondaryBorderDisabled to Color(0xFF757575),
        CustomColorKey.OnboardingGradientColor1 to Color(0xFF1B4742),
        CustomColorKey.OnboardingGradientColor2 to Color(0xFF050E0D)
    )
}

@Composable
fun getCustomColor(key: CustomColorKey): Color {
    val colors =
        if (isSystemInDarkTheme()) CustomColors.darkModeColors else CustomColors.lightModeColors
    return colors[key] ?: MaterialTheme.colorScheme.primary
}

@Composable
fun getCategoryColorsList(): List<Color> {
    return (1..8).map { getCategoryColor(it) }
}

@Composable
fun getCategoryColor(number: Int): Color {
    val key = when (number) {
        1 -> CustomColorKey.CategoryColor1
        2 -> CustomColorKey.CategoryColor2
        3 -> CustomColorKey.CategoryColor3
        4 -> CustomColorKey.CategoryColor4
        5 -> CustomColorKey.CategoryColor5
        6 -> CustomColorKey.CategoryColor6
        7 -> CustomColorKey.CategoryColor7
        8 -> CustomColorKey.CategoryColor8
        else -> CustomColorKey.CategoryColor1 // Default to CategoryColor1 if the number is invalid
    }
    return getCustomColor(key)
}