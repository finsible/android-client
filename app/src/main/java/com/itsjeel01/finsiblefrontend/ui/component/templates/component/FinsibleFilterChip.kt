package com.itsjeel01.finsiblefrontend.ui.component.templates.component
import androidx.compose.ui.unit.dp

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleIconPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleFilterChipDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleFilterChipColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleFilterChipVariant
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleDurations
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** A stateless selectable chip with optional icon support.
 *
 * @param selected Whether the chip is currently checked.
 * @param onSelectedChange Called when the user clicks the chip and toggles checked.
 * @param label The text label content.
 * @param modifier Optional [Modifier] for this chip.
 * @param enabled Controls the enabled state of this chip. When `false`, this chip will not be
 * clickable and will appear disabled to accessibility services.
 * @param size The size of the chip.
 * @param shapeVariant The shape variant of the chip.
 * @param variant The variant of the chip.
 * @param colors The colors of the chip.
 * @param icon The icon content.
 * @param iconPosition The position of the icon relative to the label.
 * @param chipContentDescription A content description for this chip.
 * @param selectedTint The tint to apply to the selected chip.
 * @param inverted Whether the chip should be inverted.
 * @param enforceMinTouchTarget Whether to enforce Material minimum touch target sizing.
 */
@Composable
fun FinsibleFilterChip(
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: FinsibleSize = FinsibleSize.Medium,
    shapeVariant: FinsibleShape = FinsibleShape.Pill,
    variant: FinsibleFilterChipVariant = FinsibleFilterChipVariant.Tonal,
    colors: FinsibleFilterChipColors = FinsibleFilterChipDefaults.colors(variant = variant),
    icon: (@Composable () -> Unit)? = null,
    iconPosition: FinsibleIconPosition = FinsibleIconPosition.Leading,
    chipContentDescription: String? = null,
    selectedTint: Color = Color.Unspecified,
    inverted: Boolean = false,
    enforceMinTouchTarget: Boolean = true
) {
    require(label.isNotBlank()) {
        "label must be non-blank."
    }
    require(size == FinsibleSize.Small || size == FinsibleSize.Medium || size == FinsibleSize.Large) {
        "FinsibleFilterChip supports only Small, Medium, and Large sizes."
    }
    require(shapeVariant == FinsibleShape.Pill || shapeVariant == FinsibleShape.Rounded || shapeVariant == FinsibleShape.Sharp) {
        "FinsibleFilterChip supports only Pill, Rounded, and Sharp shape variants."
    }

    val chipSizes = FinsibleFilterChipDefaults.sizes(size)
    require(chipSizes.iconSize > 0.dp) { "chipSizes.iconSize must be > 0." }
    require(chipSizes.horizontalPadding >= 0.dp) { "chipSizes.horizontalPadding must be >= 0." }
    require(chipSizes.verticalPadding >= 0.dp) { "chipSizes.verticalPadding must be >= 0." }

    val chipShape = FinsibleFilterChipDefaults.shape(shapeVariant, chipSizes)
    val selectedTintContentColor = FinsibleFilterChipDefaults.selectedTintContentColor(inverted)
    val resolvedColors = FinsibleFilterChipDefaults.applySelectedTint(
        colors = colors,
        selectedTint = selectedTint,
        variant = variant,
        selectedContentColor = selectedTintContentColor
    )

    val chipStateDescription = if (selected) {
        stringResource(R.string.finsible_filter_chip_selected_state)
    } else {
        stringResource(R.string.finsible_filter_chip_unselected_state)
    }

    val containerColor by animateColorAsState(
        targetValue = when {
            enabled && selected -> resolvedColors.selectedContainerColor
            enabled && !selected -> resolvedColors.unselectedContainerColor
            !enabled && selected -> resolvedColors.disabledSelectedContainerColor
            else -> resolvedColors.disabledUnselectedContainerColor
        },
        label = "chipContainerColor"
    )

    val labelColor by animateColorAsState(
        targetValue = when {
            enabled && selected -> resolvedColors.selectedLabelColor
            enabled && !selected -> resolvedColors.unselectedLabelColor
            !enabled && selected -> resolvedColors.disabledSelectedLabelColor
            else -> resolvedColors.disabledUnselectedLabelColor
        },
        label = "labelColor"
    )

    val labelFontWeight by animateIntAsState(
        animationSpec = tween(durationMillis = FinsibleDurations.values.focusMs, easing = FastOutLinearInEasing),
        targetValue = if (selected) FontWeight.SemiBold.weight else FontWeight.Normal.weight,
        label = "labelFontWeight"
    )
    val labelTextStyle = chipSizes.textStyle.copy(fontWeight = FontWeight(labelFontWeight.toInt()))

    val iconTint = when {
        enabled && selected -> resolvedColors.selectedIconTint
        enabled && !selected -> resolvedColors.unselectedIconTint
        !enabled && selected -> resolvedColors.disabledSelectedIconTint
        else -> resolvedColors.disabledUnselectedIconTint
    }

    val borderColor = when {
        enabled && selected -> resolvedColors.selectedBorderColor
        enabled && !selected -> resolvedColors.unselectedBorderColor
        !enabled && selected -> resolvedColors.disabledSelectedBorderColor
        else -> resolvedColors.disabledUnselectedBorderColor
    }

    val interactionSource = remember { MutableInteractionSource() }
    val sizeModifier = if (enforceMinTouchTarget) {
        Modifier.minimumInteractiveComponentSize()
    } else {
        Modifier
    }

    Row(
        modifier = modifier
            .then(sizeModifier)
            .clip(chipShape)
            .background(containerColor)
            .border(chipSizes.borderWidth, borderColor, chipShape)
            .toggleable(
                value = selected,
                enabled = enabled,
                role = Role.Checkbox,
                interactionSource = interactionSource,
                indication = ripple(color = resolvedColors.rippleColor),
                onValueChange = onSelectedChange
            )
            .semantics {
                stateDescription = chipStateDescription
                if (!enabled) {
                    disabled()
                }
                chipContentDescription?.let { contentDescription = it }
            }
            .padding(horizontal = chipSizes.horizontalPadding, vertical = chipSizes.verticalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (icon != null && iconPosition == FinsibleIconPosition.Leading) {
            CompositionLocalProvider(LocalContentColor provides iconTint) {
                Box(
                    modifier = Modifier.size(chipSizes.iconSize),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }
            }
            Spacer(modifier = Modifier.width(chipSizes.iconSpacing))
        }

        FinsibleText(
            text = label,
            color = labelColor,
            textStyle = labelTextStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        if (icon != null && iconPosition == FinsibleIconPosition.Trailing) {
            Spacer(modifier = Modifier.width(chipSizes.iconSpacing))
            CompositionLocalProvider(LocalContentColor provides iconTint) {
                Box(
                    modifier = Modifier.size(chipSizes.iconSize),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }
            }
        }
    }
}





