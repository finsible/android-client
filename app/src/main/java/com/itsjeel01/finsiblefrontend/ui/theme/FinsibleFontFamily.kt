package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.itsjeel01.finsiblefrontend.R

object FinsibleFontFamily {
    private val googleFontProvider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    val interfaceFont = FontFamily(
        Font(
            googleFont = GoogleFont("Inter"),
            fontProvider = googleFontProvider,
        )
    )

    val displayFont = FontFamily(
        Font(
            googleFont = GoogleFont("Oswald"),
            fontProvider = googleFontProvider,
        )
    )
}