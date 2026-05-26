package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material3.minimumInteractiveComponentSize
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleCheckboxDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleCheckboxColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleCheckboxVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.util.finsibleBounceIndication
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleDurations

/** Stateless checkbox component.
 *
 * @param checked Whether the checkbox is currently checked.
 * @param onCheckedChange Called when the user clicks the checkbox, and toggles checked.
 * @param modifier Modifier to be applied to the layout.
 * @param enabled Controls the enabled state of the checkbox
 * @param animateChecking Whether to animate the checkmark when toggling. Disabling this will make the checkmark appear instantly, which can be useful in certain contexts like forms or lists where many checkboxes may be toggled rapidly.
 * @param variant The visual tone of the checkbox.
 * @param size The size of the checkbox.
 * @param shapeVariant The shape of the checkbox.
 * @param colors The colors of the checkbox.
 * @param label The label for the checkbox.
 * @param checkboxContentDescription A content description for the checkbox.
 */
@Composable
fun FinsibleCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    enforceMinTouchTarget: Boolean = true,
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

    val dynamicFloatSpec = if (animateChecking) {
        tween<Float>(durationMillis = FinsibleDurations.values.slideMs)
    } else snap()

    val dynamicColorSpec = if (animateChecking) {
        tween<Color>(durationMillis = FinsibleDurations.values.slideMs)
    } else snap()

    val containerColor by animateColorAsState(
        targetValue = when {
            enabled && checked -> colors.checkedContainerColor
            enabled && !checked -> colors.uncheckedContainerColor
            !enabled && checked -> colors.disabledCheckedContainerColor
            else -> colors.disabledUncheckedContainerColor
        },
        animationSpec = dynamicColorSpec,
        label = "finsible-checkbox-container"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            enabled && checked -> colors.checkedBorderColor
            enabled && !checked -> colors.uncheckedBorderColor
            !enabled && checked -> colors.disabledCheckedBorderColor
            else -> colors.disabledUncheckedBorderColor
        },
        animationSpec = dynamicColorSpec,
        label = "finsible-checkbox-border"
    )

    val checkProgress by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = dynamicFloatSpec,
        label = "finsible-checkbox-progress"
    )

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
            .finsibleBounceIndication(
                interactionSource = interactionSource,
            )
            .let { base -> if (enforceMinTouchTarget) base.minimumInteractiveComponentSize() else base }
            .semantics {
                if (checkboxContentDescription != null) {
                    this.contentDescription = checkboxContentDescription
                }
            }
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Checkbox,
                interactionSource = interactionSource,
                indication = null,
                onValueChange = onCheckedChange
            )
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
            FinsibleText(
                text = label,
                color = resolvedLabelColor,
                textStyle = checkboxSizes.labelTextStyle
            )
        }
    }
}

private const val CHECK_START_X = 0.24f
private const val CHECK_START_Y = 0.53f
private const val CHECK_ELBOW_X = 0.44f
private const val CHECK_ELBOW_Y = 0.73f
private const val CHECK_END_X = 0.76f
private const val CHECK_END_Y = 0.33f
private const val ANIMATION_MIDPOINT = 0.5f

private fun DrawScope.drawCheckmark(
    progress: Float,
    color: Color,
    strokeWidth: Dp
) {
    val start = Offset(x = size.width * CHECK_START_X, y = size.height * CHECK_START_Y)
    val elbow = Offset(x = size.width * CHECK_ELBOW_X, y = size.height * CHECK_ELBOW_Y)
    val end = Offset(x = size.width * CHECK_END_X, y = size.height * CHECK_END_Y)

    val segment1Progress = (progress / ANIMATION_MIDPOINT).coerceIn(0f, 1f)
    val segment2Progress = ((progress - ANIMATION_MIDPOINT) / ANIMATION_MIDPOINT).coerceIn(0f, 1f)

    val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)

    drawLine(
        color = color,
        start = start,
        end = lerp(start, elbow, segment1Progress),
        strokeWidth = stroke.width,
        cap = stroke.cap
    )

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