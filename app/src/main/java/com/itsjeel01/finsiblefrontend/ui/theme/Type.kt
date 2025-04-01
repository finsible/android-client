package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.itsjeel01.finsiblefrontend.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val sansSerifFont = FontFamily(
    Font(
        googleFont = GoogleFont("Outfit"),
        fontProvider = provider,
    )
)

val serifFont = FontFamily(
    Font(
        googleFont = GoogleFont("EB Garamond"),
        fontProvider = provider,
    )
)

// Default Material 3 typography values
val baseline = Typography()

val AppTypography = Typography(
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
    labelLarge = baseline.labelLarge.copy(fontFamily = sansSerifFont).copy(fontSize = 22.sp),
    labelMedium = baseline.labelMedium.copy(fontFamily = sansSerifFont).copy(fontSize = 16.sp),
    labelSmall = baseline.labelSmall.copy(fontFamily = sansSerifFont).copy(fontSize = 14.sp),
)