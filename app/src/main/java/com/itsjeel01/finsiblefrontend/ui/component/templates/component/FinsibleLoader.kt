package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleLoaderDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleLoaderColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleLoaderSpeed
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sin

private const val INDICATOR_HEIGHT_RATIO = 0.5f
private const val BAR_THICKNESS_RATIO = 0.09f
private const val BAR_SPACING_RATIO = 0.4f
private const val BAR_POSITION_TALL_RATIO = 0.2f
private const val BAR_POSITION_MEDIUM_RATIO = 0.5f
private const val BAR_POSITION_SHORT_RATIO = 0.8f
private const val BAR_HEIGHT_TALL_RATIO = 0.9f
private const val BAR_HEIGHT_MEDIUM_RATIO = 0.6f
private const val BAR_HEIGHT_SHORT_RATIO = 0.3f
private const val BAR_SHADOW_OFFSET_RATIO = 0.025f
private const val BAR_CORNER_RADIUS_RATIO = 0.3f
private const val BALL_TRAJECTORY_CYCLE_LENGTH = 4f
private const val BALL_LANDING_PHASE_DURATION = 0.1f
private const val BALL_SPRING_DAMPING_FACTOR = 0.85f
private const val BALL_SPRING_STIFFNESS = 0.7f
private const val BALL_ARC_BASE_HEIGHT_RATIO = 0.35f
private const val BALL_ARC_DISTANCE_MULTIPLIER = 0.8f
private const val BALL_ARC_DISTANCE_OFFSET = 0.6f
private const val BALL_SCALE_VARIATION = 0.2f
private const val BALL_SHADOW_ALPHA = 0.15f
private const val BALL_RADIUS_RATIO = 0.06f
private const val BALL_SHADOW_OFFSET_RATIO = 0.025f
private const val BALL_BLUR_RADIUS_MULTIPLIER = 0.9f
private const val BALL_BLUR_ALPHA_MULTIPLIER = 0.24f
private const val BALL_BLUR_Y_OFFSET = 2f
private const val BALL_HIGHLIGHT_SCALE_THRESHOLD = 1.05f
private const val BALL_HIGHLIGHT_RADIUS_MULTIPLIER = 0.6f
private const val BALL_HIGHLIGHT_ALPHA_MULTIPLIER = 0.3f
private const val BALL_HIGHLIGHT_X_OFFSET_MULTIPLIER = 0.2f
private const val BALL_HIGHLIGHT_Y_OFFSET_MULTIPLIER = 0.2f

val BALL_TRAJECTORY = arrayOf(
    2 to 1, // Short->Medium
    1 to 0, // Medium->Tall
    0 to 1, // Tall->Medium
    1 to 2  // Medium->Short
)

private val BAR_POSITION_RATIOS = floatArrayOf(
    BAR_POSITION_TALL_RATIO,
    BAR_POSITION_MEDIUM_RATIO,
    BAR_POSITION_SHORT_RATIO
)

private val BAR_HEIGHT_RATIOS = floatArrayOf(
    BAR_HEIGHT_TALL_RATIO,
    BAR_HEIGHT_MEDIUM_RATIO,
    BAR_HEIGHT_SHORT_RATIO
)

@Composable
fun FinsibleLoader(
    modifier: Modifier = Modifier,
    size: FinsibleSize = FinsibleSize.Medium,
    speed: FinsibleLoaderSpeed = FinsibleLoaderSpeed.Normal,
    colors: FinsibleLoaderColors = FinsibleLoaderDefaults.colors()
) {
    val indicatorSize = FinsibleLoaderDefaults.size(size)

    LoadingAnimation(
        modifier = modifier.size(
            width = indicatorSize,
            height = (indicatorSize.value * INDICATOR_HEIGHT_RATIO).dp
        ),
        speed = speed,
        barColor = colors.barColor,
        ballColor = colors.ballColor
    )
}

@Composable
private fun LoadingAnimation(
    modifier: Modifier = Modifier,
    speed: FinsibleLoaderSpeed,
    barColor: Color,
    ballColor: Color
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(speed) {
        progress.snapTo(0f)

        progress.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(speed.durationMs, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        drawLoadingAnimation(
            progress = progress.value,
            barColor = barColor,
            ballColor = ballColor
        )
    }
}

private fun DrawScope.drawLoadingAnimation(
    progress: Float,
    barColor: Color,
    ballColor: Color
) {
    val canvasWidth = size.width
    val canvasHeight = size.height

    val ballMovementConfig = calculateBallMovement(progress)

    // Pass canvas dimensions instead of the layout config
    val ballPositionOffset = calculateBallPosition(ballMovementConfig, canvasWidth, canvasHeight)
    val ballScaleFactor = calculateBallScale(ballMovementConfig.progress)

    drawBars(canvasWidth, canvasHeight, barColor)
    animateBall(ballPositionOffset, ballScaleFactor, canvasWidth, ballColor)
}

private fun calculateBallMovement(progress: Float): BallMovementConfig {
    val cyclePosition = (progress * BALL_TRAJECTORY_CYCLE_LENGTH) % BALL_TRAJECTORY_CYCLE_LENGTH
    val currentTrajectoryIndex = cyclePosition.toInt()
    val trajectoryStepProgress = cyclePosition - currentTrajectoryIndex

    val (sourceBar, destinationBar) = BALL_TRAJECTORY[currentTrajectoryIndex]

    return if (trajectoryStepProgress < BALL_LANDING_PHASE_DURATION) {
        BallMovementConfig(
            sourceBar = sourceBar,
            destBar = sourceBar,
            progress = 0f,
            isLanding = true
        )
    } else {
        val flightProgress =
            (trajectoryStepProgress - BALL_LANDING_PHASE_DURATION) / (1f - BALL_LANDING_PHASE_DURATION)
        BallMovementConfig(
            sourceBar = sourceBar,
            destBar = destinationBar,
            progress = applyOptimizedSpringEasing(flightProgress),
            isLanding = false
        )
    }
}

private fun getBarX(canvasWidth: Float, barIndex: Int): Float {
    val availableWidth = canvasWidth * (1f - BAR_SPACING_RATIO)
    val leftMargin = BAR_SPACING_RATIO * canvasWidth * 0.5f
    return leftMargin + availableWidth * BAR_POSITION_RATIOS[barIndex]
}

private fun getBarHeight(canvasHeight: Float, barIndex: Int): Float {
    return canvasHeight * BAR_HEIGHT_RATIOS[barIndex]
}

private fun applyOptimizedSpringEasing(progress: Float): Float {
    val dampingFactor = BALL_SPRING_DAMPING_FACTOR
    val stiffness = BALL_SPRING_STIFFNESS

    val springValue = 1f - (1f - progress).pow(stiffness)
    return springValue * (1f - (1f - progress) * (1f - dampingFactor))
}

private fun calculateBallPosition(
    ballMovementState: BallMovementConfig,
    canvasWidth: Float,
    canvasHeight: Float
): Offset {
    val sourceX = getBarX(canvasWidth, ballMovementState.sourceBar)
    val sourceY = canvasHeight - getBarHeight(canvasHeight, ballMovementState.sourceBar)

    if (ballMovementState.isLanding) {
        return Offset(sourceX, sourceY)
    }

    val destinationX = getBarX(canvasWidth, ballMovementState.destBar)
    val destinationY = canvasHeight - getBarHeight(canvasHeight, ballMovementState.destBar)

    val ballX = sourceX + (destinationX - sourceX) * ballMovementState.progress

    val baseArcHeight = canvasHeight * BALL_ARC_BASE_HEIGHT_RATIO
    val distanceMultiplier = abs(ballMovementState.destBar - ballMovementState.sourceBar) * BALL_ARC_DISTANCE_MULTIPLIER + BALL_ARC_DISTANCE_OFFSET
    val arcHeight = baseArcHeight * distanceMultiplier

    val arcProgress = sin(ballMovementState.progress * PI).toFloat()
    val ballY = sourceY + (destinationY - sourceY) * ballMovementState.progress - arcHeight * arcProgress

    return Offset(ballX, ballY)
}

private fun calculateBallScale(trajectoryProgress: Float): Float {
    val scaleVariation = BALL_SCALE_VARIATION
    val bounceIntensity = sin(trajectoryProgress * PI).toFloat()
    return 1f + bounceIntensity * scaleVariation
}

private fun DrawScope.drawBars(
    canvasWidth: Float,
    canvasHeight: Float,
    barColor: Color,
) {
    val shadowColor = Color.Black.copy(alpha = BALL_SHADOW_ALPHA)
    val barThickness = canvasWidth * BAR_THICKNESS_RATIO
    val shadowOffset = canvasWidth * BAR_SHADOW_OFFSET_RATIO
    val cornerRadius = CornerRadius(barThickness * BAR_CORNER_RADIUS_RATIO)

    for (index in 0 .. 2) {
        val xPosition = getBarX(canvasWidth, index)
        val height = getBarHeight(canvasHeight, index)
        val yPosition = canvasHeight - height
        val leftXPosition = xPosition - barThickness / 2f

        val barSize = Size(barThickness, height)

        // Shadow
        drawRoundRect(
            color = shadowColor,
            topLeft = Offset(
                leftXPosition + shadowOffset,
                yPosition + shadowOffset
            ),
            size = barSize,
            cornerRadius = cornerRadius
        )

        // Main bar
        drawRoundRect(
            color = barColor,
            topLeft = Offset(leftXPosition, yPosition),
            size = barSize,
            cornerRadius = cornerRadius
        )
    }
}

private fun DrawScope.animateBall(
    ballPosition: Offset,
    scaleMultiplier: Float,
    canvasWidth: Float,
    ballColor: Color
) {
    val ballRadius = canvasWidth * BALL_RADIUS_RATIO * scaleMultiplier
    val shadowOffset = canvasWidth * BALL_SHADOW_OFFSET_RATIO

    // Conditional motion blur for performance
    if (scaleMultiplier > 1.1f) {
        val blurAlpha = (scaleMultiplier - 1f) * BALL_BLUR_ALPHA_MULTIPLIER
        drawCircle(
            color = ballColor.copy(alpha = blurAlpha),
            radius = ballRadius * BALL_BLUR_RADIUS_MULTIPLIER,
            center = Offset(ballPosition.x, ballPosition.y + BALL_BLUR_Y_OFFSET)
        )
    }

    // Shadow
    drawCircle(
        color = Color.Black.copy(alpha = BALL_SHADOW_ALPHA),
        radius = ballRadius,
        center = Offset(ballPosition.x + shadowOffset, ballPosition.y + shadowOffset)
    )

    // Main ball
    drawCircle(
        color = ballColor,
        radius = ballRadius,
        center = ballPosition
    )

    // Conditional highlight for performance
    if (scaleMultiplier > BALL_HIGHLIGHT_SCALE_THRESHOLD) {
        val highlightAlpha = (scaleMultiplier - 1f) * BALL_HIGHLIGHT_ALPHA_MULTIPLIER
        drawCircle(
            color = Color.White.copy(alpha = highlightAlpha),
            radius = ballRadius * BALL_HIGHLIGHT_RADIUS_MULTIPLIER,
            center = Offset(
                ballPosition.x - ballRadius * BALL_HIGHLIGHT_X_OFFSET_MULTIPLIER,
                ballPosition.y - ballRadius * BALL_HIGHLIGHT_Y_OFFSET_MULTIPLIER
            )
        )
    }
}

private data class BallMovementConfig(
    val sourceBar: Int,
    val destBar: Int,
    val progress: Float,
    val isLanding: Boolean
)