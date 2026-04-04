package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleRadioButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleRadioButtonColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleRadioButtonSizes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.util.finsibleBounceIndication

/** Stateless radio button with optional leading icon and label.
 *
 * @param selected Whether this radio button is selected.
 * @param onSelectedChange Called when the user clicks the radio button to select it.
 * @param label The label to display.
 * @param modifier The [Modifier] to be applied to this radio button.
 * @param enabled Controls the enabled state of this radio button. When `false`, this component will not respond to user input,
 * and it will appear visually disabled and disabled to accessibility services.
 * @param size The size of the radio button.
 * @param colors The colors of the radio button.
 * @param sizes The sizes of the radio button.
 * @param icon The optional leading icon to display.
 * @param contentDescription A content description for the radio button.
 */
@Composable
fun FinsibleRadioButton(
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: FinsibleSize = FinsibleSize.Medium,
    colors: FinsibleRadioButtonColors = FinsibleRadioButtonDefaults.colors(),
    sizes: FinsibleRadioButtonSizes = FinsibleRadioButtonDefaults.sizes(size),
    icon: (@Composable () -> Unit)? = null,
    contentDescription: String? = null
) {
    require(label.isNotBlank()) { "label must be non-blank." }
    require(size == FinsibleSize.Small || size == FinsibleSize.Medium) {
        "FinsibleRadioButton supports only Small and Medium sizes."
    }

    val interactionSource = remember { MutableInteractionSource() }

    val animatedRingColor by animateColorAsState(
        targetValue = if (enabled) colors.ringColor else colors.disabledRingColor,
        animationSpec = tween(),
        label = "ringColor"
    )
    val animatedDotColor by animateColorAsState(
        targetValue = if (enabled) colors.dotColor else colors.disabledDotColor,
        animationSpec = tween(),
        label = "dotColor"
    )
    val animatedLabelColor by animateColorAsState(
        targetValue = if (enabled) colors.labelColor else colors.disabledLabelColor,
        animationSpec = tween(),
        label = "labelColor"
    )

    if (selected) {
        stringResource(R.string.finsible_radio_button_selected_state)
    } else {
        stringResource(R.string.finsible_radio_button_unselected_state)
    }

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = sizes.outerDiameter)
            .finsibleBounceIndication(interactionSource)
            .selectable(
                selected = selected,
                onClick = { if (enabled) onSelectedChange(!selected) },
                enabled = enabled,
                role = Role.RadioButton,
                interactionSource = interactionSource,
                indication = null
            )
            .semantics(mergeDescendants = true) {
                if (!contentDescription.isNullOrEmpty()) {
                    this.contentDescription = contentDescription
                }
                if (!enabled) disabled()
            },
        horizontalArrangement = Arrangement.spacedBy(sizes.spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BoxedRadio(
            selected = selected,
            ringColor = animatedRingColor,
            dotColor = animatedDotColor,
            sizes = sizes
        )

        if (icon != null) {
            CompositionLocalProvider(LocalContentColor provides animatedLabelColor) {
                icon()
            }
        }

        FinsibleText(
            text = label,
            variant = FinsibleTextVariant.BodyRegular,
            color = animatedLabelColor,
            textStyleOverride = sizes.labelStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun BoxedRadio(
    selected: Boolean,
    ringColor: Color,
    dotColor: Color,
    sizes: FinsibleRadioButtonSizes
) {
    val outerSize = sizes.outerDiameter
    val dotSize = sizes.dotDiameter

    Box(
        modifier = Modifier
            .size(outerSize)
            .clip(CircleShape)
            .border(width = sizes.ringWidth, color = ringColor, shape = CircleShape)
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(dotColor)
            )
        }
    }
}



