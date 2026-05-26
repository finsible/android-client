package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleScrubberDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleScrubberColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleScrubberSizes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleScrubberVariant
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleDurations
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/** Stateless interactive scrubber with separate and continuous variations.
 *
 * @param currentIndex The index of the currently selected item.
 * @param totalCount The total number of items in the scrubber.
 * @param onIndexChange Callback to be invoked when the selected index changes.
 * @param modifier The [Modifier] to be applied to this scrubber.
 * @param variant The variant of the scrubber to render.
 * @param enabled Whether the scrubber is enabled.
 * @param sizes The [FinsibleScrubberSizes] to use for this scrubber.
 * @param colors The [FinsibleScrubberColors] to use for this scrubber.
 * @param scrubberContentDescription The content description for this scrubber.
 * @param onScrubEnd Callback to be invoked when the scrubber is scrubbed.
 */
@Composable
fun FinsibleScrubber(
    currentIndex: Int,
    totalCount: Int,
    onIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    variant: FinsibleScrubberVariant = FinsibleScrubberVariant.Separate,
    enabled: Boolean = true,
    sizes: FinsibleScrubberSizes = FinsibleScrubberDefaults.sizes(),
    colors: FinsibleScrubberColors = FinsibleScrubberDefaults.colors(),
    scrubberContentDescription: String? = null,
    onScrubEnd: (() -> Unit)? = null
) {
    require(totalCount > 0) { "totalCount must be greater than 0." }
    require(currentIndex in 0 until totalCount) {
        "currentIndex must be within [0, totalCount)."
    }
    require(sizes.activeBarWidth > Dp.Hairline) { "activeBarWidth must be greater than 0.dp." }
    require(sizes.inactiveBarWidth > Dp.Hairline) { "inactiveBarWidth must be greater than 0.dp." }
    require(sizes.barHeight > Dp.Hairline) { "barHeight must be greater than 0.dp." }
    require(sizes.barSpacing >= 0.dp) { "barSpacing must be non-negative." }
    require(sizes.cornerRadius >= 0.dp) { "cornerRadius must be non-negative." }

    val minValue = 0f
    val maxValue = (totalCount - 1).toFloat()
    val stateText = stringResource(
        id = R.string.finsible_scrubber_state_description,
        currentIndex + 1,
        totalCount
    )
    val resolvedContentDescription = scrubberContentDescription
        ?: stringResource(R.string.finsible_scrubber_content_description)

    var layoutWidthPx by remember { mutableFloatStateOf(0f) }

    val currIdx by rememberUpdatedState(currentIndex)
    val currOnIdxChange by rememberUpdatedState(onIndexChange)
    val currOnScrubEnd by rememberUpdatedState(onScrubEnd)

    fun updateIndexFromOffset(offsetX: Float) {
        if (!enabled || layoutWidthPx <= 0f || totalCount == 1) return

        val nextIndex = resolveScrubberIndex(offsetX, layoutWidthPx, totalCount)
        if (nextIndex != currIdx) {
            currOnIdxChange(nextIndex)
        }
    }

    val interactionModifier = if (!enabled || totalCount == 1) {
        Modifier
    } else {
        Modifier
            .pointerInput(totalCount, layoutWidthPx) {
                detectTapGestures { tapOffset ->
                    updateIndexFromOffset(tapOffset.x)
                    currOnScrubEnd?.invoke()
                }
            }
            .pointerInput(totalCount, layoutWidthPx) {
                var dragPositionX: Float? = null
                detectDragGestures(
                    onDragStart = { offset ->
                        dragPositionX = offset.x
                        updateIndexFromOffset(offset.x)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val nextX = (dragPositionX ?: change.position.x) + dragAmount.x
                        dragPositionX = nextX
                        updateIndexFromOffset(nextX)
                    },
                    onDragEnd = {
                        dragPositionX = null
                        currOnScrubEnd?.invoke()
                    },
                    onDragCancel = {
                        dragPositionX = null
                        currOnScrubEnd?.invoke()
                    }
                )
            }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(
                minWidth = sizes.minTouchTargetHeight,
                minHeight = sizes.minTouchTargetHeight
            )
            .onSizeChanged { layoutWidthPx = it.width.toFloat() }
            .semantics(mergeDescendants = true) {
                contentDescription = resolvedContentDescription
                stateDescription = stateText
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = currentIndex.toFloat(),
                    range = minValue .. maxValue,
                    steps = max(totalCount - 2, 0)
                )
                setProgress { targetValue ->
                    if (!enabled) return@setProgress false
                    val updated = targetValue.roundToInt().coerceIn(0, totalCount - 1)
                    if (updated != currIdx) {
                        currOnIdxChange(updated)
                    }
                    true
                }
                if (!enabled) disabled()
            }
            .then(interactionModifier)
    ) {
        when (variant) {
            FinsibleScrubberVariant.Separate -> SeparateScrubber(
                currentIndex = currentIndex,
                totalCount = totalCount,
                enabled = enabled,
                sizes = sizes,
                colors = colors,
                maxWidth = maxWidth
            )

            FinsibleScrubberVariant.Continuous -> ContinuousScrubber(
                currentIndex = currentIndex,
                totalCount = totalCount,
                enabled = enabled,
                sizes = sizes,
                colors = colors
            )
        }
    }
}

@Composable
private fun SeparateScrubber(
    currentIndex: Int,
    totalCount: Int,
    enabled: Boolean,
    sizes: FinsibleScrubberSizes,
    colors: FinsibleScrubberColors,
    maxWidth: Dp
) {
    val density = LocalDensity.current
    val gapCount = max(totalCount - 1, 0)

    val rawWidthPx = with(density) {
        sizes.activeBarWidth.toPx() +
                (sizes.inactiveBarWidth.toPx() * gapCount) +
                (sizes.barSpacing.toPx() * gapCount)
    }
    val availableWidthPx = with(density) { maxWidth.toPx() }
    val scaleFactor = if (rawWidthPx > 0f) min(1f, availableWidthPx / rawWidthPx) else 1f

    val activeBarWidth = sizes.activeBarWidth * scaleFactor
    val inactiveBarWidth = sizes.inactiveBarWidth * scaleFactor
    val spacing = sizes.barSpacing * scaleFactor
    val shape = RoundedCornerShape(sizes.cornerRadius)
    val minVisibleBarWidth = FinsibleTheme.stroke.thin

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing, Alignment.CenterHorizontally)
    ) {
        repeat(totalCount) { index ->
            val isCurrent = currentIndex == index
            val targetColor = when {
                enabled && isCurrent -> colors.currentColor
                enabled && !isCurrent -> colors.restColor
                !enabled && isCurrent -> colors.disabledCurrentColor
                else -> colors.disabledRestColor
            }
            val barColor by animateColorAsState(
                targetValue = targetColor,
                animationSpec = tween(FinsibleDurations.values.revealMs),
                label = "scrubber_separate_color"
            )
            val targetWidth = if (isCurrent) activeBarWidth else inactiveBarWidth
            val animatedWidth by animateDpAsState(
                targetValue = if (targetWidth < minVisibleBarWidth) minVisibleBarWidth else targetWidth,
                animationSpec = tween(FinsibleDurations.values.revealMs),
                label = "scrubber_separate_width"
            )

            Box(
                modifier = Modifier
                    .width(animatedWidth)
                    .height(sizes.barHeight)
                    .background(barColor, shape)
            )
        }
    }
}

@Composable
private fun ContinuousScrubber(
    currentIndex: Int,
    totalCount: Int,
    enabled: Boolean,
    sizes: FinsibleScrubberSizes,
    colors: FinsibleScrubberColors
) {
    val restColor = if (enabled) colors.restColor else colors.disabledRestColor
    val currentColor = if (enabled) colors.currentColor else colors.disabledCurrentColor
    val progressTarget = (currentIndex + 1) / totalCount.toFloat()
    val progress by animateFloatAsState(
        targetValue = progressTarget,
        animationSpec = tween(Duration.MS_300.toInt()),
        label = "scrubber_continuous_progress"
    )
    val shape = RoundedCornerShape(sizes.cornerRadius)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(sizes.barHeight)
            .background(restColor, shape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .height(sizes.barHeight)
                .background(currentColor, shape)
        )
    }
}

private fun resolveScrubberIndex(position: Float, widthPx: Float, totalCount: Int): Int {
    if (totalCount <= 1 || widthPx <= 0f) return 0
    val index = (position / widthPx * totalCount).toInt()
    return index.coerceIn(0, totalCount - 1)
}
