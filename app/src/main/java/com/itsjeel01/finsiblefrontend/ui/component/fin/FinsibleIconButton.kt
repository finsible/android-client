package com.itsjeel01.finsiblefrontend.ui.component.fin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

private const val SECONDARY_BORDER_WIDTH_DP = 1
private const val LOADING_INDICATOR_STROKE_WIDTH_DP = 2
private const val DISABLED_ALPHA = 0.38f

/** Color configuration for [FinsibleIconButton]. */
@Immutable
data class FinsibleIconButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val borderColor: Color?,
    val rippleAlpha: Float
)

/** Size configuration for [FinsibleIconButton]. */
@Immutable
data class FinsibleIconButtonSizes(
    val iconSize: Dp,
    val padding: Dp
)

/** Defaults factory for [FinsibleIconButton] colors and sizes. */
object FinsibleIconButtonDefaults {

    /** Brand-accent icon button colors. */
    @Composable
    fun brandColors(
        containerColor: Color = FinsibleTheme.colors.brandAccent,
        contentColor: Color = FinsibleTheme.colors.primaryBackground
    ) = FinsibleIconButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        borderColor = null,
        rippleAlpha = 0.24f
    )

    /** Primary filled icon button colors. */
    @Composable
    fun primaryColors(
        containerColor: Color = FinsibleTheme.colors.primaryContent,
        contentColor: Color = FinsibleTheme.colors.primaryBackground
    ) = FinsibleIconButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        borderColor = null,
        rippleAlpha = 0.24f
    )

    /** Secondary outlined icon button colors. */
    @Composable
    fun secondaryColors(
        contentColor: Color = FinsibleTheme.colors.primaryContent,
        borderColor: Color = FinsibleTheme.colors.border
    ) = FinsibleIconButtonColors(
        containerColor = Color.Transparent,
        contentColor = contentColor,
        borderColor = borderColor,
        rippleAlpha = 0.12f
    )

    /** Tertiary text-only icon button colors. */
    @Composable
    fun tertiaryColors(
        contentColor: Color = FinsibleTheme.colors.secondaryContent
    ) = FinsibleIconButtonColors(
        containerColor = Color.Transparent,
        contentColor = contentColor,
        borderColor = null,
        rippleAlpha = 0.12f
    )

    /** Small icon button sizes. */
    @Composable
    fun smallSizes() = FinsibleIconButtonSizes(
        iconSize = FinsibleTheme.dimes.d16,
        padding = FinsibleTheme.dimes.d4
    )

    /** Medium icon button sizes. */
    @Composable
    fun mediumSizes() = FinsibleIconButtonSizes(
        iconSize = FinsibleTheme.dimes.d20,
        padding = FinsibleTheme.dimes.d8
    )

    /** Large icon button sizes. */
    @Composable
    fun largeSizes() = FinsibleIconButtonSizes(
        iconSize = FinsibleTheme.dimes.d24,
        padding = FinsibleTheme.dimes.d8
    )
}

@Composable
fun FinsibleIconButton(
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    enabled: Boolean = true,
    loading: Boolean = false,
    colors: FinsibleIconButtonColors = FinsibleIconButtonDefaults.primaryColors(),
    sizes: FinsibleIconButtonSizes = FinsibleIconButtonDefaults.mediumSizes(),
    shape: Shape = CircleShape,
    tintIcon: Boolean = true,
    customTint: Color? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isClickable = enabled && !loading

    val effectiveContainerColor = if (enabled) colors.containerColor
    else colors.containerColor.copy(alpha = DISABLED_ALPHA)
    val effectiveContentColor = if (enabled) colors.contentColor
    else colors.contentColor.copy(alpha = DISABLED_ALPHA)
    val effectiveBorderColor = if (enabled) colors.borderColor
    else colors.borderColor?.copy(alpha = DISABLED_ALPHA)

    val isFilled = colors.containerColor != Color.Transparent
    val hasBorder = colors.borderColor != null

    val baseModifier = modifier.clip(shape)

    val styledModifier = when {
        isFilled -> baseModifier
            .background(color = effectiveContainerColor, shape = shape)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = true,
                    color = effectiveContentColor.copy(alpha = colors.rippleAlpha)
                ),
                enabled = isClickable,
                onClick = onClick
            )

        hasBorder -> baseModifier
            .background(color = effectiveContainerColor, shape = shape)
            .border(
                width = SECONDARY_BORDER_WIDTH_DP.dp,
                color = effectiveBorderColor ?: FinsibleTheme.colors.border,
                shape = shape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = true,
                    color = effectiveContentColor.copy(alpha = colors.rippleAlpha)
                ),
                enabled = isClickable,
                onClick = onClick
            )

        else -> baseModifier
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = true,
                    color = effectiveContentColor.copy(alpha = colors.rippleAlpha)
                ),
                enabled = isClickable,
                onClick = onClick
            )
    }

    Box(
        modifier = styledModifier,
        contentAlignment = Alignment.Center
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(sizes.iconSize),
                strokeWidth = LOADING_INDICATOR_STROKE_WIDTH_DP.dp,
                color = effectiveContentColor
            )
        } else {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(sizes.iconSize + sizes.padding * 2)
                    .padding(sizes.padding),
                tint = customTint ?: if (tintIcon) effectiveContentColor else Color.Unspecified
            )
        }
    }
}