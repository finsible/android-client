package com.itsjeel01.finsiblefrontend.ui.screen.playground.helper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleDropdown
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleToggle
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDropdownOption
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleToggleArrangement
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleToggleLabelPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun <T> OptionDropdown(
    label: String,
    selectedLabel: String,
    options: List<T>,
    optionLabel: @Composable (T) -> String,
    onSelect: (T) -> Unit,
    enabled: Boolean = true,
    helperText: String? = null
) {
    val dropdownEntries = options.mapIndexed { index, option ->
        val displayLabel = optionLabel(option)
        FinsibleDropdownOption(id = index.toString(), label = displayLabel) to option
    }

    val selectedId = dropdownEntries.firstOrNull { it.first.label == selectedLabel }?.first?.id
    val placeholderText = selectedLabel.ifBlank { label }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4)) {
            FinsibleText(
                text = label,
                variant = FinsibleTextVariant.BodyRegular,
                color = FinsibleTheme.colors.primaryContent
            )
            helperText?.let {
                FinsibleText(
                    text = it,
                    variant = FinsibleTextVariant.SmallLabelRegular,
                    color = FinsibleTheme.colors.tertiaryContent
                )
            }
        }

        FinsibleDropdown(
            options = dropdownEntries.map { it.first },
            selectedId = selectedId,
            onSelected = { optionId ->
                dropdownEntries.firstOrNull { it.first.id == optionId }?.second?.let(onSelect)
            },
            enabled = enabled,
            placeholder = placeholderText,
            size = FinsibleSize.Small,
            fullWidth = false
        )
    }
}

@Composable
fun OptionToggle(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    helperText: String? = null
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4)
    ) {
        FinsibleToggle(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            label = label,
            size = FinsibleSize.Medium,
            arrangement = FinsibleToggleArrangement.SpaceBetween,
            labelPosition = FinsibleToggleLabelPosition.Leading
        )

        helperText?.let {
            FinsibleText(
                text = it,
                variant = FinsibleTextVariant.SmallLabelRegular,
                color = FinsibleTheme.colors.tertiaryContent
            )
        }
    }
}

@Composable
fun OptionSlider(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    onValueChange: (Float) -> Unit,
    enabled: Boolean = true,
    helperText: String? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FinsibleText(
                text = label,
                variant = FinsibleTextVariant.SmallBodyRegular,
                color = FinsibleTheme.colors.primaryContent
            )
            FinsibleText(
                text = value.toInt().toString(),
                variant = FinsibleTextVariant.SmallLabelRegular,
                color = FinsibleTheme.colors.secondaryContent
            )
        }

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps,
            enabled = enabled,
            colors = SliderDefaults.colors(
                activeTrackColor = FinsibleTheme.colors.brandAccent,
                inactiveTrackColor = FinsibleTheme.colors.divider,
                thumbColor = FinsibleTheme.colors.brandAccent
            )
        )

        helperText?.let {
            FinsibleText(
                text = it,
                variant = FinsibleTextVariant.SmallLabelRegular,
                color = FinsibleTheme.colors.tertiaryContent
            )
        }
    }
}

@Composable
fun FinsibleLabeledTextField(
    label: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4)) {
        FinsibleText(
            text = label,
            variant = FinsibleTextVariant.SmallBodyRegular,
            color = FinsibleTheme.colors.primaryContent
        )
        androidx.compose.material3.OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                FinsibleText(
                    text = stringResource(R.string.component_playground_text_placeholder),
                    variant = FinsibleTextVariant.SmallLabelRegular,
                    color = FinsibleTheme.colors.secondaryContent
                )
            }
        )
    }
}
