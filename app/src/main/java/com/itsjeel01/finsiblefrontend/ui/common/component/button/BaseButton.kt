package com.itsjeel01.finsiblefrontend.ui.common.component.button

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.ui.common.InputCommonProps
import com.itsjeel01.finsiblefrontend.ui.common.InputFieldSize
import com.itsjeel01.finsiblefrontend.ui.theme.CustomColorKey
import com.itsjeel01.finsiblefrontend.ui.theme.getCustomColor

enum class ButtonVariant { FullWidth, WrapContent }
enum class ButtonStyle { Primary, Secondary }
enum class IconPosition { StartOfButton, EndOfButton, StartOfLabel, EndOfLabel }

@Composable
fun BaseButton(
    onClick: () -> Unit,
    commonProps: InputCommonProps = InputCommonProps(),
    style: ButtonStyle = ButtonStyle.Primary,
    variant: ButtonVariant = ButtonVariant.FullWidth,
    icon: Int? = null,
    iconPosition: IconPosition = IconPosition.EndOfLabel,
    tintedIcon: Boolean = true,
    scaleValue: Float = 0.98f,
    pivotFractionX: Float = 0.5f,
    pivotFractionY: Float = 0.5f,
    durationMillis: Int = 100,
) {
    // Animation and interaction state
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) scaleValue else 1f,
        animationSpec = tween(durationMillis = durationMillis),
        label = "scaleAnimation"
    )

    // Style configurations
    val buttonShape = RoundedCornerShape(2.dp)
    val isSmall = commonProps.size == InputFieldSize.Small
    val horizontalPadding = if (isSmall) 12.dp else 16.dp
    val verticalPadding = if (isSmall) 12.dp else 16.dp
    val labelStyle = if (isSmall)
        MaterialTheme.typography.labelMedium
    else
        MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)

    // Width modifier based on variant
    val widthModifier = when (variant) {
        ButtonVariant.FullWidth -> Modifier.fillMaxWidth()
        ButtonVariant.WrapContent -> Modifier.wrapContentSize()
    }

    // Colors based on style and enabled state
    val (labelColor, buttonBackground, buttonBorder) = getButtonColors(style, commonProps.enabled)

    // Icon configuration
    val iconModifier = Modifier.size(labelStyle.fontSize.value.dp)

    Box(
        modifier = commonProps.modifier
            .then(widthModifier)
            .applyClickable(commonProps.enabled, onClick, interactionSource)
            .applyScale(scale, pivotFractionX, pivotFractionY)
            .border(width = 1.dp, color = buttonBorder, shape = buttonShape)
            .background(color = buttonBackground, shape = buttonShape)
            .padding(vertical = verticalPadding, horizontal = horizontalPadding),
        contentAlignment = Alignment.Center
    ) {
        ButtonContent(
            labelColor = labelColor,
            labelStyle = labelStyle,
            label = commonProps.label!!,
            icon = icon,
            iconPosition = iconPosition,
            variant = variant,
            tintedIcon = tintedIcon,
            iconModifier = iconModifier
        )
    }
}

@Composable
private fun ButtonContent(
    labelColor: Color,
    labelStyle: TextStyle,
    label: String,
    icon: Int?,
    iconPosition: IconPosition,
    variant: ButtonVariant,
    tintedIcon: Boolean,
    iconModifier: Modifier,
) {
    val iconComposable: @Composable (modifier: Modifier) -> Unit = { modifier ->
        icon?.let {
            Icon(
                painter = painterResource(id = it),
                tint = if (tintedIcon) labelColor else Color.Unspecified,
                contentDescription = null,
                modifier = modifier
            )
        }
    }

    val labelComposable: @Composable () -> Unit = {
        Text(text = label, color = labelColor, style = labelStyle)
    }

    when (iconPosition) {
        IconPosition.StartOfButton, IconPosition.EndOfButton -> {
            if (variant == ButtonVariant.WrapContent) {
                ButtonWithIconAtEnds(iconPosition, iconComposable, labelComposable, iconModifier)
            } else {
                ButtonWithOverlayIcon(iconPosition, icon, iconComposable, labelComposable, iconModifier)
            }
        }

        IconPosition.StartOfLabel, IconPosition.EndOfLabel -> {
            ButtonWithInlineIcon(iconPosition, iconComposable, labelComposable, iconModifier)
        }
    }
}

@Composable
private fun ButtonWithIconAtEnds(
    iconPosition: IconPosition,
    iconComposable: @Composable (Modifier) -> Unit,
    labelComposable: @Composable () -> Unit,
    modifier: Modifier,
) {
    Box(
        modifier = Modifier,
        contentAlignment = if (iconPosition == IconPosition.StartOfButton)
            Alignment.CenterStart else Alignment.CenterEnd
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (iconPosition == IconPosition.StartOfButton) iconComposable(modifier)
            labelComposable()
            if (iconPosition == IconPosition.EndOfButton) iconComposable(modifier)
        }
    }
}

@Composable
private fun ButtonWithOverlayIcon(
    iconPosition: IconPosition,
    icon: Int?,
    iconComposable: @Composable (Modifier) -> Unit,
    labelComposable: @Composable () -> Unit,
    modifier: Modifier,
) {
    Box(contentAlignment = Alignment.Center) {
        labelComposable()
        if (icon != null) {
            Box(
                contentAlignment = if (iconPosition == IconPosition.StartOfButton)
                    Alignment.CenterStart else Alignment.CenterEnd,
                modifier = Modifier.fillMaxWidth()
            ) {
                iconComposable(modifier)
            }
        }
    }
}

@Composable
private fun ButtonWithInlineIcon(
    iconPosition: IconPosition,
    iconComposable: @Composable (Modifier) -> Unit,
    labelComposable: @Composable () -> Unit,
    modifier: Modifier,
) {
    Box(contentAlignment = Alignment.Center) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (iconPosition == IconPosition.StartOfLabel) iconComposable(modifier)
            labelComposable()
            if (iconPosition == IconPosition.EndOfLabel) iconComposable(modifier)
        }
    }
}

@Composable
private fun getButtonColors(style: ButtonStyle, enabled: Boolean): Triple<Color, Color, Color> {
    return when (style) {
        ButtonStyle.Primary -> Triple(
            if (enabled) getCustomColor(CustomColorKey.BtnPrimaryForegroundEnabled)
            else getCustomColor(CustomColorKey.BtnPrimaryForegroundDisabled),
            if (enabled) getCustomColor(CustomColorKey.BtnPrimaryBackgroundEnabled)
            else getCustomColor(CustomColorKey.BtnPrimaryBackgroundDisabled),
            getCustomColor(CustomColorKey.BtnPrimaryBorder)
        )

        ButtonStyle.Secondary -> Triple(
            if (enabled) getCustomColor(CustomColorKey.BtnSecondaryForegroundEnabled)
            else getCustomColor(CustomColorKey.BtnSecondaryForegroundDisabled),
            getCustomColor(CustomColorKey.BtnSecondaryBackground),
            if (enabled) getCustomColor(CustomColorKey.BtnSecondaryBorderEnabled)
            else getCustomColor(CustomColorKey.BtnSecondaryBorderDisabled)
        )
    }
}

private fun Modifier.applyClickable(
    enabled: Boolean,
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource,
): Modifier {
    return if (enabled) {
        this.clickable(
            onClick = onClick,
            interactionSource = interactionSource,
            indication = null,
            role = Role.Button
        )
    } else {
        this
    }
}

private fun Modifier.applyScale(
    scale: Float,
    pivotFractionX: Float,
    pivotFractionY: Float,
): Modifier {
    return this.graphicsLayer {
        scaleX = scale
        scaleY = scale
        transformOrigin = TransformOrigin(pivotFractionX, pivotFractionY)
    }
}