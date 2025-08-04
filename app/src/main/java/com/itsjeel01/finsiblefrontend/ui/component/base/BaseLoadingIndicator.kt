package com.itsjeel01.finsiblefrontend.ui.component.base

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.itsjeel01.finsiblefrontend.ui.theme.ColorKey
import com.itsjeel01.finsiblefrontend.ui.theme.dime.IconSize
import com.itsjeel01.finsiblefrontend.ui.theme.dime.Radius
import com.itsjeel01.finsiblefrontend.ui.theme.dime.appDimensions
import com.itsjeel01.finsiblefrontend.ui.theme.dime.cornerRadius
import com.itsjeel01.finsiblefrontend.ui.theme.dime.iconSize
import com.itsjeel01.finsiblefrontend.ui.theme.getCustomColor
import com.itsjeel01.finsiblefrontend.ui.util.Animation
import com.itsjeel01.finsiblefrontend.ui.util.Duration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class LoadingSpeed(val durationMs: Int) {
    SLOW(Duration.MSEC_2000.toInt()),        // For heavy operations like data sync
    NORMAL(Duration.MSEC_1200.toInt()),      // Default for most operations
    FAST(Duration.MSEC_800.toInt()),         // For quick operations like form validation
    INSTANT(Duration.MSEC_400.toInt())       // For immediate feedback
}

@Composable
fun BaseLoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = appDimensions().iconSize(IconSize.XL),
    speed: LoadingSpeed = LoadingSpeed.NORMAL,
    primaryColor: Color = getCustomColor(ColorKey.IndicatorPrimary),
    secondaryColor: Color = MaterialTheme.colorScheme.background
) {
    val dims = appDimensions()
    val progress = remember { Animatable(0f) }
    val barAnimations = remember { List(5) { Animatable(0f) } }

    LaunchedEffect(speed) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = Animation.restartingInfiniteTween(speed.durationMs)
        )
    }

    LaunchedEffect(Unit) {
        barAnimations.forEachIndexed { index, animatable ->
            launch {
                delay(index * Duration.MSEC_100)
                animatable.animateTo(
                    targetValue = 1f,
                    animationSpec = Animation.reversingInfiniteTween(speed.durationMs)
                )
            }
        }
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        val mainBarCornerRadius = with(LocalDensity.current) {
            dims.cornerRadius(Radius.SM).toPx()
        }
        val highlightCornerRadius = with(LocalDensity.current) {
            dims.cornerRadius(Radius.XS).toPx()
        }
        Canvas(modifier = Modifier.size(size)) {
            val center = this.size.center
            val chartWidth = size.toPx() * 0.7f
            val chartHeight = size.toPx() * 0.5f
            val barWidth = chartWidth / 7
            val barSpacing = barWidth * 0.3f

            val barHeights = listOf(0.3f, 0.7f, 0.5f, 0.9f, 0.4f)

            for (i in barHeights.indices) {
                val x = center.x - chartWidth / 2 + i * (barWidth + barSpacing) + barWidth / 2
                val animatedHeight = chartHeight * barHeights[i] * barAnimations[i].value
                val y = center.y + chartHeight / 2 - animatedHeight
                val barColor = lerp(secondaryColor, primaryColor, barAnimations[i].value)

                // Draw shadow
                drawRoundRect(
                    color = Color.Black.copy(alpha = 0.1f),
                    topLeft = Offset(x - barWidth / 2 + 2, y + 2),
                    size = Size(barWidth, animatedHeight),
                    cornerRadius = CornerRadius(mainBarCornerRadius)
                )

                // Draw main bar
                drawRoundRect(
                    color = barColor,
                    topLeft = Offset(x - barWidth / 2, y),
                    size = Size(barWidth, animatedHeight),
                    cornerRadius = CornerRadius(mainBarCornerRadius)
                )

                // Draw highlight
                drawRoundRect(
                    color = barColor.copy(alpha = 0.6f),
                    topLeft = Offset(x - barWidth / 2 + 2, y + 2),
                    size = Size(barWidth - 4, maxOf(0f, animatedHeight - 4)),
                    cornerRadius = CornerRadius(highlightCornerRadius)
                )
            }
        }
    }
}

@Composable
@Preview
fun FullScreenLoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = appDimensions().iconSize(IconSize.XL),
    speed: LoadingSpeed = LoadingSpeed.NORMAL,
    primaryColor: Color = getCustomColor(ColorKey.IndicatorPrimary),
    secondaryColor: Color = MaterialTheme.colorScheme.background
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        BaseLoadingIndicator(
            size = size,
            speed = speed,
            primaryColor = primaryColor,
            secondaryColor = secondaryColor
        )
    }
}