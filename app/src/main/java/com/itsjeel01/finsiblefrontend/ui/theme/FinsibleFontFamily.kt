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

    private fun interFont(weight: FontWeight, style: FontStyle) = Font(
        googleFont = GoogleFont("Manrope"),
        fontProvider = googleFontProvider,
        weight = weight,
        style = style
    )

    val interfaceFont = FontFamily(
        interFont(FontWeight.ExtraLight, FontStyle.Normal),
        interFont(FontWeight.Light, FontStyle.Normal),
        interFont(FontWeight.Normal, FontStyle.Normal),
        interFont(FontWeight.Medium, FontStyle.Normal),
        interFont(FontWeight.SemiBold, FontStyle.Normal),
        interFont(FontWeight.Bold, FontStyle.Normal),
        interFont(FontWeight.ExtraBold, FontStyle.Normal),
        interFont(FontWeight.ExtraLight, FontStyle.Italic),
        interFont(FontWeight.Light, FontStyle.Italic),
        interFont(FontWeight.Normal, FontStyle.Italic),
        interFont(FontWeight.Medium, FontStyle.Italic),
        interFont(FontWeight.SemiBold, FontStyle.Italic),
        interFont(FontWeight.Bold, FontStyle.Italic),
        interFont(FontWeight.ExtraBold, FontStyle.Italic),
    )

    val displayFont = FontFamily(
        Font(
            googleFont = GoogleFont("Oswald"),
            fontProvider = googleFontProvider
        )
    )
}