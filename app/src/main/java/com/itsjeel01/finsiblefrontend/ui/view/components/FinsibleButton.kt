package com.itsjeel01.finsiblefrontend.ui.view.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.theme.CustomColorKey
import com.itsjeel01.finsiblefrontend.ui.theme.getCustomColor

enum class ButtonVariant {
    FullWidth,
    FullWidthWithIcon,
    WrapContent,
    WrapContentWithIcon
}

enum class ButtonSize {
    Small,
    Large
}

enum class IconPosition {
    StartOfButton,
    EndOfButton,
    StartOfLabel,
    EndOfLabel
}

enum class ButtonStyle {
    Primary,
    Secondary
}

@Composable
fun FinsibleButton(
    modifier: Modifier = Modifier,
    label: String,
    iconDrawable: Int? = null,
    onClick: () -> Unit,
    style: ButtonStyle = ButtonStyle.Primary,
    pivotFractionX: Float = 0.5f,
    pivotFractionY: Float = 0.5f,
    scaleValue: Float = 0.98f,
    durationMillis: Int = 100,
    variant: ButtonVariant = ButtonVariant.FullWidth,
    size: ButtonSize = ButtonSize.Large,
    iconPosition: IconPosition = IconPosition.EndOfButton,
    isEnabled: Boolean = true,
    tintedIcon: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) scaleValue else 1f,
        animationSpec = tween(durationMillis = durationMillis),
        label = "scaleAnimation"
    )

    val buttonShape = RoundedCornerShape(2.dp)
    val horizontalPadding = if (size == ButtonSize.Small) 12.dp else 16.dp
    val verticalPadding = if (size == ButtonSize.Small) 8.dp else 12.dp

    val labelStyle = getLabelStyle(size)

    val widthModifier = when (variant) {
        ButtonVariant.FullWidth, ButtonVariant.FullWidthWithIcon -> Modifier.fillMaxWidth()
        ButtonVariant.WrapContent, ButtonVariant.WrapContentWithIcon -> Modifier.wrapContentSize()
    }

    val (labelColor, buttonBackground, buttonBorder) = when (style) {
        ButtonStyle.Primary -> Triple(
            if (isEnabled) getCustomColor(CustomColorKey.BtnPrimaryForegroundEnabled) else getCustomColor(CustomColorKey.BtnPrimaryForegroundDisabled),
            if (isEnabled) getCustomColor(CustomColorKey.BtnPrimaryBackgroundEnabled) else getCustomColor(CustomColorKey.BtnPrimaryBackgroundDisabled),
            getCustomColor(CustomColorKey.BtnPrimaryBorder)
        )

        ButtonStyle.Secondary -> Triple(
            if (isEnabled) getCustomColor(CustomColorKey.BtnSecondaryForegroundEnabled) else getCustomColor(CustomColorKey.BtnSecondaryForegroundDisabled),
            getCustomColor(CustomColorKey.BtnSecondaryBackground),
            if (isEnabled) getCustomColor(CustomColorKey.BtnSecondaryBorderEnabled) else getCustomColor(CustomColorKey.BtnSecondaryBorderDisabled)
        )
    }

    Box(
        modifier = modifier.then(widthModifier)
            .then(
                if (isEnabled) Modifier.clickable(
                    onClick = onClick,
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.Button
                ) else Modifier
            )
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin(pivotFractionX, pivotFractionY)
            }
            .border(width = 1.dp, color = buttonBorder, shape = buttonShape)
            .background(color = buttonBackground, shape = buttonShape)
            .padding(vertical = verticalPadding, horizontal = horizontalPadding),
        contentAlignment = Alignment.Center
    ) {
        val iconModifier = Modifier.size(labelStyle.fontSize.value.dp)

        val iconComposable: @Composable (modifier: Modifier) -> Unit = { modifier ->
            iconDrawable?.let {
                Icon(
                    painter = painterResource(id = it),
                    tint = if (tintedIcon) labelColor else Color.Transparent,
                    contentDescription = "Button Icon",
                    modifier = modifier
                )
            }
        }

        val labelComposable: @Composable () -> Unit = {
            Text(text = label, color = labelColor, style = labelStyle)
        }

        // FullWidthWithIcon | WrapContentWithIcon
        if (variant == ButtonVariant.FullWidthWithIcon || variant == ButtonVariant.WrapContentWithIcon) {
            when (iconPosition) {
                // StartOfButton | EndOfButton
                IconPosition.StartOfButton, IconPosition.EndOfButton -> {

                    // WrapContentWithIcon -> StartOfButton | EndOfButton
                    if (variant == ButtonVariant.WrapContentWithIcon) {
                        Row(
                            modifier = Modifier.align(if (iconPosition == IconPosition.StartOfButton) Alignment.CenterStart else Alignment.CenterEnd),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (iconPosition == IconPosition.StartOfButton) iconComposable(iconModifier) // StartOfButton
                            labelComposable()
                            if (iconPosition == IconPosition.EndOfButton) iconComposable(iconModifier) // EndOfButton
                        }
                    }

                    // FullWidthWithIcon -> StartOfButton | EndOfButton
                    else {
                        labelComposable()
                        iconComposable(
                            if (iconPosition == IconPosition.StartOfButton) iconModifier.align(Alignment.CenterStart) // StartOfButton
                            else iconModifier.align(Alignment.CenterEnd) // EndOfButton
                        )
                    }
                }

                // StartOfLabel | EndOfLabel
                IconPosition.StartOfLabel, IconPosition.EndOfLabel -> {
                    // WrapContentWithIcon | FullWidthWithIcon -> StartOfLabel | EndOfLabel
                    Row(
                        modifier = Modifier.align(Alignment.Center),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (iconPosition == IconPosition.StartOfLabel) iconComposable(iconModifier) // StartOfLabel
                        labelComposable()
                        if (iconPosition == IconPosition.EndOfLabel) iconComposable(iconModifier) // EndOfLabel
                    }
                }
            }
        }
        // FullWidth | WrapContent
        else {
            labelComposable()
        }
    }
}

@Composable
fun getLabelStyle(size: ButtonSize): TextStyle {
    return (if (size == ButtonSize.Small) MaterialTheme.typography.titleMedium
    else MaterialTheme.typography.titleLarge)
        .copy(fontWeight = FontWeight.Bold)
}

@Preview(showBackground = true)
@Composable
fun FinsibleButtonPreviews() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val label = "Button Label"

        // FullWidth Primary Button
        FinsibleButton(
            label = label,
            onClick = {},
            style = ButtonStyle.Primary,
            variant = ButtonVariant.FullWidth
        )

        // FullWidthWithIcon Primary Button
        FinsibleButton(
            label = label,
            iconDrawable = R.drawable.arrow_right_icon,
            onClick = {},
            style = ButtonStyle.Primary,
            variant = ButtonVariant.FullWidthWithIcon,
            iconPosition = IconPosition.EndOfLabel
        )

        // WrapContent Primary Button
        FinsibleButton(
            label = label,
            onClick = {},
            style = ButtonStyle.Primary,
            variant = ButtonVariant.WrapContent
        )

        // WrapContentWithIcon Primary Button
        FinsibleButton(
            label = label,
            iconDrawable = R.drawable.arrow_right_icon,
            onClick = {},
            style = ButtonStyle.Primary,
            variant = ButtonVariant.WrapContentWithIcon,
            iconPosition = IconPosition.StartOfLabel
        )

        // FullWidth Secondary Button
        FinsibleButton(
            label = label,
            onClick = {},
            style = ButtonStyle.Secondary,
            variant = ButtonVariant.FullWidth
        )

        // FullWidthWithIcon Secondary Button
        FinsibleButton(
            label = label,
            iconDrawable = R.drawable.arrow_right_icon,
            onClick = {},
            style = ButtonStyle.Secondary,
            variant = ButtonVariant.FullWidthWithIcon,
            iconPosition = IconPosition.EndOfLabel
        )

        // WrapContent Secondary Button
        FinsibleButton(
            label = label,
            onClick = {},
            style = ButtonStyle.Secondary,
            variant = ButtonVariant.WrapContent
        )

        // WrapContentWithIcon Secondary Button
        FinsibleButton(
            label = label,
            iconDrawable = R.drawable.arrow_right_icon,
            onClick = {},
            style = ButtonStyle.Secondary,
            variant = ButtonVariant.WrapContentWithIcon,
            iconPosition = IconPosition.StartOfLabel
        )
    }
}