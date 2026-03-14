package com.itsjeel01.finsiblefrontend.ui.component.fin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Color configuration for [FinsibleButton]. */
@Immutable
data class FinsibleButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val borderColor: Color?
)

/** Size configuration for [FinsibleButton]. */
@Immutable
data class FinsibleButtonSizes(
    val height: Dp,
    val cornerRadius: Dp,
    val iconSize: Dp,
    val horizontalPadding: Dp,
    val textStyle: TextStyle
)

/** Icon position relative to the button label. */
enum class FinsibleButtonIconPosition {
    /** Aligned to the start edge of the button. */
    Leading,

    /** Aligned to the end edge of the button. */
    Trailing,

    /** Adjacent to the label with spacing, placed before the text. */
    BeforeLabel,

    /** Adjacent to the label with spacing, placed after the text. */
    AfterLabel
}

/** Defaults factory for [FinsibleButton] colors and sizes. */
object FinsibleButtonDefaults {

    /** Brand-accent filled button colors. */
    @Composable
    fun brandColors(
        containerColor: Color = FinsibleTheme.colors.brandAccent,
        contentColor: Color = FinsibleTheme.colors.primaryBackground
    ) = FinsibleButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        borderColor = null
    )

    /** Primary filled button colors. */
    @Composable
    fun primaryColors(
        containerColor: Color = FinsibleTheme.colors.primaryContent,
        contentColor: Color = FinsibleTheme.colors.primaryBackground
    ) = FinsibleButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        borderColor = null
    )

    /** Secondary outlined button colors. */
    @Composable
    fun secondaryColors(
        contentColor: Color = FinsibleTheme.colors.primaryContent,
        borderColor: Color = FinsibleTheme.colors.border
    ) = FinsibleButtonColors(
        containerColor = Color.Transparent,
        contentColor = contentColor,
        borderColor = borderColor
    )

    /** Text-only button colors. */
    @Composable
    fun textColors(
        contentColor: Color = FinsibleTheme.colors.secondaryContent
    ) = FinsibleButtonColors(
        containerColor = Color.Transparent,
        contentColor = contentColor,
        borderColor = null
    )

    /** Small button sizes. */
    @Composable
    fun smallSizes() = FinsibleButtonSizes(
        height = FinsibleTheme.dimes.d32,
        cornerRadius = FinsibleTheme.dimes.d8,
        iconSize = FinsibleTheme.dimes.d16,
        horizontalPadding = FinsibleTheme.dimes.d12,
        textStyle = FinsibleTheme.typography.t14
    )

    /** Medium button sizes. */
    @Composable
    fun mediumSizes() = FinsibleButtonSizes(
        height = FinsibleTheme.dimes.d48,
        cornerRadius = FinsibleTheme.dimes.d12,
        iconSize = FinsibleTheme.dimes.d20,
        horizontalPadding = FinsibleTheme.dimes.d16,
        textStyle = FinsibleTheme.typography.t20
    )

    /** Large button sizes. */
    @Composable
    fun largeSizes() = FinsibleButtonSizes(
        height = FinsibleTheme.dimes.d56,
        cornerRadius = FinsibleTheme.dimes.d12,
        iconSize = FinsibleTheme.dimes.d24,
        horizontalPadding = FinsibleTheme.dimes.d20,
        textStyle = FinsibleTheme.typography.t24
    )
}

@Composable
fun FinsibleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    fullWidth: Boolean = false,
    colors: FinsibleButtonColors = FinsibleButtonDefaults.brandColors(),
    sizes: FinsibleButtonSizes = FinsibleButtonDefaults.mediumSizes(),
    cornerRadius: Dp? = null,
    icon: Int? = null,
    iconPosition: FinsibleButtonIconPosition = FinsibleButtonIconPosition.Leading,
    tintIcon: Boolean = true
) {
    val isEnabled = enabled && !loading
    val interactionSource = remember { MutableInteractionSource() }
    val effectiveCornerRadius = cornerRadius ?: sizes.cornerRadius

    val buttonModifier = modifier
        .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
        .height(sizes.height)

    val isOutlined = colors.borderColor != null && colors.containerColor == Color.Transparent
    val isText = colors.containerColor == Color.Transparent && colors.borderColor == null

    when {
        isOutlined -> {
            OutlinedButton(
                onClick = onClick,
                enabled = isEnabled,
                shape = RoundedCornerShape(effectiveCornerRadius),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colors.contentColor
                ),
                border = BorderStroke(
                    width = FinsibleTheme.dimes.d1,
                    color = colors.borderColor ?: FinsibleTheme.colors.border
                ),
                interactionSource = interactionSource,
                contentPadding = PaddingValues(horizontal = sizes.horizontalPadding),
                modifier = buttonModifier
            ) {
                ButtonContent(
                    text = text,
                    colors = colors,
                    sizes = sizes,
                    icon = icon,
                    iconPosition = iconPosition,
                    tintIcon = tintIcon,
                    loading = loading
                )
            }
        }

        isText -> {
            TextButton(
                onClick = onClick,
                enabled = isEnabled,
                shape = RoundedCornerShape(effectiveCornerRadius),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (isEnabled) colors.contentColor
                    else FinsibleTheme.colors.disabledContent
                ),
                interactionSource = interactionSource,
                contentPadding = PaddingValues(horizontal = sizes.horizontalPadding),
                modifier = buttonModifier
            ) {
                ButtonContent(
                    text = text,
                    colors = colors,
                    sizes = sizes,
                    icon = icon,
                    iconPosition = iconPosition,
                    tintIcon = tintIcon,
                    loading = loading
                )
            }
        }

        else -> {
            Button(
                onClick = onClick,
                enabled = isEnabled,
                shape = RoundedCornerShape(effectiveCornerRadius),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.containerColor,
                    contentColor = colors.contentColor
                ),
                interactionSource = interactionSource,
                contentPadding = PaddingValues(horizontal = sizes.horizontalPadding),
                modifier = buttonModifier
            ) {
                ButtonContent(
                    text = text,
                    colors = colors,
                    sizes = sizes,
                    icon = icon,
                    iconPosition = iconPosition,
                    tintIcon = tintIcon,
                    loading = loading
                )
            }
        }
    }
}

@Composable
private fun ButtonContent(
    text: String,
    colors: FinsibleButtonColors,
    sizes: FinsibleButtonSizes,
    icon: Int?,
    iconPosition: FinsibleButtonIconPosition,
    tintIcon: Boolean,
    loading: Boolean
) {
    val isDark = FinsibleTheme.isDarkTheme()
    val isFilled = colors.containerColor != Color.Transparent
    // SemiBold improves contrast: filled buttons on dark backgrounds and unfilled on light.
    val adjustWeight = (isDark && isFilled) || (!isDark && !isFilled)

    @Composable
    fun ButtonLabel() = Text(
        text = text,
        style = sizes.textStyle.copy(
            fontWeight = if (adjustWeight) FontWeight.SemiBold else FontWeight.Medium
        )
    )

    @Composable
    fun ButtonIcon(modifier: Modifier) = Icon(
        painter = painterResource(id = icon!!),
        contentDescription = null,
        modifier = modifier.size(sizes.iconSize),
        tint = if (tintIcon) LocalContentColor.current else Color.Unspecified
    )

    if (loading) {
        CircularProgressIndicator(
            modifier = Modifier.size(sizes.iconSize),
            strokeWidth = FinsibleTheme.dimes.d2,
            color = LocalContentColor.current
        )
        return
    }

    if (iconPosition == FinsibleButtonIconPosition.BeforeLabel ||
        iconPosition == FinsibleButtonIconPosition.AfterLabel
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (icon != null && iconPosition == FinsibleButtonIconPosition.BeforeLabel)
                ButtonIcon(modifier = Modifier.size(sizes.iconSize))

            ButtonLabel()

            if (icon != null && iconPosition == FinsibleButtonIconPosition.AfterLabel)
                ButtonIcon(modifier = Modifier.size(sizes.iconSize))
        }
        return
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = if (icon != null) Modifier.fillMaxWidth() else Modifier
    ) {
        if (icon != null)
            ButtonIcon(
                modifier = Modifier
                    .size(sizes.iconSize)
                    .align(
                        if (iconPosition == FinsibleButtonIconPosition.Leading)
                            Alignment.CenterStart
                        else Alignment.CenterEnd
                    )
            )

        ButtonLabel()
    }
}