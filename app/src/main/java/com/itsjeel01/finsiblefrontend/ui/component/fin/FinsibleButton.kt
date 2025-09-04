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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun FinsibleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    config: ButtonConfig = ButtonConfig()
) {
    val isEnabled = config.enabled && !config.loading
    val interactionSource = remember { MutableInteractionSource() }

    val buttonModifier = modifier
        .then(
            if (config.fullWidth)
                Modifier.fillMaxWidth()
            else Modifier
        )
        .height(config.size.buttonHeight)

    when (config.type) {
        ComponentType.Secondary -> {
            OutlinedButton(
                onClick = onClick,
                enabled = isEnabled,
                shape = RoundedCornerShape(config.effectiveCornerRadius),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = config.type.contentColor()
                ),
                border = BorderStroke(
                    width = FinsibleTheme.dimes.d1,
                    color = config.type.borderColor() ?: FinsibleTheme.colors.border
                ),
                interactionSource = interactionSource,
                contentPadding = PaddingValues(horizontal = config.size.horizontalPadding),
                modifier = buttonModifier
            ) {
                ButtonContent(text = text, config = config)
            }
        }

        ComponentType.Tertiary -> {
            TextButton(
                onClick = onClick,
                enabled = isEnabled,
                shape = RoundedCornerShape(config.effectiveCornerRadius),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = config.type.contentColor()
                ),
                interactionSource = interactionSource,
                contentPadding = PaddingValues(horizontal = config.size.horizontalPadding),
                modifier = buttonModifier
            ) {
                ButtonContent(text = text, config = config)
            }
        }

        else -> {
            Button(
                onClick = onClick,
                enabled = isEnabled,
                shape = RoundedCornerShape(config.effectiveCornerRadius),
                colors = ButtonDefaults.buttonColors(
                    containerColor = config.type.containerColor(),
                    contentColor = config.type.contentColor()
                ),
                interactionSource = interactionSource,
                contentPadding = PaddingValues(horizontal = config.size.horizontalPadding),
                modifier = buttonModifier
            ) {
                ButtonContent(text = text, config = config)
            }
        }
    }
}

@Composable
private fun ButtonContent(
    text: String,
    config: ButtonConfig
) {

    val isDark = FinsibleTheme.isDarkTheme()
    val adjustWeightForVisibility =
        (isDark && (config.type == ComponentType.Primary || config.type == ComponentType.Brand)) ||
                (!isDark && (config.type == ComponentType.Secondary || config.type == ComponentType.Tertiary))

    @Composable
    fun ButtonLabel() = Text(
        text = text,
        style = config.size.typography().copy(
            fontWeight =
                if (adjustWeightForVisibility)
                    FontWeight.SemiBold
                else FontWeight.Medium
        )
    )

    @Composable
    fun ButtonIcon(modifier: Modifier) = Icon(
        painter = painterResource(id = config.icon!!),
        contentDescription = null,
        modifier = modifier.size(config.size.iconSize),
        tint = if (config.tintIcon) LocalContentColor.current else Color.Unspecified
    )

    // --- Loading State ---

    if (config.loading) {
        CircularProgressIndicator(
            modifier = Modifier.size(config.size.iconSize),
            strokeWidth = FinsibleTheme.dimes.d2,
            color = LocalContentColor.current
        )
        return
    }

    // -- Icon with Label (Before/After) ---

    if (config.iconPosition == IconPosition.BeforeLabel || config.iconPosition == IconPosition.AfterLabel) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (config.icon != null && config.iconPosition == IconPosition.BeforeLabel)
                ButtonIcon(modifier = Modifier.size(config.size.iconSize))

            ButtonLabel()

            if (config.icon != null && config.iconPosition == IconPosition.AfterLabel)
                ButtonIcon(modifier = Modifier.size(config.size.iconSize))
        }
        return
    }

    // --- Icon at Start/End ---

    Box(
        contentAlignment = Alignment.Center,
        modifier = if (config.icon != null) Modifier.fillMaxWidth() else Modifier
    ) {
        if (config.icon != null)
            ButtonIcon(
                modifier = Modifier
                    .size(config.size.iconSize)
                    .align(
                        if (config.iconPosition == IconPosition.Leading)
                            Alignment.CenterStart
                        else Alignment.CenterEnd
                    )
            )

        ButtonLabel()
    }
}

data class ButtonConfig(
    val type: ComponentType = ComponentType.Primary,
    val size: ComponentSize = ComponentSize.Medium,
    val fullWidth: Boolean = false,
    val icon: Int? = null,
    val iconPosition: IconPosition = IconPosition.Leading,
    val enabled: Boolean = true,
    val loading: Boolean = false,
    val customCornerRadius: Dp? = null,
    val tintIcon: Boolean = true
) {
    val effectiveCornerRadius: Dp
        @Composable
        get() = customCornerRadius ?: size.cornerRadius
}