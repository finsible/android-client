package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleAccordionDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleAccordionColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleAccordionIconVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleAccordionSizes
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Expandable accordion supporting chevron or plus/minus affordances.
 * @param title The title of the accordion.
 * @param content The content of the accordion.
 * @param modifier The modifier to apply to the accordion.
 * @param subtitle The subtitle of the accordion.
 * @param expanded Whether the accordion is expanded.
 * @param onExpandedChange Callback invoked when the expanded state changes.
 * @param enabled Whether the accordion is enabled.
 * @param size The size of the accordion.
 * @param shapeVariant The shape variant of the accordion.
 * @param iconVariant The icon variant of the accordion.
 * @param colors The colors of the accordion.
 * @param sizes The sizes of the accordion.
 * @param leadingIcon The leading icon of the accordion.
 * */
@Composable
fun FinsibleAccordion(
    title: String,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    expanded: Boolean? = null,
    onExpandedChange: ((Boolean) -> Unit)? = null,
    enabled: Boolean = true,
    size: FinsibleSize = FinsibleSize.Medium,
    shapeVariant: FinsibleShape = FinsibleShape.Rounded,
    iconVariant: FinsibleAccordionIconVariant = FinsibleAccordionIconVariant.Chevron,
    colors: FinsibleAccordionColors = FinsibleAccordionDefaults.colors(),
    sizes: FinsibleAccordionSizes = FinsibleAccordionDefaults.sizes(size, shapeVariant),
    leadingIcon: (@Composable () -> Unit)? = null
) {
    require(size == FinsibleSize.Small || size == FinsibleSize.Medium || size == FinsibleSize.Large) {
        "Accordion only supports Small, Medium, and Large sizes"
    }
    require(title.isNotBlank()) { "title must be non-blank." }
    require(shapeVariant == FinsibleShape.Rounded || shapeVariant == FinsibleShape.Sharp) {
        "Only Rounded and Sharp shape variants are supported for Accordion"
    }
    require((expanded != null && onExpandedChange != null) || (expanded == null && onExpandedChange == null)) {
        "expanded and onExpandedChange must be set together"
    }

    val interactionSource = remember { MutableInteractionSource() }
    val shape = remember(sizes.cornerRadius) { RoundedCornerShape(sizes.cornerRadius) }

    var internalExpanded by remember { mutableStateOf(false) }
    val resolvedExpanded = expanded ?: internalExpanded

    val onToggle = {
        val next = !resolvedExpanded
        internalExpanded = next
        onExpandedChange?.invoke(next)
    }

    val iconRotation by animateFloatAsState(
        targetValue = if (resolvedExpanded && iconVariant == FinsibleAccordionIconVariant.Chevron) 180f else 0f,
        label = "accordionRotation"
    )

    val stateDescriptionText = if (resolvedExpanded) {
        stringResource(R.string.finsible_accordion_expanded_state)
    } else {
        stringResource(R.string.finsible_accordion_collapsed_state)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.containerColor)
            .border(width = FinsibleTheme.dimes.d1, color = colors.borderColor, shape = shape)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = ripple(color = colors.rippleColor),
                onClick = { onToggle() }
            )
            .semantics(mergeDescendants = true) {
                stateDescription = stateDescriptionText
                if (!enabled) disabled()
            }
            .padding(sizes.padding)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(sizes.spacing)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(sizes.spacing)
        ) {
            if (leadingIcon != null) {
                CompositionLocalProvider(LocalContentColor provides colors.iconTint) {
                    leadingIcon()
                }
            }

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4)) {
                FinsibleText(
                    text = title,
                    textStyleOverride = sizes.titleStyle,
                    color = colors.titleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!subtitle.isNullOrEmpty()) {
                    FinsibleText(
                        text = subtitle,
                        textStyleOverride = sizes.subtitleStyle,
                        color = colors.subtitleColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            CompositionLocalProvider(LocalContentColor provides if (enabled) colors.iconTint else colors.disabledIconTint) {
                when (iconVariant) {
                    FinsibleAccordionIconVariant.Chevron -> Icon(
                        painter = painterResource(com.composables.icons.tabler.filled.R.drawable.tabler_ic_caret_down_filled),
                        contentDescription = null,
                        modifier = Modifier.graphicsLayer { rotationZ = iconRotation },
                        tint = LocalContentColor.current
                    )

                    FinsibleAccordionIconVariant.PlusMinus -> Icon(
                        painter = painterResource(if (resolvedExpanded) com.composables.icons.materialicons.sharp.R.drawable.materialicons_ic_remove_sharp else com.composables.icons.materialicons.sharp.R.drawable.materialicons_ic_add_sharp),
                        contentDescription = null,
                        tint = LocalContentColor.current
                    )
                }
            }
        }

        if (resolvedExpanded) {
            HorizontalDivider(color = FinsibleTheme.colors.divider, thickness = FinsibleTheme.dimes.d1)
            content()
        }
    }
}








