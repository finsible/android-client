package com.itsjeel01.finsiblefrontend.ui.screen.playground.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleCheckbox
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleCheckboxVariant
import com.itsjeel01.finsiblefrontend.ui.screen.playground.FinsibleLabeledTextField
import com.itsjeel01.finsiblefrontend.ui.screen.playground.OptionDropdown
import com.itsjeel01.finsiblefrontend.ui.screen.playground.OptionToggle
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun CheckboxPlayground() {
    val defaultLabel = stringResource(R.string.component_playground_checkbox_default_label)
    var checked by rememberSaveable { mutableStateOf(true) }
    var enabled by rememberSaveable { mutableStateOf(true) }
    var animate by rememberSaveable { mutableStateOf(true) }
    var variant by rememberSaveable { mutableStateOf(FinsibleCheckboxVariant.Colorful) }
    var size by rememberSaveable { mutableStateOf(FinsibleSize.Medium) }
    var shape by rememberSaveable { mutableStateOf(FinsibleShape.Rounded) }
    var showLabel by rememberSaveable { mutableStateOf(true) }
    var labelField by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(defaultLabel)) }

    val supportedSizes = listOf(FinsibleSize.Small, FinsibleSize.Medium, FinsibleSize.Large)
    val supportedShapes = listOf(FinsibleShape.Rounded, FinsibleShape.Sharp)

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = FinsibleTheme.dimes.d16, vertical = FinsibleTheme.dimes.d12),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12)
    ) {
        FinsibleCheckbox(
            checked = checked,
            onCheckedChange = { checked = it },
            enabled = enabled,
            animateChecking = animate,
            variant = variant,
            size = size,
            shapeVariant = shape,
            label = if (showLabel && labelField.text.isNotBlank()) labelField.text else null
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_checkbox_option_checked),
            checked = checked,
            onCheckedChange = { checked = it }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_checkbox_option_enabled),
            checked = enabled,
            onCheckedChange = { enabled = it }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_checkbox_option_animate),
            checked = animate,
            onCheckedChange = { animate = it }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_checkbox_option_variant),
            selectedLabel = checkboxVariantLabel(variant),
            options = FinsibleCheckboxVariant.entries,
            optionLabel = { checkboxVariantLabel(it) },
            onSelect = { variant = it }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_checkbox_option_size),
            selectedLabel = sizeLabel(size),
            options = supportedSizes,
            optionLabel = { sizeLabel(it) },
            onSelect = { size = it },
            helperText = stringResource(R.string.component_playground_checkbox_hint_size_support)
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_checkbox_option_shape),
            selectedLabel = shapeLabel(shape),
            options = supportedShapes,
            optionLabel = { shapeLabel(it) },
            onSelect = { shape = it },
            helperText = stringResource(R.string.component_playground_checkbox_hint_shape_support)
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_checkbox_option_show_label),
            checked = showLabel,
            onCheckedChange = { showLabel = it }
        )

        if (showLabel) {
            FinsibleLabeledTextField(
                label = stringResource(R.string.component_playground_checkbox_label_input),
                value = labelField,
                onValueChange = { newValue -> labelField = newValue }
            )
        }
    }
}

