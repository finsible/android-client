package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

data class FinsibleTypes(
    val t72: TextStyle,    // Hero/Marketing
    val t64: TextStyle,    // Large display
    val t56: TextStyle,    // Display
    val t48: TextStyle,    // Big headlines
    val t44: TextStyle,    // Headlines
    val t40: TextStyle,    // Headlines
    val t36: TextStyle,    // Headlines
    val t32: TextStyle,    // Section titles
    val t28: TextStyle,    // Section titles
    val t24: TextStyle,    // Subsections
    val t20: TextStyle,    // Large text
    val t18: TextStyle,    // Large text
    val t16: TextStyle,    // Base text
    val t14: TextStyle,    // Small text
    val t12: TextStyle,    // Fine text
    val t10: TextStyle,    // Micro text
    val t8: TextStyle,     // Nano text
) {
    companion object {
        val materialTypography = Typography()

        val types = with(materialTypography) {
            val displayFont = FinsibleFontFamily.displayFont
            val interfaceFont = FinsibleFontFamily.interfaceFont

            FinsibleTypes(
                t72 = displayLarge.copy(
                    fontFamily = displayFont,
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 80.sp,
                    letterSpacing = (-0.02).em
                ),
                t64 = displayLarge.copy(
                    fontFamily = displayFont,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 72.sp,
                    letterSpacing = (-0.015).em
                ),
                t56 = displayMedium.copy(
                    fontFamily = displayFont,
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 64.sp,
                    letterSpacing = (-0.01).em
                ),
                t48 = displaySmall.copy(
                    fontFamily = interfaceFont,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 56.sp,
                    letterSpacing = (-0.005).em
                ),
                t44 = headlineLarge.copy(
                    fontFamily = interfaceFont,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 52.sp,
                    letterSpacing = 0.em
                ),
                t40 = headlineLarge.copy(
                    fontFamily = interfaceFont,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 48.sp,
                    letterSpacing = 0.em
                ),
                t36 = headlineLarge.copy(
                    fontFamily = interfaceFont,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 44.sp,
                    letterSpacing = 0.em
                ),
                t32 = headlineLarge.copy(
                    fontFamily = interfaceFont,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 40.sp,
                    letterSpacing = 0.em
                ),
                t28 = headlineMedium.copy(
                    fontFamily = interfaceFont,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 36.sp,
                    letterSpacing = 0.em
                ),
                t24 = headlineMedium.copy(
                    fontFamily = interfaceFont,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 32.sp,
                    letterSpacing = 0.em
                ),
                t20 = headlineSmall.copy(
                    fontFamily = interfaceFont,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 28.sp,
                    letterSpacing = 0.01.em
                ),
                t18 = bodyLarge.copy(
                    fontFamily = interfaceFont,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 24.sp,
                    letterSpacing = 0.01.em
                ),
                t16 = bodyLarge.copy(
                    fontFamily = interfaceFont,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 24.sp,
                    letterSpacing = 0.01.em
                ),
                t14 = bodyMedium.copy(
                    fontFamily = interfaceFont,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 20.sp,
                    letterSpacing = 0.02.em
                ),
                t12 = bodySmall.copy(
                    fontFamily = interfaceFont,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 16.sp,
                    letterSpacing = 0.03.em
                ),
                t10 = labelSmall.copy(
                    fontFamily = interfaceFont,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 14.sp,
                    letterSpacing = 0.04.em
                ),
                t8 = labelSmall.copy(
                    fontFamily = interfaceFont,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 12.sp,
                    letterSpacing = 0.05.em
                )
            )
        }
    }
}

/** Weight modifiers for complete flexibility */
fun TextStyle.extraLight() = this.copy(fontWeight = FontWeight.ExtraLight)
fun TextStyle.light() = this.copy(fontWeight = FontWeight.Light)
fun TextStyle.normal() = this.copy(fontWeight = FontWeight.Normal)
fun TextStyle.medium() = this.copy(fontWeight = FontWeight.Medium)
fun TextStyle.semiBold() = this.copy(fontWeight = FontWeight.SemiBold)
fun TextStyle.bold() = this.copy(fontWeight = FontWeight.Bold)
fun TextStyle.extraBold() = this.copy(fontWeight = FontWeight.ExtraBold)

/** Line height modifiers */
fun TextStyle.tight() = this.copy(lineHeight = this.fontSize * 1.1)
fun TextStyle.standard() = this.copy(lineHeight = this.fontSize * 1.5)
fun TextStyle.loose() = this.copy(lineHeight = this.fontSize * 1.8)

/** Letter spacing modifiers */
fun TextStyle.condensed() = this.copy(letterSpacing = (-0.02).em)
fun TextStyle.expanded() = this.copy(letterSpacing = 0.1.em)

fun TextStyle.displayFont() = this.copy(fontFamily = FinsibleFontFamily.displayFont)
fun TextStyle.interfaceFont() = this.copy(fontFamily = FinsibleFontFamily.interfaceFont)