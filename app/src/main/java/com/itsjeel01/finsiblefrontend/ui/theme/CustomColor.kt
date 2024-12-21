package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object CustomColors {
    val lightModeColors = mapOf(
        CustomColorKey.Income to Color(0xFF388E3C),
        CustomColorKey.Expense to Color(0xFFD32F2F),
        CustomColorKey.Transfer to Color(0xFF1976D2)
    )

    val darkModeColors = mapOf(
        CustomColorKey.Income to Color(0xFF69F0AE),
        CustomColorKey.Expense to Color(0xFFFF8A80),
        CustomColorKey.Transfer to Color(0xFF82B1FF),
    )
}

@Composable
fun getCustomColor(key: CustomColorKey): Color {
    val colors =
        if (isSystemInDarkTheme()) CustomColors.darkModeColors else CustomColors.lightModeColors
    return colors[key] ?: MaterialTheme.colorScheme.primary
}