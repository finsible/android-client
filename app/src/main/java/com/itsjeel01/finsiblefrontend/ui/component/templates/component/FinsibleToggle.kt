package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleIconPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleToggleDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleToggleArrangement
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleToggleColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleToggleLabelPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleToggleSizes

/** Templatised toggle (switch) with optional leading icon label.
 *
 * @param checked Whether the toggle is checked.
 * @param onCheckedChange Called when the checked state changes.
 * @param modifier The modifier to be applied to the toggle.
 * @param label The label to be displayed next to the toggle.
 * @param enabled Whether the toggle is enabled.
 * @param size The size of the toggle.
 * @param colors The colors to be used for the toggle.
 * @param sizes The sizes to be used for the toggle.
 * @param labelIcon The icon to be displayed next to the label.
 * @param labelPosition The position of the label relative to the toggle.
 * @param labelIconPosition The position of the label icon relative to the label.
 * @param arrangement The arrangement of the toggle and label.
 * @param contentDescription The content description of the toggle.
 **/
@Composable
fun FinsibleToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true,
    size: FinsibleSize = FinsibleSize.Medium,
    colors: FinsibleToggleColors = FinsibleToggleDefaults.colors(),
    sizes: FinsibleToggleSizes = FinsibleToggleDefaults.sizes(size),
    labelIcon: (@Composable () -> Unit)? = null,
    labelPosition: FinsibleToggleLabelPosition = FinsibleToggleLabelPosition.Trailing,
    labelIconPosition: FinsibleIconPosition = FinsibleIconPosition.Leading,
    arrangement: FinsibleToggleArrangement = FinsibleToggleArrangement.Attached,
    contentDescription: String? = null
) {
    val interactionSource = remember { MutableInteractionSource() }

    val trackColor by animateColorAsState(
        targetValue = when {
            enabled && checked -> colors.trackOnColor
            enabled && !checked -> colors.trackOffColor
            else -> colors.disabledTrackColor
        },
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "trackColor"
    )
    val thumbColor by animateColorAsState(
        targetValue = when {
            enabled && checked -> colors.thumbOnColor
            enabled && !checked -> colors.thumbOffColor
            else -> colors.disabledThumbColor
        },
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "thumbColor"
    )

    val labelColor by animateColorAsState(
        targetValue = if (enabled) colors.labelColor else colors.disabledLabelColor,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "labelColor"
    )

    val stateDescription = if (checked) {
        stringResource(R.string.finsible_toggle_on_state)
    } else {
        stringResource(R.string.finsible_toggle_off_state)
    }

    val icon: (@Composable () -> Unit) = {
        Box(modifier = Modifier.size(sizes.iconSize)) {
            labelIcon?.invoke()
        }
    }

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = sizes.height)
            .let { base -> if (arrangement == FinsibleToggleArrangement.SpaceBetween) base.fillMaxWidth() else base }
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                interactionSource = interactionSource,
                indication = null,
                onValueChange = onCheckedChange
            )
            .semantics {
                this.stateDescription = stateDescription
                if (!contentDescription.isNullOrEmpty()) this.contentDescription = contentDescription
                if (!enabled) disabled()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (arrangement == FinsibleToggleArrangement.SpaceBetween) Arrangement.SpaceBetween else Arrangement.spacedBy(sizes.spacing)
    ) {
        val labelContent: @Composable () -> Unit = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(sizes.spacing)
            ) {
                if (labelIcon != null && labelIconPosition == FinsibleIconPosition.Leading) {
                    CompositionLocalProvider(LocalContentColor provides labelColor) { icon() }
                }

                label?.let {
                    FinsibleText(
                        text = it,
                        variant = FinsibleTextVariant.SmallBodyMedium,
                        color = labelColor,
                        textStyleOverride = sizes.labelStyle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (labelIcon != null && labelIconPosition == FinsibleIconPosition.Trailing) {
                    CompositionLocalProvider(LocalContentColor provides labelColor) { icon() }
                }
            }
        }

        val toggleContent: @Composable () -> Unit = {
            Box(
                modifier = Modifier
                    .size(width = sizes.width, height = sizes.height)
                    .clip(CircleShape)
                    .background(trackColor)
                    .border(width = sizes.padding, color = Color.Transparent, shape = CircleShape)
            ) {
                val travel = sizes.width - (sizes.padding * 2 + sizes.thumbDiameter)
                val offsetX by animateDpAsState(
                    targetValue = if (checked) travel else 0.dp,
                    animationSpec = spring(stiffness = Spring.StiffnessMedium),
                    label = "thumbOffset"
                )
                val density = LocalDensity.current

                Box(
                    modifier = Modifier
                        .padding(sizes.padding)
                        .size(sizes.thumbDiameter)
                        .align(Alignment.CenterStart)
                        .offset {
                            with(density) { IntOffset(x = offsetX.roundToPx(), y = 0) }
                        }
                        .clip(CircleShape)
                        .background(thumbColor)
                )
            }
        }

        if (labelPosition == FinsibleToggleLabelPosition.Leading) {
            if (!label.isNullOrBlank() || labelIcon != null) {
                labelContent()
            }
            toggleContent()
        } else {
            toggleContent()
            if (!label.isNullOrBlank() || labelIcon != null) {
                labelContent()
            }
        }
    }
}

// clickableWithoutRipple removed; inline clickable used above


