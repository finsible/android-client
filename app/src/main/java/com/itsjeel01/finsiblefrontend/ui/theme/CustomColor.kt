package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import com.itsjeel01.finsiblefrontend.data.model.TransactionType

object CustomColors {
    val lightModeColors = mapOf(
        CustomColorKey.Income to Color(0xFF2D995B),
        CustomColorKey.Expense to Color(0xFFC26619),
        CustomColorKey.Transfer to Color(0xFF1976B8),
        CustomColorKey.SecondaryBackground to Color(0xFFF5F5F5),
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
        CustomColorKey.OnboardingGradientColor2 to Color(0xFFDEE3E3),
        CustomColorKey.YELLOW to Color(0xFFDC9000),
        CustomColorKey.ORANGE to Color(0xFFE65100),
        CustomColorKey.RED to Color(0xFFC62828),
        CustomColorKey.PINK to Color(0xFFC218A6),
        CustomColorKey.PURPLE to Color(0xFF7B1FA2),
        CustomColorKey.BLUE to Color(0xFF1565C0),
        CustomColorKey.GREEN to Color(0xFF2E7D32),
        CustomColorKey.GRAY to Color(0xFF455A64)
    )

    val darkModeColors = mapOf(
        CustomColorKey.Income to Color(0xFF70AF8B),
        CustomColorKey.Expense to Color(0xFFD78549),
        CustomColorKey.Transfer to Color(0xFF6EA0CB),
        CustomColorKey.SecondaryBackground to Color(0xFF222222),
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
        CustomColorKey.OnboardingGradientColor2 to Color(0xFF050E0D),
        CustomColorKey.YELLOW to Color(0xFFFFEF74),
        CustomColorKey.ORANGE to Color(0xFFFFBC5F),
        CustomColorKey.RED to Color(0xFFFA7979),
        CustomColorKey.PINK to Color(0xFFFC6EC2),
        CustomColorKey.PURPLE to Color(0xFFC97BFA),
        CustomColorKey.BLUE to Color(0xFF64B5F6),
        CustomColorKey.GREEN to Color(0xFF81C784),
        CustomColorKey.GRAY to Color(0xFFB0BEC5)
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
    val colors = listOf(
        CustomColorKey.YELLOW,
        CustomColorKey.ORANGE,
        CustomColorKey.RED,
        CustomColorKey.PINK,
        CustomColorKey.PURPLE,
        CustomColorKey.BLUE,
        CustomColorKey.GREEN,
        CustomColorKey.GRAY
    )
    return colors.map { getCustomColor(it) }
}

@Composable
fun getCategoryColor(color: String): Color {
    val colorKey = runCatching {
        CustomColorKey.valueOf(color.toUpperCase(Locale.current))
    }.getOrElse {
        CustomColorKey.GRAY
    }

    return getCustomColor(key = colorKey)
}

@Composable
fun getTransactionColor(type: TransactionType): Color {
    return when (type) {
        TransactionType.INCOME -> getCustomColor(key = CustomColorKey.Income)
        TransactionType.EXPENSE -> getCustomColor(key = CustomColorKey.Expense)
        TransactionType.TRANSFER -> getCustomColor(key = CustomColorKey.Transfer)
    }
}