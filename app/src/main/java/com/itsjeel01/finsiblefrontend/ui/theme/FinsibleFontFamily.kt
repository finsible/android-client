package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.itsjeel01.finsiblefrontend.R

object FinsibleFontFamily {
    private val googleFontProvider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    private fun manropeFont(weight: FontWeight, style: FontStyle) = Font(
        googleFont = GoogleFont("Manrope"),
        fontProvider = googleFontProvider,
        weight = weight,
        style = style
    )

    val interfaceFont = FontFamily(
        manropeFont(FontWeight.ExtraLight, FontStyle.Normal),
        manropeFont(FontWeight.Light, FontStyle.Normal),
        manropeFont(FontWeight.Normal, FontStyle.Normal),
        manropeFont(FontWeight.Medium, FontStyle.Normal),
        manropeFont(FontWeight.SemiBold, FontStyle.Normal),
        manropeFont(FontWeight.Bold, FontStyle.Normal),
        manropeFont(FontWeight.ExtraBold, FontStyle.Normal),
        manropeFont(FontWeight.ExtraLight, FontStyle.Italic),
        manropeFont(FontWeight.Light, FontStyle.Italic),
        manropeFont(FontWeight.Normal, FontStyle.Italic),
        manropeFont(FontWeight.Medium, FontStyle.Italic),
        manropeFont(FontWeight.SemiBold, FontStyle.Italic),
        manropeFont(FontWeight.Bold, FontStyle.Italic),
        manropeFont(FontWeight.ExtraBold, FontStyle.Italic),
    )

    val displayFont = FontFamily(
        Font(
            googleFont = GoogleFont("Oswald"),
            fontProvider = googleFontProvider
        )
    )
}