package com.itsjeel01.finsiblefrontend.ui.component.base

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
import com.itsjeel01.finsiblefrontend.common.ButtonStyle
import com.itsjeel01.finsiblefrontend.common.ButtonVariant
import com.itsjeel01.finsiblefrontend.common.IconPosition
import com.itsjeel01.finsiblefrontend.common.InputFieldSize
import com.itsjeel01.finsiblefrontend.ui.theme.ColorKey
import com.itsjeel01.finsiblefrontend.ui.theme.dime.BorderStroke
import com.itsjeel01.finsiblefrontend.ui.theme.dime.Radius
import com.itsjeel01.finsiblefrontend.ui.theme.dime.Size
import com.itsjeel01.finsiblefrontend.ui.theme.dime.appDimensions
import com.itsjeel01.finsiblefrontend.ui.theme.dime.borderWidth
import com.itsjeel01.finsiblefrontend.ui.theme.dime.buttonPadding
import com.itsjeel01.finsiblefrontend.ui.theme.dime.cornerRadius
import com.itsjeel01.finsiblefrontend.ui.theme.dime.spacing
import com.itsjeel01.finsiblefrontend.ui.theme.getCustomColor

/** Base composable for custom buttons with icon, style, and animation options.
 * @param onClick Lambda to execute when button is clicked.
 * @param commonProps Common input properties like label, size, modifier, enabled.
 * @param style Button style (Primary/Secondary).
 * @param variant Button width variant (FullWidth/WrapContent).
 * @param icon Resource ID for the icon to display.
 * @param iconPosition Position of the icon relative to label/button.
 * @param tintedIcon Whether to tint the icon with label color.
 * @param scaleValue Scale factor when pressed.
 * @param pivotFractionX X pivot for scale animation.
 * @param pivotFractionY Y pivot for scale animation.
 * @param durationMillis Animation duration in milliseconds.
 */
@Composable
fun BaseButton(
    onClick: () -> Unit,
    commonProps: CommonProps = CommonProps(),
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
    val dims = appDimensions()

    // --- Interaction and Animation Setup ---

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) scaleValue else 1f,
        animationSpec = tween(durationMillis = durationMillis),
        label = "${commonProps.label ?: "Button"} Scale Animation"
    )

    // --- Button Properties ---

    val buttonShape = RoundedCornerShape(dims.cornerRadius(Radius.XS))
    val isSmall = commonProps.size == InputFieldSize.Small
    val labelStyle = if (isSmall)
        MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
    else
        MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
    val widthModifier = when (variant) {
        ButtonVariant.FullWidth -> Modifier.fillMaxWidth()
        ButtonVariant.WrapContent -> Modifier.wrapContentSize()
    }
    val (labelColor, buttonBackground, buttonBorder) = buttonColors(style, commonProps.enabled)
    val iconModifier = Modifier.size(labelStyle.fontSize.value.dp)

    // --- Button Generation ---

    Box(
        modifier = commonProps.modifier
            .then(widthModifier)
            .addInteraction(commonProps.enabled, onClick, interactionSource)
            .applyScale(scale, pivotFractionX, pivotFractionY)
            .border(
                dims.borderWidth(BorderStroke.MEDIUM),
                color = buttonBorder,
                shape = buttonShape
            )
            .background(color = buttonBackground, shape = buttonShape)
            .buttonPadding(commonProps.size),
        contentAlignment = Alignment.Center
    ) {
        ButtonContent(
            labelColor = labelColor,
            labelStyle = labelStyle,
            label = commonProps.label ?: "",
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
                ButtonWithIconAtEdges(iconPosition, iconComposable, labelComposable, iconModifier)
            } else {
                FullWidthButtonWithIconAtEdges(
                    iconPosition,
                    icon,
                    iconComposable,
                    labelComposable,
                    iconModifier
                )
            }
        }

        IconPosition.StartOfLabel, IconPosition.EndOfLabel -> {
            ButtonWithIconAdjacentToLabel(
                iconPosition,
                iconComposable,
                labelComposable,
                iconModifier
            )
        }
    }
}

/** Composable for button with icon at the ends of the button. */
@Composable
private fun ButtonWithIconAtEdges(
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
            horizontalArrangement = Arrangement.spacedBy(appDimensions().spacing(Size.S8))
        ) {
            if (iconPosition == IconPosition.StartOfButton) iconComposable(modifier)
            labelComposable()
            if (iconPosition == IconPosition.EndOfButton) iconComposable(modifier)
        }
    }
}

/** Composable for button with icon overlaying the label. */
@Composable
private fun FullWidthButtonWithIconAtEdges(
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

/** Composable for button with icon inline with the label. */
@Composable
private fun ButtonWithIconAdjacentToLabel(
    iconPosition: IconPosition,
    iconComposable: @Composable (Modifier) -> Unit,
    labelComposable: @Composable () -> Unit,
    modifier: Modifier,
) {
    Box(contentAlignment = Alignment.Center) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(appDimensions().spacing(Size.S8))
        ) {
            if (iconPosition == IconPosition.StartOfLabel) iconComposable(modifier)
            labelComposable()
            if (iconPosition == IconPosition.EndOfLabel) iconComposable(modifier)
        }
    }
}

@Composable
private fun buttonColors(style: ButtonStyle, enabled: Boolean): Triple<Color, Color, Color> {
    return when (style) {
        ButtonStyle.Primary -> Triple(
            if (enabled) getCustomColor(ColorKey.BtnPrimaryForegroundEnabled) else getCustomColor(
                ColorKey.BtnPrimaryForegroundDisabled
            ),
            if (enabled) getCustomColor(ColorKey.BtnPrimaryBackgroundEnabled) else getCustomColor(
                ColorKey.BtnPrimaryBackgroundDisabled
            ),
            getCustomColor(ColorKey.BtnPrimaryBorder)
        )

        ButtonStyle.Secondary -> Triple(
            if (enabled) getCustomColor(ColorKey.BtnSecondaryForegroundEnabled) else getCustomColor(
                ColorKey.BtnSecondaryForegroundDisabled
            ),
            getCustomColor(ColorKey.BtnSecondaryBackground),
            if (enabled) getCustomColor(ColorKey.BtnSecondaryBorderEnabled)
            else getCustomColor(ColorKey.BtnSecondaryBorderDisabled)
        )
    }
}

private fun Modifier.addInteraction(
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
