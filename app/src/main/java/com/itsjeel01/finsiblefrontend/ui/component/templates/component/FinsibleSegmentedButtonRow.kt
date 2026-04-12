package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
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
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleSegmentedButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleSegmentAlignment
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleSegmentedButtonColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleSegmentedButtonOption
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleSegmentedButtonSizes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleSegmentedButtonVariant

/** Single-select segmented control API.
 *
 * @param options List of options to display.
 * @param selectedValue The currently selected value, or null if none is selected.
 * @param onSelectedValueChange Callback invoked when the selected value changes.
 * @param modifier Modifier to be applied to the layout.
 * @param enabled Controls the enabled state of the segmented control.
 * @param size The size of the segmented control.
 * @param shapeVariant The shape variant of the segmented control.
 * @param variant The variant of the segmented control.
 * @param colors The colors of the segmented control.
 * @param selectedTint The tint color to apply to the selected option.
 * @param inverted Whether to invert the colors of the segmented control.
 * @param sizes The sizes of the segmented control.
 */
@Composable
fun FinsibleSegmentedButtonRow(
    options: List<FinsibleSegmentedButtonOption>,
    selectedValue: String?,
    onSelectedValueChange: (String?) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: FinsibleSize = FinsibleSize.Medium,
    shapeVariant: FinsibleShape = FinsibleShape.Rounded,
    variant: FinsibleSegmentedButtonVariant = FinsibleSegmentedButtonVariant.Filled,
    colors: FinsibleSegmentedButtonColors = FinsibleSegmentedButtonDefaults.colors(variant = variant),
    selectedTint: Color = Color.Unspecified,
    inverted: Boolean = false,
    sizes: FinsibleSegmentedButtonSizes = FinsibleSegmentedButtonDefaults.sizes(size)
) {
    val optionIds = remember(options) { options.mapTo(mutableSetOf()) { it.id } }
    val selectedIds = remember(selectedValue, optionIds) {
        selectedValue?.takeIf { it in optionIds }?.let(::setOf).orEmpty()
    }

    FinsibleSegmentedButtonRowBase(
        options = options,
        selectedIds = selectedIds,
        onSelectionChange = { next -> onSelectedValueChange(next.firstOrNull()) },
        modifier = modifier,
        singleSelection = true,
        enabled = enabled,
        size = size,
        shapeVariant = shapeVariant,
        variant = variant,
        colors = colors,
        selectedTint = selectedTint,
        inverted = inverted,
        sizes = sizes
    )
}

/** Multi-select segmented control API.
 *
 * @param options List of options to display.
 * @param selectedValues The currently selected values.
 * @param onSelectedValuesChange Callback invoked when the selected values change.
 * @param modifier Modifier to be applied to the layout.
 * @param enabled Controls the enabled state of the segmented control.
 * @param size The size of the segmented control.
 * @param shapeVariant The shape variant of the segmented control.
 * @param variant The variant of the segmented control.
 * @param colors The colors of the segmented control.
 * @param selectedTint The tint color to apply to the selected option.
 * @param inverted Whether to invert the colors of the segmented control.
 * @param sizes The sizes of the segmented control.
 */
@Composable
fun FinsibleSegmentedButtonRow(
    options: List<FinsibleSegmentedButtonOption>,
    selectedValues: Set<String>,
    onSelectedValuesChange: (Set<String>) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: FinsibleSize = FinsibleSize.Medium,
    shapeVariant: FinsibleShape = FinsibleShape.Rounded,
    variant: FinsibleSegmentedButtonVariant = FinsibleSegmentedButtonVariant.Filled,
    colors: FinsibleSegmentedButtonColors = FinsibleSegmentedButtonDefaults.colors(variant = variant),
    selectedTint: Color = Color.Unspecified,
    inverted: Boolean = false,
    sizes: FinsibleSegmentedButtonSizes = FinsibleSegmentedButtonDefaults.sizes(size)
) {
    val optionIds = remember(options) { options.mapTo(mutableSetOf()) { it.id } }
    val sanitizedSelection = remember(selectedValues, optionIds) { selectedValues.filterTo(mutableSetOf()) { it in optionIds } }

    FinsibleSegmentedButtonRowBase(
        options = options,
        selectedIds = sanitizedSelection,
        onSelectionChange = { next -> onSelectedValuesChange(next.filterTo(mutableSetOf()) { it in optionIds }) },
        modifier = modifier,
        singleSelection = false,
        enabled = enabled,
        size = size,
        shapeVariant = shapeVariant,
        variant = variant,
        colors = colors,
        selectedTint = selectedTint,
        inverted = inverted,
        sizes = sizes
    )
}

@Composable
private fun FinsibleSegmentedButtonRowBase(
    options: List<FinsibleSegmentedButtonOption>,
    selectedIds: Set<String>,
    onSelectionChange: (Set<String>) -> Unit,
    modifier: Modifier = Modifier,
    singleSelection: Boolean,
    enabled: Boolean = true,
    size: FinsibleSize = FinsibleSize.Medium,
    shapeVariant: FinsibleShape = FinsibleShape.Rounded,
    variant: FinsibleSegmentedButtonVariant = FinsibleSegmentedButtonVariant.Filled,
    colors: FinsibleSegmentedButtonColors = FinsibleSegmentedButtonDefaults.colors(variant = variant),
    selectedTint: Color = Color.Unspecified,
    inverted: Boolean = false,
    sizes: FinsibleSegmentedButtonSizes = FinsibleSegmentedButtonDefaults.sizes(size)
) {
    require(options.isNotEmpty()) { "options must not be empty." }
    require(sizes.iconSize > 0.dp) { "sizes.iconSize must be > 0." }
    require(sizes.horizontalPadding >= 0.dp) { "sizes.horizontalPadding must be >= 0." }
    require(sizes.verticalPadding >= 0.dp) { "sizes.verticalPadding must be >= 0." }

    val segmentShape = FinsibleButtonDefaults.shape(shapeVariant, size)
    val middleShape = FinsibleButtonDefaults.shape(FinsibleShape.Sharp, size)
    val selectedTintContentColor = FinsibleSegmentedButtonDefaults.selectedTintContentColor(inverted)
    val resolvedColors = FinsibleSegmentedButtonDefaults.applySelectedTint(
        colors = colors,
        selectedTint = selectedTint,
        variant = variant,
        selectedContentColor = selectedTintContentColor
    )
    val borderStroke = BorderStroke(width = 1.dp, color = resolvedColors.borderColor)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(segmentShape)
            .border(borderStroke, segmentShape)
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = option.id in selectedIds
            val isLast = index == options.lastIndex

            val resolvedContainer = when {
                !enabled -> resolvedColors.disabledContainerColor
                isSelected -> resolvedColors.selectedContainerColor
                else -> resolvedColors.unselectedContainerColor
            }
            val resolvedContent = when {
                !enabled -> resolvedColors.disabledContentColor
                isSelected -> resolvedColors.selectedContentColor
                else -> resolvedColors.unselectedContentColor
            }

            val stateDescriptionText = if (isSelected) {
                stringResource(com.itsjeel01.finsiblefrontend.R.string.finsible_radio_button_selected_state)
            } else {
                stringResource(com.itsjeel01.finsiblefrontend.R.string.finsible_radio_button_unselected_state)
            }

            key(option.id) {
                val interactionSource = remember { MutableInteractionSource() }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(middleShape)
                        .background(resolvedContainer)
                        .toggleable(
                            value = isSelected,
                            enabled = enabled,
                            role = if (singleSelection) Role.RadioButton else Role.Checkbox,
                            interactionSource = interactionSource,
                            indication = ripple(color = resolvedColors.rippleColor),
                            onValueChange = { checked ->
                                if (!enabled) return@toggleable
                                val nextSelection = if (singleSelection) {
                                    if (checked) setOf(option.id) else emptySet()
                                } else {
                                    if (checked) selectedIds + option.id else selectedIds - option.id
                                }
                                onSelectionChange(nextSelection)
                            }
                        )
                        .semantics {
                            stateDescription = stateDescriptionText
                            if (!enabled) disabled()
                            contentDescription = option.label
                        }
                        .padding(horizontal = sizes.horizontalPadding, vertical = sizes.verticalPadding),
                    contentAlignment = Alignment.Center
                ) {
                    val contentArrangement = when (option.alignment) {
                        FinsibleSegmentAlignment.Start -> Arrangement.spacedBy(sizes.iconSpacing, Alignment.Start)
                        FinsibleSegmentAlignment.Center -> Arrangement.spacedBy(sizes.iconSpacing, Alignment.CenterHorizontally)
                        FinsibleSegmentAlignment.End -> Arrangement.spacedBy(sizes.iconSpacing, Alignment.End)
                    }

                    val labelStyle = if (isSelected) sizes.textStyle.copy(fontWeight = FontWeight.Bold) else sizes.textStyle

                    Row(horizontalArrangement = contentArrangement, verticalAlignment = Alignment.CenterVertically) {
                        if (option.icon != null) {
                            CompositionLocalProvider(LocalContentColor provides resolvedContent) {
                                Box(
                                    modifier = Modifier.size(sizes.iconSize),
                                    contentAlignment = Alignment.Center
                                ) {
                                    option.icon(Modifier.fillMaxSize())
                                }
                            }
                        }
                        FinsibleText(
                            text = option.label,
                            textStyleOverride = labelStyle,
                            color = resolvedContent,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                if (!isLast) {
                    Surface(
                        color = resolvedColors.borderColor, modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                    ) {}
                }
            }
        }
    }
}