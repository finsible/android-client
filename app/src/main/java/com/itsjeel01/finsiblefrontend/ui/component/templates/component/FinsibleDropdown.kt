package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.itsjeel01.finsiblefrontend.ui.component.templates.FlushDropdownPositionProvider
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleDropdownDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDropdownColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDropdownOption
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDropdownSizes

/** Finsible dropdown component.
 *
 * @param options List of options to display in the dropdown.
 * @param selectedId The ID of the currently selected option.
 * @param onSelected Callback to invoke when an option is selected.
 * @param modifier Modifier to apply to the dropdown.
 * @param enabled Whether the dropdown is enabled.
 * @param placeholder Text to display when no option is selected.
 * @param size Size of the dropdown.
 * @param shapeVariant Variant of the dropdown shape.
 * @param colors Colors to use for the dropdown.
 * @param sizes Sizes to use for the dropdown.
 * @param fullWidth Whether the dropdown should take up the full width of its container.
 * @param expanded Whether the dropdown menu is expanded.
 * @param onExpandedChange Callback to invoke when the dropdown menu is expanded or collapsed.
 * @param interactionSource Interaction source to use for the dropdown.
 **/
@Composable
fun FinsibleDropdown(
    options: List<FinsibleDropdownOption>,
    selectedId: String?,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String,
    size: FinsibleSize = FinsibleSize.Medium,
    shapeVariant: FinsibleShape = FinsibleShape.Rounded,
    colors: FinsibleDropdownColors = FinsibleDropdownDefaults.colors(),
    sizes: FinsibleDropdownSizes = FinsibleDropdownDefaults.sizes(size, shapeVariant),
    fullWidth: Boolean = true,
    expanded: Boolean = false,
    onExpandedChange: ((Boolean) -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    require(options.isNotEmpty()) { "options must not be empty." }
    require(placeholder.isNotBlank()) { "placeholder must be non-blank." }

    var internalExpanded by remember { mutableStateOf(false) }
    val isExpanded = onExpandedChange?.let { expanded } ?: internalExpanded
    val closeMenu = { if (onExpandedChange != null) onExpandedChange(false) else internalExpanded = false }
    val toggleMenu = { if (onExpandedChange != null) onExpandedChange(!isExpanded) else internalExpanded = !internalExpanded }

    val expandedStates = remember { MutableTransitionState(false) }
    expandedStates.targetState = isExpanded

    var isUpward by remember { mutableStateOf(false) }
    var anchorWidth by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    val textMeasurer = rememberTextMeasurer()
    val maxTextWidth = remember(options, placeholder, sizes.textStyle, fullWidth) {
        if (fullWidth) 0.dp else {
            val measureStyle = sizes.textStyle.copy(fontWeight = FontWeight.ExtraBold)
            val placeholderWidth = textMeasurer.measure(placeholder, measureStyle).size.width
            val maxOptionWidth = options.maxOfOrNull { textMeasurer.measure(it.label, measureStyle).size.width } ?: 0
            with(density) { maxOf(placeholderWidth, maxOptionWidth).toDp() }
        }
    }

    val borderColor by animateColorAsState(
        targetValue = if (enabled) colors.borderColor else colors.borderColor.copy(alpha = 0.4f),
        label = "dropdownBorder"
    )

    val caretRotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "caretRotation"
    )

    val anchorShape: Shape = RoundedCornerShape(
        topStart = if (isExpanded && isUpward) 0.dp else sizes.cornerRadius,
        topEnd = if (isExpanded && isUpward) 0.dp else sizes.cornerRadius,
        bottomStart = if (isExpanded && !isUpward) 0.dp else sizes.cornerRadius,
        bottomEnd = if (isExpanded && !isUpward) 0.dp else sizes.cornerRadius
    )

    val menuShape = RoundedCornerShape(
        topStart = if (isUpward) sizes.cornerRadius else 0.dp,
        topEnd = if (isUpward) sizes.cornerRadius else 0.dp,
        bottomStart = if (isUpward) 0.dp else sizes.cornerRadius,
        bottomEnd = if (isUpward) 0.dp else sizes.cornerRadius
    )

    val selectedOption = remember(options, selectedId) { options.firstOrNull { it.id == selectedId } }

    val menuModifier = if (anchorWidth > 0) {
        Modifier.width(with(density) { anchorWidth.toDp() })
    } else Modifier

    Box(
        modifier = modifier
            .run { if (fullWidth) fillMaxWidth() else this }
            .onSizeChanged { anchorWidth = it.width },
    ) {
        Row(
            modifier = Modifier
                .run { if (fullWidth) fillMaxWidth() else this }
                .height(sizes.height)
                .clip(anchorShape)
                .background(colors.containerColor)
                .border(width = sizes.borderWidth, color = borderColor, shape = anchorShape)
                .clickable(
                    enabled = enabled,
                    interactionSource = interactionSource,
                    indication = ripple(color = colors.rippleColor),
                    onClick = toggleMenu
                )
                .semantics {
                    role = Role.DropdownList
                    contentDescription = placeholder
                }
                .padding(horizontal = sizes.itemPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(sizes.iconSpacing)
        ) {
            val tint = if (enabled) colors.iconTint else colors.disabledIconTint
            val labelColor = if (selectedOption != null) colors.optionTextColor else colors.placeholderColor
            val labelText = selectedOption?.label ?: placeholder

            if (selectedOption?.icon != null) {
                CompositionLocalProvider(LocalContentColor provides tint) {
                    Box(modifier = Modifier.size(sizes.iconSize), contentAlignment = Alignment.Center) {
                        selectedOption.icon.invoke()
                    }
                }
            }

            val textModifier = if (fullWidth) Modifier.weight(1f) else Modifier.widthIn(min = maxTextWidth)

            Box(modifier = textModifier, contentAlignment = Alignment.CenterStart) {
                FinsibleText(
                    text = labelText,
                    textStyleOverride = sizes.textStyle,
                    color = labelColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                painter = painterResource(com.composables.icons.tabler.filled.R.drawable.tabler_ic_caret_down_filled),
                contentDescription = null,
                tint = tint,
                modifier = Modifier
                    .size(sizes.iconSize)
                    .graphicsLayer { rotationZ = caretRotation }
            )
        }

        if (expandedStates.currentState || expandedStates.targetState) {
            val transformOrigin = remember(isUpward) {
                TransformOrigin(0.5f, if (isUpward) 1f else 0f)
            }

            Popup(
                popupPositionProvider = remember {
                    FlushDropdownPositionProvider(verticalOffset = 0) { openedUpward -> isUpward = openedUpward }
                },
                properties = PopupProperties(focusable = true),
                onDismissRequest = closeMenu
            ) {
                AnimatedVisibility(
                    visibleState = expandedStates,
                    enter = fadeIn(tween(120)) + scaleIn(tween(120), transformOrigin = transformOrigin),
                    exit = fadeOut(tween(100)) + scaleOut(tween(100), transformOrigin = transformOrigin)
                ) {
                    Surface(
                        modifier = menuModifier.heightIn(max = 300.dp),
                        shape = menuShape,
                        color = colors.menuColor,
                        border = BorderStroke(sizes.borderWidth, borderColor),
                        shadowElevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier.verticalScroll(rememberScrollState())
                        ) {
                            options.forEach { option ->
                                val isSelected = option.id == selectedId
                                val itemTextColor = if (isSelected) colors.selectedOptionTextColor else colors.optionTextColor
                                val itemIconTint = if (isSelected) colors.selectedIconTint else colors.iconTint
                                val itemBgColor = if (isSelected) colors.selectedOptionColor else Color.Transparent

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(sizes.height)
                                        .background(itemBgColor)
                                        .selectable(
                                            selected = isSelected,
                                            role = Role.RadioButton,
                                            onClick = {
                                                closeMenu()
                                                onSelected(option.id)
                                            }
                                        )
                                        .padding(horizontal = sizes.itemPadding),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(sizes.iconSpacing)
                                ) {
                                    if (option.icon != null) {
                                        CompositionLocalProvider(LocalContentColor provides itemIconTint) {
                                            Box(
                                                modifier = Modifier.size(sizes.iconSize),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                option.icon.invoke()
                                            }
                                        }
                                    }
                                    val itemStyle = if (isSelected) sizes.textStyle.copy(fontWeight = FontWeight.ExtraBold) else sizes.textStyle
                                    FinsibleText(
                                        text = option.label,
                                        textStyleOverride = itemStyle,
                                        color = itemTextColor,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}