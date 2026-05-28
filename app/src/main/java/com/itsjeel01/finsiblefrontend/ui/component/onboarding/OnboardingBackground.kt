
package com.itsjeel01.finsiblefrontend.ui.component.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.graphics.Color
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
    val screenWidthPx = with(androidx.compose.ui.platform.LocalDensity.current) { FinsibleTheme.screenWidth.toPx() }
    val gradientRadiusPixels = screenWidthPx * 1.5f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        FinsibleTheme.colors.brandTint,
                        FinsibleTheme.colors.brandTint,
                        FinsibleTheme.colors.brandSubtle,
                        FinsibleTheme.colors.contentInverse,
                        FinsibleTheme.colors.contentInverse,
                    ),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY,
                    tileMode = TileMode.Clamp
                )
            )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        FinsibleTheme.colors.brandAccent,
                        Color.Transparent
                    ),
                    center = Offset(0f, 0f),
                    radius = gradientRadiusPixels,
                    tileMode = TileMode.Clamp
                )
            )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        FinsibleTheme.colors.brandTint,
                        Color.Transparent,
                    ),
                    center = Offset(Float.POSITIVE_INFINITY, 0f),
                    radius = gradientRadiusPixels,
                    tileMode = TileMode.Clamp
                )
            )
    )

    val noise = ImageBitmap.imageResource(R.drawable.noise)
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawImage(
            image = noise,
            dstSize = IntSize(size.width.toInt(), size.height.toInt()),
            alpha = 0.25f,
        )
    }
}
