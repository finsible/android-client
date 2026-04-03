package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
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
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleIconPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleBadgeType
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleButtonColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun FinsibleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
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

    val buttonSizes = FinsibleButtonDefaults.sizes(size)
    val badgeMetrics = FinsibleButtonDefaults.badgeSpec(size)
    val buttonShape = FinsibleButtonDefaults.shape(shapeVariant, size)

    val loadingStateDescription = stringResource(R.string.finsible_button_loading_state)
    val badgeDescription = when (badgeType) {
        FinsibleBadgeType.None -> null
        FinsibleBadgeType.Dot -> stringResource(R.string.finsible_button_badge_dot)
        FinsibleBadgeType.Count -> stringResource(R.string.finsible_button_badge_count, badgeCount.coerceIn(0, 99))
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
    val borderStroke = borderColor?.let { BorderStroke(FinsibleTheme.dimes.d1, it) }
    val rippleIndication = ripple(color = colors.rippleColor)

    val containerModifier = when {
        iconOnly -> modifier.size(buttonSizes.height)
        fullWidth -> modifier
            .fillMaxWidth()
            .height(buttonSizes.height)
        else -> modifier.height(buttonSizes.height)
    }

    val buttonModifier = if (fullWidth || iconOnly) Modifier.fillMaxSize() else Modifier.fillMaxHeight()

    val padding = contentPadding ?: when {
        iconOnly -> PaddingValues(FinsibleTheme.dimes.d0)
        variant == FinsibleButtonVariant.Text || variant == FinsibleButtonVariant.Link ->
            PaddingValues(horizontal = FinsibleTheme.dimes.d8)

        else -> buttonSizes.contentPadding
    }

    val showBadge = badgeType != FinsibleBadgeType.None && !loading

    val badgeDiameter = if (badgeType == FinsibleBadgeType.Count) badgeMetrics.diameter + FinsibleTheme.dimes.d2 else badgeMetrics.diameter
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
                        modifier = Modifier.size(buttonSizes.iconSize),
                        color = contentColor,
                        strokeWidth = FinsibleTheme.dimes.d2
                    )
                } else if (iconOnly) {
                    Box(modifier = Modifier.size(buttonSizes.iconSize), contentAlignment = Alignment.Center) {
                        icon!!.invoke()
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(buttonSizes.iconSpacing)
                    ) {
                        if (icon != null && iconPosition == FinsibleIconPosition.Leading) {
                            Box(modifier = Modifier.size(buttonSizes.iconSize), contentAlignment = Alignment.Center) { icon() }
                        }

                        FinsibleText(
                            text = text!!,
                            textStyleOverride = textStyle,
                            color = contentColor
                        )

                        if (icon != null && iconPosition == FinsibleIconPosition.Trailing) {
                            Box(modifier = Modifier.size(buttonSizes.iconSize), contentAlignment = Alignment.Center) { icon() }
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