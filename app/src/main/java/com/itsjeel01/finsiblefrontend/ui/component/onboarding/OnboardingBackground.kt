package com.itsjeel01.finsiblefrontend.ui.component.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntSize
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun OnboardingBackground() {
    // Vertical gradient
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        FinsibleTheme.colors.brandAccent40,
                        FinsibleTheme.colors.brandAccent20,
                        FinsibleTheme.colors.brandAccent10,
                        FinsibleTheme.colors.same,
                        FinsibleTheme.colors.same,
                    ),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY,
                    tileMode = TileMode.Clamp
                )
            )
    )

    // TopLeft radial gradient
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        FinsibleTheme.colors.brandAccent50,
                        FinsibleTheme.colors.transparent
                    ),
                    center = Offset(0f, 0f),
                    radius = FinsibleTheme.dimes.d800.value,
                    tileMode = TileMode.Clamp
                )
            )
    )

    // TopRight radial gradient
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        FinsibleTheme.colors.brandAccent20,
                        FinsibleTheme.colors.transparent,
                    ),
                    center = Offset(Float.POSITIVE_INFINITY, 0f),
                    radius = FinsibleTheme.dimes.d800.value,
                    tileMode = TileMode.Clamp
                )
            )
    )

    // Noise overlay
    val noise = ImageBitmap.imageResource(R.drawable.noise)
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawImage(
            image = noise,
            dstSize = IntSize(size.width.toInt(), size.height.toInt()),
            alpha = 0.25f,
        )
    }
}
