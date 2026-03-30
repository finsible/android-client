package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleIconPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleFilterChipDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleFilterChipColors

/** A stateless selectable chip with optional icon support. */
@Composable
fun FinsibleFilterChip(
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: FinsibleSize = FinsibleSize.Medium,
    shapeVariant: FinsibleShape = FinsibleShape.Pill,
    colors: FinsibleFilterChipColors = FinsibleFilterChipDefaults.colors(),
    icon: (@Composable () -> Unit)? = null,
    iconPosition: FinsibleIconPosition = FinsibleIconPosition.Leading,
    chipContentDescription: String? = null
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
    require(!(icon == null && iconPosition != FinsibleIconPosition.Leading)) {
        "iconPosition has no effect when no icon is provided."
    }

    val chipSizes = FinsibleFilterChipDefaults.sizes(size)
    val chipShape = FinsibleFilterChipDefaults.shape(shapeVariant, chipSizes)

    val chipStateDescription = if (selected) {
        stringResource(R.string.finsible_filter_chip_selected_state)
    } else {
        stringResource(R.string.finsible_filter_chip_unselected_state)
    }

    val containerColor = when {
        enabled && selected -> colors.selectedContainerColor
        enabled && !selected -> colors.unselectedContainerColor
        !enabled && selected -> colors.disabledSelectedContainerColor
        else -> colors.disabledUnselectedContainerColor
    }

    val labelColor = when {
        enabled && selected -> colors.selectedLabelColor
        enabled && !selected -> colors.unselectedLabelColor
        !enabled && selected -> colors.disabledSelectedLabelColor
        else -> colors.disabledUnselectedLabelColor
    }

    val iconTint = when {
        enabled && selected -> colors.selectedIconTint
        enabled && !selected -> colors.unselectedIconTint
        !enabled && selected -> colors.disabledSelectedIconTint
        else -> colors.disabledUnselectedIconTint
    }

    val borderColor = when {
        enabled && selected -> colors.selectedBorderColor
        enabled && !selected -> colors.unselectedBorderColor
        !enabled && selected -> colors.disabledSelectedBorderColor
        else -> colors.disabledUnselectedBorderColor
    }

    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = chipSizes.height)
            .clip(chipShape)
            .background(containerColor)
            .border(chipSizes.borderWidth, borderColor, chipShape)
            .toggleable(
                value = selected,
                enabled = enabled,
                role = Role.Checkbox,
                interactionSource = interactionSource,
                indication = ripple(color = colors.rippleColor),
                onValueChange = onSelectedChange
            )
            .semantics {
                stateDescription = chipStateDescription
                if (!enabled) {
                    disabled()
                }
                chipContentDescription?.let { contentDescription = it }
            }
            .padding(chipSizes.contentPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
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

        Text(
            text = label,
            style = chipSizes.textStyle,
            color = labelColor,
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





