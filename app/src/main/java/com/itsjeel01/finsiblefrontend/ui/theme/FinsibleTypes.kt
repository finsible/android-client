package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

data class FinsibleTypes(
    val displayXLarge: TextStyle,
    val displayLarge: TextStyle,
    val displayMedium: TextStyle,
    val displaySmall: TextStyle,
    val displayXSmall: TextStyle,

    val headingXLarge: TextStyle,
    val headingLarge: TextStyle,
    val headingMedium: TextStyle,
    val headingSmall: TextStyle,
    val headingXSmall: TextStyle,

    val uiXLarge: TextStyle,
    val uiLarge: TextStyle,
    val uiMedium: TextStyle,
    val uiSmall: TextStyle,
    val uiXSmall: TextStyle,

    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle
) {
    companion object {
        val materialTypography = Typography()

        val types = FinsibleTypes(
            displayXLarge = materialTypography.displayLarge.copy(
                fontFamily = FinsibleFontFamily.displayFont,
                fontSize = 64.sp,
                fontWeight = FontWeight.Companion.Normal,
                lineHeight = 72.sp,
                letterSpacing = (-0.02).em
            ),
            displayLarge = materialTypography.displayMedium.copy(
                fontFamily = FinsibleFontFamily.displayFont,
                fontSize = 56.sp,
                fontWeight = FontWeight.Companion.Normal,
                lineHeight = 64.sp,
                letterSpacing = (-0.01).em
            ),
            displayMedium = materialTypography.displaySmall.copy(
                fontFamily = FinsibleFontFamily.displayFont,
                fontSize = 48.sp,
                fontWeight = FontWeight.Companion.Normal,
                lineHeight = 56.sp,
                letterSpacing = 0.em
            ),
            displaySmall = materialTypography.displaySmall.copy(
                fontFamily = FinsibleFontFamily.displayFont,
                fontSize = 32.sp,
                fontWeight = FontWeight.Companion.Normal,
                lineHeight = 40.sp,
                letterSpacing = 0.em
            ),
            displayXSmall = materialTypography.displaySmall.copy(
                fontFamily = FinsibleFontFamily.displayFont,
                fontSize = 28.sp,
                fontWeight = FontWeight.Companion.Normal,
                lineHeight = 36.sp,
                letterSpacing = 0.em
            ),
            headingXLarge = materialTypography.headlineLarge.copy(
                fontFamily = FinsibleFontFamily.interfaceFont,
                fontSize = 48.sp,
                fontWeight = FontWeight.Companion.SemiBold,
                lineHeight = 56.sp,
                letterSpacing = (-0.01).em
            ),
            headingLarge = materialTypography.headlineLarge.copy(
                fontFamily = FinsibleFontFamily.interfaceFont,
                fontSize = 40.sp,
                fontWeight = FontWeight.Companion.SemiBold,
                lineHeight = 48.sp,
                letterSpacing = 0.em
            ),
            headingMedium = materialTypography.headlineMedium.copy(
                fontFamily = FinsibleFontFamily.interfaceFont,
                fontSize = 32.sp,
                fontWeight = FontWeight.Companion.SemiBold,
                lineHeight = 40.sp,
                letterSpacing = 0.em
            ),
            headingSmall = materialTypography.headlineSmall.copy(
                fontFamily = FinsibleFontFamily.interfaceFont,
                fontSize = 24.sp,
                fontWeight = FontWeight.Companion.SemiBold,
                lineHeight = 32.sp,
                letterSpacing = 0.em
            ),
            headingXSmall = materialTypography.headlineSmall.copy(
                fontFamily = FinsibleFontFamily.interfaceFont,
                fontSize = 16.sp,
                fontWeight = FontWeight.Companion.SemiBold,
                lineHeight = 24.sp,
                letterSpacing = 0.01.em
            ),
            uiXLarge = materialTypography.titleLarge.copy(
                fontFamily = FinsibleFontFamily.interfaceFont,
                fontSize = 28.sp,
                fontWeight = FontWeight.Companion.Medium,
                lineHeight = 32.sp,
                letterSpacing = 0.em
            ),
            uiLarge = materialTypography.titleLarge.copy(
                fontFamily = FinsibleFontFamily.interfaceFont,
                fontSize = 24.sp,
                fontWeight = FontWeight.Companion.Medium,
                lineHeight = 28.sp,
                letterSpacing = 0.01.em
            ),
            uiMedium = materialTypography.titleMedium.copy(
                fontFamily = FinsibleFontFamily.interfaceFont,
                fontSize = 20.sp,
                fontWeight = FontWeight.Companion.Medium,
                lineHeight = 24.sp,
                letterSpacing = 0.01.em
            ),
            uiSmall = materialTypography.titleSmall.copy(
                fontFamily = FinsibleFontFamily.interfaceFont,
                fontSize = 16.sp,
                fontWeight = FontWeight.Companion.Medium,
                lineHeight = 16.sp,
                letterSpacing = 0.02.em
            ),
            uiXSmall = materialTypography.titleSmall.copy(
                fontFamily = FinsibleFontFamily.interfaceFont,
                fontSize = 12.sp,
                fontWeight = FontWeight.Companion.Medium,
                lineHeight = 12.sp,
                letterSpacing = 0.03.em
            ),
            bodyLarge = materialTypography.bodyLarge.copy(
                fontFamily = FinsibleFontFamily.interfaceFont,
                fontSize = 16.sp,
                fontWeight = FontWeight.Companion.Normal,
                lineHeight = 24.sp,
                letterSpacing = 0.01.em
            ),
            bodyMedium = materialTypography.bodyMedium.copy(
                fontFamily = FinsibleFontFamily.interfaceFont,
                fontSize = 12.sp,
                fontWeight = FontWeight.Companion.Normal,
                lineHeight = 18.sp,
                letterSpacing = 0.02.em
            ),
            bodySmall = materialTypography.bodySmall.copy(
                fontFamily = FinsibleFontFamily.interfaceFont,
                fontSize = 8.sp,
                fontWeight = FontWeight.Companion.Normal,
                lineHeight = 12.sp,
                letterSpacing = 0.03.em
            )
        )
    }
}