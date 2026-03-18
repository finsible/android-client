package com.itsjeel01.finsiblefrontend.ui.component.templates.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextDecoration
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleIconPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/**
 * A highly configurable "Semantic" Button component that adheres to Finsible Design System.
 *
 * @param onClick Callback when the button is clicked.
 * @param modifier Composable modifier.
 * @param enabled Whether the button is enabled.
 * @param loading Whether the button is in a loading state. Displays a progress indicator instead of content.
 * @param iconOnly Renders button as icon-only, enforcing square bounds.
 * @param fullWidth If true, the button expands to fill the maximum width.
 * @param variant The visual style of the button (Filled, Outlined, Text, etc.).
 * @param size The semantic size of the button (XS, S, M, L, XL).
 * @param shapeVariant The semantic shape of the button (Pill, Rounded, Sharp, etc.).
 * @param colors The resolved color styles for the button. Defaults based on [variant].
 * @param contentPadding Custom padding for the button content. If null, defaults to [size] specific padding.
 * @param badgeType In-house badge style.
 * @param badgeCount Count value used when [badgeType] is [com.itsjeel01.finsiblefrontend.ui.component.templates.defaults.FinsibleBadgeType.Count].
 * @param icon Optional composable icon displayed with the label.
 * @param iconPosition Position for [icon] relative to the label.
 * @param content The main content of the button (usually text).
 */
@Composable
fun FinsibleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    iconOnly: Boolean = false,
    fullWidth: Boolean = false,
    variant: FinsibleButtonVariant = FinsibleButtonVariant.Filled,
    size: FinsibleSize = FinsibleSize.Medium,
    shapeVariant: FinsibleShape = FinsibleShape.Pill,
    colors: FinsibleButtonColors = FinsibleButtonDefaults.colors(variant),
    contentPadding: PaddingValues? = null,
    badgeType: FinsibleBadgeType = FinsibleBadgeType.None,
    badgeCount: Int = 0,
    icon: (@Composable () -> Unit)? = null,
    iconPosition: FinsibleIconPosition = FinsibleIconPosition.Leading,
    content: @Composable RowScope.() -> Unit
) {
    require(!iconOnly || icon != null) {
        "iconOnly requires a non-null icon."
    }
    require(!(iconOnly && fullWidth)) {
        "iconOnly and fullWidth cannot be used together."
    }
    require(!(badgeType == FinsibleBadgeType.Count && size == FinsibleSize.ExtraSmall)) {
        "Count badge is not supported on ExtraSmall buttons."
    }
    require(!(badgeType != FinsibleBadgeType.None && !iconOnly)) {
        "Badges are only supported on iconOnly buttons."
    }
    require(!(badgeType == FinsibleBadgeType.Count && badgeCount < 0)) {
        "badgeCount cannot be negative."
    }
    require(!(loading && !enabled)) {
        "A disabled button should not also be in a loading state."
    }
    require(!(variant == FinsibleButtonVariant.Text && badgeType != FinsibleBadgeType.None)) {
        "Text variant does not support badges."
    }
    require(!(variant == FinsibleButtonVariant.Link && badgeType != FinsibleBadgeType.None)) {
        "Link variant does not support badges."
    }
    require(!(icon == null && iconPosition != FinsibleIconPosition.Leading)) {
        "iconPosition has no effect when no icon is provided."
    }

    val buttonSizes = FinsibleButtonDefaults.sizes(size)
    val badgeMetrics = FinsibleButtonDefaults.badgeSpec(size)
    val buttonShape = FinsibleButtonDefaults.shape(shapeVariant, size)
    val loadingStateDescription = stringResource(R.string.finsible_button_loading_state)
    val badgeDescription = when (badgeType) {
        FinsibleBadgeType.None -> null
        FinsibleBadgeType.Dot -> stringResource(R.string.finsible_button_badge_dot)
        FinsibleBadgeType.Count -> stringResource(
            R.string.finsible_button_badge_count,
            badgeCount.coerceIn(0, 99)
        )
    }

    val textStyle = if (variant == FinsibleButtonVariant.Link) {
        buttonSizes.textStyle.copy(textDecoration = TextDecoration.Underline)
    } else {
        buttonSizes.textStyle
    }

    val containerColor = if (enabled) colors.containerColor else colors.disabledContainerColor
    val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor
    val borderColor = if (enabled) colors.borderColor else colors.disabledBorderColor

    val interactionSource = remember { MutableInteractionSource() }

    val borderStroke = if (borderColor != null) {
        BorderStroke(FinsibleTheme.dimes.d1, borderColor)
    } else null
    val rippleIndication = ripple(color = colors.rippleColor)

    val containerModifier = when {
        iconOnly -> modifier.size(buttonSizes.height)
        fullWidth -> modifier
            .fillMaxWidth()
            .height(buttonSizes.height)

        else -> modifier.height(buttonSizes.height)
    }
    val buttonModifier = if (fullWidth || iconOnly) Modifier.fillMaxSize() else Modifier.fillMaxHeight()
    val padding = if (iconOnly) PaddingValues(FinsibleTheme.dimes.d0) else contentPadding ?: buttonSizes.contentPadding
    val showBadge = badgeType != FinsibleBadgeType.None && !loading

    val badgeDiameter = when (badgeType) {
        FinsibleBadgeType.Count -> badgeMetrics.diameter + FinsibleTheme.dimes.d2
        else -> badgeMetrics.diameter
    }
    val badgeRadius = badgeDiameter / 2

    val cornerRadius = FinsibleButtonDefaults.cornerRadius(shapeVariant, size)

    val cos45Inset = 1f - 0.7071f // 1 - cos(45°)
    val edgeInset = cornerRadius * cos45Inset
    val offsetX = badgeRadius - edgeInset
    val offsetY = -badgeRadius + edgeInset

    Box(modifier = containerModifier) {
        CompositionLocalProvider(LocalIndication provides rippleIndication) {
            Button(
                onClick = onClick,
                modifier = buttonModifier.semantics {
                    role = Role.Button
                    if (loading) {
                        stateDescription = loadingStateDescription
                    }
                },
                enabled = enabled && !loading,
                shape = buttonShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = containerColor,
                    contentColor = contentColor,
                    disabledContainerColor = colors.disabledContainerColor,
                    disabledContentColor = colors.disabledContentColor
                ),
                elevation = null,
                border = borderStroke,
                contentPadding = padding,
                interactionSource = interactionSource
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(buttonSizes.iconSize)
                                .semantics { contentDescription = loadingStateDescription },
                            color = contentColor,
                            strokeWidth = FinsibleTheme.dimes.d2
                        )
                    } else if (iconOnly) {
                        Box(
                            modifier = Modifier.size(buttonSizes.iconSize),
                            contentAlignment = Alignment.Center
                        ) {
                            icon?.invoke()
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CompositionLocalProvider(LocalContentColor provides contentColor) {
                                ProvideTextStyle(value = textStyle) {

                                    // Leading Icon
                                    if (icon != null && iconPosition == FinsibleIconPosition.Leading) {
                                        Box(
                                            modifier = Modifier.size(buttonSizes.iconSize),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            icon()
                                        }
                                        Spacer(modifier = Modifier.width(buttonSizes.iconSpacing))
                                    }

                                    // Label
                                    content()

                                    // Trailing Icon
                                    if (icon != null && iconPosition == FinsibleIconPosition.Trailing) {
                                        Spacer(modifier = Modifier.width(buttonSizes.iconSpacing))
                                        Box(
                                            modifier = Modifier.size(buttonSizes.iconSize),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            icon()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showBadge) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = offsetX, y = offsetY)
            ) {
                FinsibleButtonBadge(
                    badgeType = badgeType,
                    badgeCount = badgeCount,
                    badgeMetrics = badgeMetrics,
                    badgeDiameter = badgeDiameter,
                    containerColor = colors.badgeContainerColor,
                    contentColor = colors.badgeContentColor,
                    badgeDescription = badgeDescription
                )
            }
        }
    }
}