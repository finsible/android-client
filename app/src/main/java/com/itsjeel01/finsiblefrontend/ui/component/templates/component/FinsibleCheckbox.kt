package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleCheckboxDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleCheckboxColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleCheckboxVariant
import com.itsjeel01.finsiblefrontend.ui.constants.Duration

// Checkmark anchor points as fractions of the box size.
// These were tuned visually to sit centered and balanced inside the box at all sizes.
private const val CHECK_START_X = 0.24f  // Left foot of the tick
private const val CHECK_START_Y = 0.53f
private const val CHECK_ELBOW_X = 0.44f  // The bend point — bottom of the short stroke / top of the long stroke
private const val CHECK_ELBOW_Y = 0.73f
private const val CHECK_END_X   = 0.76f  // Right tip of the tick
private const val CHECK_END_Y   = 0.33f

// The animation is split into two equal halves: [0, 0.5) draws the short stroke, [0.5, 1] draws the long stroke.
// This gives the tick a natural "drawing" feel — short arm first, then the long sweep.
private const val ANIMATION_MIDPOINT = 0.5f

/** A stateless semantic checkbox with size, color, and animation controls. */
@Composable
fun FinsibleCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    animateChecking: Boolean = true,
    variant: FinsibleCheckboxVariant = FinsibleCheckboxVariant.Colorful,
    size: FinsibleSize = FinsibleSize.Medium,
    shapeVariant: FinsibleShape = FinsibleShape.Rounded,
    colors: FinsibleCheckboxColors = FinsibleCheckboxDefaults.colors(variant),
    label: String? = null,
    checkboxContentDescription: String? = null
) {
    require(size == FinsibleSize.Small || size == FinsibleSize.Medium || size == FinsibleSize.Large) {
        "FinsibleCheckbox supports only Small, Medium, and Large sizes."
    }
    require(shapeVariant == FinsibleShape.Rounded || shapeVariant == FinsibleShape.Sharp) {
        "FinsibleCheckbox supports only Rounded and Sharp shape variants."
    }
    require(label == null || label.isNotBlank()) {
        "label must be null or non-blank."
    }

    val checkboxSizes = FinsibleCheckboxDefaults.sizes(size)
    val checkboxShape = FinsibleCheckboxDefaults.shape(shapeVariant, size)
    val checkboxStateDescription = if (checked) {
        stringResource(R.string.finsible_checkbox_checked_state)
    } else {
        stringResource(R.string.finsible_checkbox_unchecked_state)
    }

    val animationSpec = if (animateChecking) {
        tween<Float>(durationMillis = Duration.MS_200.toInt())
    } else {
        snap()
    }

    val checkProgress by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = animationSpec,
        label = "finsible-checkbox-progress"
    )

    val containerColor = when {
        enabled && checked -> colors.checkedContainerColor
        enabled && !checked -> colors.uncheckedContainerColor
        !enabled && checked -> colors.disabledCheckedContainerColor
        else -> colors.disabledUncheckedContainerColor
    }

    val borderColor = when {
        enabled && checked -> colors.checkedBorderColor
        enabled && !checked -> colors.uncheckedBorderColor
        !enabled && checked -> colors.disabledCheckedBorderColor
        else -> colors.disabledUncheckedBorderColor
    }

    val checkColor = when {
        enabled && checked -> colors.checkedIconColor
        enabled && !checked -> colors.uncheckedIconColor
        !enabled && checked -> colors.disabledCheckedIconColor
        else -> colors.disabledUncheckedIconColor
    }

    val resolvedLabelColor = if (enabled) colors.labelColor else colors.disabledLabelColor
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Checkbox,
                interactionSource = interactionSource,
                indication = ripple(color = colors.rippleColor),
                onValueChange = onCheckedChange
            )
            .semantics {
                role = Role.Checkbox
                stateDescription = checkboxStateDescription
                if (!enabled) {
                    disabled()
                }
                checkboxContentDescription?.let { contentDescription = it }
            }
    ) {
        Box(
            modifier = Modifier
                .size(checkboxSizes.boxSize)
                .clip(checkboxShape)
                .background(containerColor)
                .border(checkboxSizes.borderWidth, borderColor, checkboxShape)
        ) {
            if (checkProgress > 0f) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCheckmark(
                        progress = checkProgress,
                        color = checkColor,
                        strokeWidth = checkboxSizes.checkStrokeWidth
                    )
                }
            }
        }

        if (label != null) {
            Spacer(modifier = Modifier.width(checkboxSizes.labelSpacing))
            Text(
                text = label,
                style = checkboxSizes.labelTextStyle,
                color = resolvedLabelColor
            )
        }
    }
}

/**
 * Draws an animated two-segment checkmark inside a [DrawScope].
 *
 * The tick is made of two line segments that share a bend point (elbow):
 *   - Segment 1 (short): start → elbow  — the bottom-left downstroke
 *   - Segment 2 (long):  elbow → end    — the top-right upstroke
 *
 * [progress] runs 0→1 and is split at [ANIMATION_MIDPOINT]:
 *   - 0.0–0.5: segment 1 draws from start to elbow
 *   - 0.5–1.0: segment 2 draws from elbow to end
 *
 * Each half is re-normalized to its own 0→1 range so [lerp] always receives
 * a clean fraction regardless of where in the overall animation we are.
 */
private fun DrawScope.drawCheckmark(
    progress: Float,
    color: Color,
    strokeWidth: Dp
) {
    val start  = Offset(x = size.width * CHECK_START_X, y = size.height * CHECK_START_Y)
    val elbow  = Offset(x = size.width * CHECK_ELBOW_X, y = size.height * CHECK_ELBOW_Y)
    val end    = Offset(x = size.width * CHECK_END_X,   y = size.height * CHECK_END_Y)

    // Normalize each animation half independently into 0→1 so lerp gets a clean fraction.
    val segment1Progress = (progress / ANIMATION_MIDPOINT).coerceIn(0f, 1f)
    val segment2Progress = ((progress - ANIMATION_MIDPOINT) / ANIMATION_MIDPOINT).coerceIn(0f, 1f)

    val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)

    // Segment 1 — always draw when progress > 0 (caller already guards this)
    drawLine(
        color = color,
        start = start,
        end = lerp(start, elbow, segment1Progress),
        strokeWidth = stroke.width,
        cap = stroke.cap
    )

    // Segment 2 — only begins once the animation crosses the midpoint
    if (progress > ANIMATION_MIDPOINT) {
        drawLine(
            color = color,
            start = elbow,
            end = lerp(elbow, end, segment2Progress),
            strokeWidth = stroke.width,
            cap = stroke.cap
        )
    }
}