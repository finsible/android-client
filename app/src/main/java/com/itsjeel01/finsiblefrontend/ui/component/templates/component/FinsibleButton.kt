package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleIconPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleBadgeType
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleButtonColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleButtonSizes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Renders a templatised button with support for icons, loading, and optional badges.
 * @param onClick Callback invoked when the button is clicked.
 * @param modifier Modifier applied to the button container.
 * @param text Text label shown when the button is not icon-only.
 * @param enabled Whether the button is enabled for interaction.
 * @param loading Whether to show a loading indicator and suppress clicks.
 * @param iconOnly Whether to render only the icon without text.
 * @param fullWidth Whether the button should expand to the parent width.
 * @param enforceMinTouchTargetSize Whether to enforce Material3 minimum touch target
 *   dimensions. Set to `false` to allow micro-sized buttons (e.g. ExtraSmall iconOnly).
 * @param variant Visual variant used to style the button.
 * @param size Size token used to resolve button dimensions and typography.
 * @param shapeVariant Shape token used to resolve the button shape.
 * @param colors Color tokens used for all enabled/disabled states.
 * @param contentPadding Optional explicit content padding override.
 * @param badgeType Badge style applied to icon-only buttons.
 * @param badgeCount Numeric badge value when [badgeType] is count.
 * @param icon Optional icon content for icon-only and icon+text modes.
 * @param iconPosition Position of [icon] relative to text content.
 */
@Composable
fun FinsibleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    enabled: Boolean = true,
    loading: Boolean = false,
    iconOnly: Boolean = false,
    fullWidth: Boolean = false,
    enforceMinTouchTargetSize: Boolean = true,
    variant: FinsibleButtonVariant = FinsibleButtonVariant.Filled,
    size: FinsibleSize = FinsibleSize.Medium,
    shapeVariant: FinsibleShape = FinsibleShape.Pill,
    colors: FinsibleButtonColors = FinsibleButtonDefaults.colors(variant), contentPadding: PaddingValues? = null,
    badgeType: FinsibleBadgeType = FinsibleBadgeType.None,
    badgeCount: Int = 0,
    icon: (@Composable () -> Unit)? = null,
    iconPosition: FinsibleIconPosition = FinsibleIconPosition.Leading,
    sizes: FinsibleButtonSizes = FinsibleButtonDefaults.sizes(size),
) {
    require(!iconOnly || icon != null) { "iconOnly requires a non-null icon." }
    require(!(iconOnly && fullWidth)) { "iconOnly and fullWidth cannot be used together." }
    require(!(badgeType != FinsibleBadgeType.None && !iconOnly)) { "Badges are only supported on iconOnly buttons." }
    require(!(badgeType == FinsibleBadgeType.Count && size == FinsibleSize.ExtraSmall)) { "Count badge is not supported on ExtraSmall buttons." }
    require(!(badgeType == FinsibleBadgeType.Count && badgeCount < 0)) { "badgeCount cannot be negative." }
    require(!(loading && !enabled)) { "A disabled button should not also be in a loading state." }
    require(!(variant == FinsibleButtonVariant.Text && badgeType != FinsibleBadgeType.None)) { "Text variant does not support badges." }
    require(!(variant == FinsibleButtonVariant.Link && badgeType != FinsibleBadgeType.None)) { "Link variant does not support badges." }
    require(!(icon == null && iconPosition != FinsibleIconPosition.Leading)) { "iconPosition has no effect when no icon is provided." }
    require(iconOnly || text != null) { "A label (text) must be provided if the button is not iconOnly." }
    require(shapeVariant != FinsibleShape.Circle || iconOnly) { "Circle shape is supported only for iconOnly buttons." }

    val badgeMetrics = FinsibleButtonDefaults.badgeSpec(size)
    val buttonShape = FinsibleButtonDefaults.shape(shapeVariant, size)

    val loadingStateDescription = stringResource(R.string.finsible_button_loading_state)
    val badgeDescription = when (badgeType) {
        FinsibleBadgeType.None -> null
        FinsibleBadgeType.Dot -> stringResource(R.string.finsible_button_badge_dot)
        FinsibleBadgeType.Count -> stringResource(R.string.finsible_button_badge_count, badgeCount.coerceIn(0, 99))
    }

    val textStyle = if (variant == FinsibleButtonVariant.Link) {
        sizes.textStyle.copy(textDecoration = TextDecoration.Underline)
    } else {
        sizes.textStyle
    }

    val containerColor = if (enabled) colors.containerColor else colors.disabledContainerColor
    val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor
    val borderColor = if (enabled) colors.borderColor else colors.disabledBorderColor

    val interactionSource = remember { MutableInteractionSource() }
    val borderStroke = borderColor?.let { BorderStroke(FinsibleTheme.stroke.thin, it) }
    val rippleIndication = ripple(color = colors.rippleColor)

    val iconOnlySide = FinsibleButtonDefaults.iconOnlySize(size)
    val containerModifier = when {
        shapeVariant == FinsibleShape.Circle -> modifier.size(iconOnlySide)
        fullWidth -> modifier.fillMaxWidth()
        else -> modifier
    }

    val baseButtonModifier = Modifier.defaultMinSize(minWidth = 0.dp, minHeight = 0.dp)

    val buttonModifier = if (enforceMinTouchTargetSize) {
        when {
            shapeVariant == FinsibleShape.Circle -> baseButtonModifier.size(iconOnlySide)
            iconOnly -> baseButtonModifier.defaultMinSize(minWidth = iconOnlySide, minHeight = iconOnlySide)
            fullWidth -> baseButtonModifier.fillMaxWidth()
            else -> baseButtonModifier
        }
    } else {
        // Override M3's internal min touch target by capping max dimensions.
        // M3 Button internally applies defaultMinSize(minWidth = 40, minHeight = 40).
        // By setting maxWidth and maxHeight to iconOnlySide, the constraint system
        // caps the actual rendered size regardless of M3's minimum.
        val modifier1 = when {
            shapeVariant == FinsibleShape.Circle -> baseButtonModifier.size(iconOnlySide)
            iconOnly -> baseButtonModifier
                .defaultMinSize(minWidth = iconOnlySide, minHeight = iconOnlySide)
                .widthIn(max = iconOnlySide)
                .heightIn(max = iconOnlySide)

            fullWidth -> baseButtonModifier
                .fillMaxWidth()
                .heightIn(max = iconOnlySide)

            else -> baseButtonModifier.heightIn(max = iconOnlySide)
        }
        modifier1
    }

    val padding = contentPadding ?: when {
        iconOnly -> PaddingValues(0.dp)

        else -> {
            val base = sizes.contentPadding
            if (enforceMinTouchTargetSize) base
            else {
                PaddingValues(
                    start = 0.dp,
                    top = base.calculateTopPadding(),
                    end = 0.dp,
                    bottom = base.calculateBottomPadding()
                )
            }
        }
    }

    val showBadge = badgeType != FinsibleBadgeType.None && !loading

    val badgeDiameter = if (badgeType == FinsibleBadgeType.Count) badgeMetrics.diameter + FinsibleTheme.stroke.bold else badgeMetrics.diameter
    val badgeRadius = badgeDiameter / 2
    val cornerRadius = FinsibleButtonDefaults.cornerRadius(shapeVariant, size)

    val cos45Inset = 1f - 0.7071f
    val edgeInset = cornerRadius * cos45Inset

    Box(
        modifier = containerModifier.semantics(mergeDescendants = true) {
            if (showBadge && badgeDescription != null) {
                contentDescription = badgeDescription
            }
        }
    ) {

        CompositionLocalProvider(LocalIndication provides rippleIndication) {
            Button(
                onClick = onClick,
                modifier = buttonModifier.semantics {
                    if (loading) stateDescription = loadingStateDescription
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
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(sizes.iconSize),
                        color = contentColor,
                        strokeWidth = FinsibleTheme.stroke.bold
                    )
                } else if (iconOnly) {
                    Box(modifier = Modifier.size(sizes.iconSize), contentAlignment = Alignment.Center) {
                        icon!!.invoke()
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(sizes.iconSpacing)
                    ) {
                        if (icon != null && iconPosition == FinsibleIconPosition.Leading) {
                            Box(modifier = Modifier.size(sizes.iconSize), contentAlignment = Alignment.Center) { icon() }
                        }

                        FinsibleText(
                            text = text!!,
                            textStyle = textStyle,
                            color = contentColor
                        )

                        if (icon != null && iconPosition == FinsibleIconPosition.Trailing) {
                            Box(modifier = Modifier.size(sizes.iconSize), contentAlignment = Alignment.Center) { icon() }
                        }
                    }
                }
            }
        }

        if (showBadge) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = badgeRadius - edgeInset, y = -badgeRadius + edgeInset)
            ) {
                FinsibleButtonBadge(
                    badgeType = badgeType,
                    badgeCount = badgeCount,
                    badgeMetrics = badgeMetrics,
                    badgeDiameter = badgeDiameter,
                    containerColor = colors.badgeContainerColor,
                    contentColor = colors.badgeContentColor,
                    badgeDescription = null
                )
            }
        }
    }
}