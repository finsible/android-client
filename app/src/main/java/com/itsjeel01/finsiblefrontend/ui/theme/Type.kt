package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.itsjeel01.finsiblefrontend.R

val googleFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val sansSerifFont = FontFamily(
    Font(
        googleFont = GoogleFont("Outfit"),
        fontProvider = googleFontProvider,
    )
)

val serifFont = FontFamily(
    Font(
        googleFont = GoogleFont("EB Garamond"),
        fontProvider = googleFontProvider,
    )
)

val baseline = Typography() // Default Material3 Typography

val FinsibleTypography = Typography(
    // Display -> Serif
    displayLarge = baseline.displayLarge.copy(fontFamily = serifFont),
    displayMedium = baseline.displayMedium.copy(fontFamily = serifFont),
    displaySmall = baseline.displaySmall.copy(fontFamily = serifFont),

    // Headline -> Sans Serif
    headlineLarge = baseline.headlineLarge.copy(fontFamily = sansSerifFont),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = sansSerifFont),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = sansSerifFont),

    // Title -> Serif
    titleLarge = baseline.titleLarge.copy(fontFamily = sansSerifFont),
    titleMedium = baseline.titleMedium.copy(fontFamily = sansSerifFont),
    titleSmall = baseline.titleSmall.copy(fontFamily = sansSerifFont),

    // Body -> Sans Serif
    bodyLarge = baseline.bodyLarge.copy(fontFamily = sansSerifFont),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = sansSerifFont),
    bodySmall = baseline.bodySmall.copy(fontFamily = sansSerifFont),

    // Label -> Serif
    labelLarge = baseline.labelLarge.copy(fontFamily = sansSerifFont).copy(fontSize = 20.sp),
    labelMedium = baseline.labelMedium.copy(fontFamily = sansSerifFont).copy(fontSize = 14.sp),
    labelSmall = baseline.labelSmall.copy(fontFamily = sansSerifFont).copy(fontSize = 12.sp),
)
