package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object CustomColors {
    val lightModeColors = mapOf(
        ColorKey.Income to Color(0xFF2D995B),
        ColorKey.Expense to Color(0xFFC26619),
        ColorKey.Transfer to Color(0xFF1976B8),
        ColorKey.SecondaryBackground to Color(0xFFF5F5F5),
        ColorKey.BtnPrimaryBackgroundEnabled to Color(0xFF222222),
        ColorKey.BtnPrimaryForegroundEnabled to Color(0xFFF1F1F1),
        ColorKey.BtnPrimaryBorder to Color.Transparent,
        ColorKey.BtnPrimaryBackgroundDisabled to Color(0xFFBDBDBD),
        ColorKey.BtnPrimaryForegroundDisabled to Color(0xFF757575),
        ColorKey.BtnSecondaryBackground to Color.Transparent,
        ColorKey.BtnSecondaryForegroundEnabled to Color(0xFF222222),
        ColorKey.BtnSecondaryBorderEnabled to Color(0xFF222222),
        ColorKey.BtnSecondaryForegroundDisabled to Color(0xFFBDBDBD),
        ColorKey.BtnSecondaryBorderDisabled to Color(0xFFBDBDBD),
        ColorKey.OnboardingGradientSecondaryColor to Color(0xFFC5FFF9),
        ColorKey.OnboardingGradientPrimaryColor to Color(0xFFDEE3E3),
        ColorKey.YELLOW to Color(0xFFDC9000),
        ColorKey.ORANGE to Color(0xFFE65100),
        ColorKey.RED to Color(0xFFC62828),
        ColorKey.PINK to Color(0xFFC218A6),
        ColorKey.PURPLE to Color(0xFF7B1FA2),
        ColorKey.BLUE to Color(0xFF1565C0),
        ColorKey.GREEN to Color(0xFF2E7D32),
        ColorKey.GRAY to Color(0xFF455A64),
        ColorKey.IndicatorPrimary to Color(0xFF00AFA1),
    )

    val darkModeColors = mapOf(
        ColorKey.Income to Color(0xFF70AF8B),
        ColorKey.Expense to Color(0xFFD78549),
        ColorKey.Transfer to Color(0xFF6EA0CB),
        ColorKey.SecondaryBackground to Color(0xFF222222),
        ColorKey.BtnPrimaryBackgroundEnabled to Color(0xFFF1F1F1),
        ColorKey.BtnPrimaryForegroundEnabled to Color(0xFF222222),
        ColorKey.BtnPrimaryBorder to Color.Transparent,
        ColorKey.BtnPrimaryBackgroundDisabled to Color(0xFF757575),
        ColorKey.BtnPrimaryForegroundDisabled to Color(0xFFBDBDBD),
        ColorKey.BtnSecondaryBackground to Color.Transparent,
        ColorKey.BtnSecondaryForegroundEnabled to Color(0xFFF1F1F1),
        ColorKey.BtnSecondaryBorderEnabled to Color(0xFFF1F1F1),
        ColorKey.BtnSecondaryForegroundDisabled to Color(0xFF757575),
        ColorKey.BtnSecondaryBorderDisabled to Color(0xFF757575),
        ColorKey.OnboardingGradientSecondaryColor to Color(0xFF1B4742),
        ColorKey.OnboardingGradientPrimaryColor to Color(0xFF050E0D),
        ColorKey.YELLOW to Color(0xFFFFEF74),
        ColorKey.ORANGE to Color(0xFFFFBC5F),
        ColorKey.RED to Color(0xFFFA7979),
        ColorKey.PINK to Color(0xFFFC6EC2),
        ColorKey.PURPLE to Color(0xFFC97BFA),
        ColorKey.BLUE to Color(0xFF64B5F6),
        ColorKey.GREEN to Color(0xFF81C784),
        ColorKey.GRAY to Color(0xFFB0BEC5),
        ColorKey.IndicatorPrimary to Color(0xFF22C2BB),
    )
}

@Composable
fun getCustomColor(key: ColorKey): Color {
    val colors =
        if (isSystemInDarkTheme()) CustomColors.darkModeColors else CustomColors.lightModeColors
    return colors[key] ?: MaterialTheme.colorScheme.primary
}
