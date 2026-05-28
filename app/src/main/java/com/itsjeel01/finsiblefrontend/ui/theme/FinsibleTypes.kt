package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

/**
 * Finsible typography — semantic role-based tokens.
 *
 * ### Roles
 * - **display.***   Oswald, Normal weight — hero numbers, net worth totals
 * - **heading.***   Manrope, SemiBold — screen titles, card/dialog headings
 * - **body.***      Manrope, Normal weight — transaction names, descriptions, fine print
 * - **label.***     Manrope, Medium weight — buttons, tags, micro-labels
 * - **caption**     Manrope, Normal weight — timestamps, helper text
 * - **numeral.***   Manrope, tabular-nums enforced — amounts in cards, lists, tables
 */
@Immutable
data class FinsibleTypes(
    // Display — Oswald
    val displayXl: TextStyle,
    val displayLg: TextStyle,
    val displayMd: TextStyle,
    val displaySm: TextStyle,

    // Heading — Manrope SemiBold
    val headingLg: TextStyle,
    val headingMd: TextStyle,
    val headingSm: TextStyle,

    // Body — Manrope Normal
    val bodyLg: TextStyle,
    val bodyMd: TextStyle,
    val bodySm: TextStyle,

    // Label — Manrope Medium
    val labelLg: TextStyle,
    val labelMd: TextStyle,
    val labelSm: TextStyle,

    // Caption
    val caption: TextStyle,

    // Numeral — Manrope, tabular-nums enforced
    val numeralXl: TextStyle,
    val numeralLg: TextStyle,
    val numeralMd: TextStyle,
    val numeralSm: TextStyle,
    val numeralXs: TextStyle,
) {
    companion object {
        val materialTypography = Typography()

        val values = with(materialTypography) {
            val displayFont = FinsibleFontFamily.displayFont
            val interfaceFont = FinsibleFontFamily.interfaceFont

            FinsibleTypes(
                // Display — Oswald, Normal
                displayXl = TextStyle(
                    fontFamily = displayFont,
                    fontSize = 48.sp, fontWeight = FontWeight.Normal,
                    lineHeight = 54.sp, letterSpacing = (-0.02).em
                ),
                displayLg = TextStyle(
                    fontFamily = displayFont,
                    fontSize = 36.sp, fontWeight = FontWeight.Normal,
                    lineHeight = 42.sp, letterSpacing = (-0.015).em
                ),
                displayMd = TextStyle(
                    fontFamily = displayFont,
                    fontSize = 28.sp, fontWeight = FontWeight.Normal,
                    lineHeight = 34.sp, letterSpacing = (-0.01).em
                ),
                displaySm = TextStyle(
                    fontFamily = displayFont,
                    fontSize = 22.sp, fontWeight = FontWeight.Normal,
                    lineHeight = 28.sp, letterSpacing = 0.em
                ),

                // Heading — Manrope SemiBold
                headingLg = TextStyle(
                    fontFamily = interfaceFont,
                    fontSize = 22.sp, fontWeight = FontWeight.SemiBold,
                    lineHeight = 30.sp, letterSpacing = 0.em
                ),
                headingMd = TextStyle(
                    fontFamily = interfaceFont,
                    fontSize = 18.sp, fontWeight = FontWeight.SemiBold,
                    lineHeight = 26.sp, letterSpacing = 0.em
                ),
                headingSm = TextStyle(
                    fontFamily = interfaceFont,
                    fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                    lineHeight = 22.sp, letterSpacing = 0.em
                ),

                // Body — Manrope Normal
                bodyLg = TextStyle(
                    fontFamily = interfaceFont,
                    fontSize = 16.sp, fontWeight = FontWeight.Normal,
                    lineHeight = 24.sp, letterSpacing = 0.01.em
                ),
                bodyMd = TextStyle(
                    fontFamily = interfaceFont,
                    fontSize = 14.sp, fontWeight = FontWeight.Normal,
                    lineHeight = 20.sp, letterSpacing = 0.02.em
                ),
                bodySm = TextStyle(
                    fontFamily = interfaceFont,
                    fontSize = 12.sp, fontWeight = FontWeight.Normal,
                    lineHeight = 16.sp, letterSpacing = 0.03.em
                ),

                // Label — Manrope Medium
                labelLg = TextStyle(
                    fontFamily = interfaceFont,
                    fontSize = 14.sp, fontWeight = FontWeight.Medium,
                    lineHeight = 20.sp, letterSpacing = 0.01.em
                ),
                labelMd = TextStyle(
                    fontFamily = interfaceFont,
                    fontSize = 12.sp, fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp, letterSpacing = 0.04.em
                ),
                labelSm = TextStyle(
                    fontFamily = interfaceFont,
                    fontSize = 10.sp, fontWeight = FontWeight.Medium,
                    lineHeight = 14.sp, letterSpacing = 0.06.em
                ),

                // Caption
                caption = TextStyle(
                    fontFamily = interfaceFont,
                    fontSize = 11.sp, fontWeight = FontWeight.Normal,
                    lineHeight = 15.sp, letterSpacing = 0.03.em
                ),

                // Numeral — Manrope with tabular-nums
                numeralXl = TextStyle(
                    fontFamily = interfaceFont,
                    fontSize = 40.sp, fontWeight = FontWeight.SemiBold,
                    lineHeight = 48.sp, letterSpacing = (-0.01).em,
                    fontFeatureSettings = "tnum"
                ),
                numeralLg = TextStyle(
                    fontFamily = interfaceFont,
                    fontSize = 32.sp, fontWeight = FontWeight.SemiBold,
                    lineHeight = 40.sp, letterSpacing = 0.em,
                    fontFeatureSettings = "tnum"
                ),
                numeralMd = TextStyle(
                    fontFamily = interfaceFont,
                    fontSize = 20.sp, fontWeight = FontWeight.Medium,
                    lineHeight = 28.sp, letterSpacing = 0.em,
                    fontFeatureSettings = "tnum"
                ),
                numeralSm = TextStyle(
                    fontFamily = interfaceFont,
                    fontSize = 14.sp, fontWeight = FontWeight.Medium,
                    lineHeight = 20.sp, letterSpacing = 0.01.em,
                    fontFeatureSettings = "tnum"
                ),
                numeralXs = TextStyle(
                    fontFamily = interfaceFont,
                    fontSize = 12.sp, fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp, letterSpacing = 0.02.em,
                    fontFeatureSettings = "tnum"
                ),
            )
        }
    }
}

// ── Weight modifiers ────────────────────────────────────────────────────

fun TextStyle.extraLight() = copy(fontWeight = FontWeight.ExtraLight)
fun TextStyle.light()      = copy(fontWeight = FontWeight.Light)
fun TextStyle.normal()     = copy(fontWeight = FontWeight.Normal)
fun TextStyle.medium()     = copy(fontWeight = FontWeight.Medium)
fun TextStyle.semiBold()   = copy(fontWeight = FontWeight.SemiBold)
fun TextStyle.bold()       = copy(fontWeight = FontWeight.Bold)
fun TextStyle.extraBold()  = copy(fontWeight = FontWeight.ExtraBold)

// ── Line height modifiers ───────────────────────────────────────────────

fun TextStyle.tight()      = copy(lineHeight = fontSize * 1.1)
fun TextStyle.standard()   = copy(lineHeight = fontSize * 1.5)
fun TextStyle.loose()      = copy(lineHeight = fontSize * 1.8)

// ── Letter spacing modifiers ────────────────────────────────────────────

fun TextStyle.condensed()  = copy(letterSpacing = (-0.02).em)
fun TextStyle.relaxed()    = copy(letterSpacing = 0.025.em)
fun TextStyle.expanded()   = copy(letterSpacing = 0.1.em)

// ── Font family modifiers ───────────────────────────────────────────────

fun TextStyle.displayFont()  = copy(fontFamily = FinsibleFontFamily.displayFont)
fun TextStyle.interfaceFont() = copy(fontFamily = FinsibleFontFamily.interfaceFont)
