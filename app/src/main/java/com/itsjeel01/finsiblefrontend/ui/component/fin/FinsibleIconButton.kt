package com.itsjeel01.finsiblefrontend.ui.component.fin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

private const val SECONDARY_BORDER_WIDTH_DP = 1
private const val LOADING_INDICATOR_STROKE_WIDTH_DP = 2
private const val BRAND_RIPPLE_ALPHA = 0.24f
private const val SECONDARY_RIPPLE_ALPHA = 0.12f
private const val TERTIARY_RIPPLE_ALPHA = 0.12f
private const val DISABLED_ALPHA = 0.38f

@Composable
fun FinsibleIconButton(
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    config: IconButtonConfig = IconButtonConfig()
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isClickable = config.enabled && !config.loading

    val baseModifier = modifier.clip(config.effectiveShape())

    val styledModifier = when (config.type) {
        ComponentType.Brand, ComponentType.Primary -> baseModifier
            .background(
                color = if (config.type == ComponentType.Brand) FinsibleTheme.colors.brandAccent else config.containerColor(),
                shape = config.effectiveShape()
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = true,
                    color = config.contentColor().copy(alpha = BRAND_RIPPLE_ALPHA)
                ),
                enabled = isClickable,
                onClick = onClick
            )

        ComponentType.Secondary -> baseModifier
            .background(
                color = config.containerColor(),
                shape = config.effectiveShape()
            )
            .border(
                width = SECONDARY_BORDER_WIDTH_DP.dp,
                color = config.borderColor() ?: FinsibleTheme.colors.border,
                shape = config.effectiveShape()
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = true,
                    color = config.contentColor().copy(alpha = SECONDARY_RIPPLE_ALPHA)
                ),
                enabled = isClickable,
                onClick = onClick
            )

        ComponentType.Tertiary -> baseModifier
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = true,
                    color = config.contentColor().copy(alpha = TERTIARY_RIPPLE_ALPHA)
                ),
                enabled = isClickable,
                onClick = onClick
            )
    }

    Box(
        modifier = styledModifier,
        contentAlignment = Alignment.Center
    ) {
        if (config.loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(config.size.iconSize),
                strokeWidth = LOADING_INDICATOR_STROKE_WIDTH_DP.dp,
                color = config.contentColor()
            )
        } else {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(config.size.iconSize + 2 * config.size.iconButtonPadding)
                    .padding(config.size.iconButtonPadding),
                tint = config.customTint ?: if (config.tintIcon) config.contentColor() else Color.Unspecified
            )
        }
    }
}

data class IconButtonConfig(
    val type: ComponentType = ComponentType.Primary,
    val size: ComponentSize = ComponentSize.Medium,
    val shape: IconButtonShape = IconButtonShape.Rounded,
    val enabled: Boolean = true,
    val loading: Boolean = false,
    val tintIcon: Boolean = true,
    val customTint: Color? = null,
    val customShape: Shape? = null
) {
    @Composable
    fun effectiveShape(): Shape = customShape ?: shape.shape(size)

    @Composable
    fun containerColor(): Color = when {
        !enabled -> type.containerColor().copy(alpha = DISABLED_ALPHA)
        else -> type.containerColor()
    }

    @Composable
    fun contentColor(): Color = when {
        !enabled -> type.contentColor().copy(alpha = DISABLED_ALPHA)
        else -> type.contentColor()
    }

    @Composable
    fun borderColor(): Color? = when {
        !enabled -> type.borderColor()?.copy(alpha = DISABLED_ALPHA)
        else -> type.borderColor()
    }
}