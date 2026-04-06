package com.itsjeel01.finsiblefrontend.ui.screen.playground.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleTextField
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleTextFieldDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.screen.playground.FinsibleLabeledTextField
import com.itsjeel01.finsiblefrontend.ui.screen.playground.OptionDropdown
import com.itsjeel01.finsiblefrontend.ui.screen.playground.OptionToggle
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.composables.icons.lucide.R as LucideR

@Composable
fun TextFieldPlayground() {
    var value by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    var enabled by rememberSaveable { mutableStateOf(true) }
    var error by rememberSaveable { mutableStateOf(false) }
    var readOnly by rememberSaveable { mutableStateOf(false) }
    var leadingIcon by rememberSaveable { mutableStateOf(true) }
    var trailingIcon by rememberSaveable { mutableStateOf(false) }
    var size by rememberSaveable { mutableStateOf(FinsibleSize.Medium) }
    var shape by rememberSaveable { mutableStateOf(FinsibleShape.Rounded) }
    var singleLine by rememberSaveable { mutableStateOf(true) }
    var minLines by rememberSaveable { mutableStateOf(2) }
    var maxLines by rememberSaveable { mutableStateOf(4) }
    var textAlignOption by rememberSaveable { mutableStateOf(TextAlignOption.Start) }
    var keyboardOption by rememberSaveable { mutableStateOf(KeyboardOption.Text) }
    var imeActionOption by rememberSaveable { mutableStateOf(ImeActionOption.Done) }
    var visualTransformationOption by rememberSaveable { mutableStateOf(VisualTransformationOption.None) }
    var filterOption by rememberSaveable { mutableStateOf(InputFilterOption.None) }
    var useMaxLength by rememberSaveable { mutableStateOf(false) }
    var maxLengthOption by rememberSaveable { mutableStateOf(MaxLengthOption.Ten) }
    var useContentDescription by rememberSaveable { mutableStateOf(false) }
    var leadingTapCount by rememberSaveable { mutableStateOf(0) }
    var trailingTapCount by rememberSaveable { mutableStateOf(0) }

    val defaultPlaceholder = stringResource(R.string.component_playground_textfield_placeholder)
    var placeholderValue by rememberSaveable(stateSaver = TextFieldValue.Saver, inputs = arrayOf(defaultPlaceholder)) {
        mutableStateOf(TextFieldValue(defaultPlaceholder))
    }

    var showLabel by rememberSaveable { mutableStateOf(false) }
    val defaultLabel = stringResource(R.string.component_playground_textfield_label_default)
    var labelValue by rememberSaveable(stateSaver = TextFieldValue.Saver, inputs = arrayOf(defaultLabel)) { mutableStateOf(TextFieldValue(defaultLabel)) }
    var showSupporting by rememberSaveable { mutableStateOf(false) }
    val defaultSupporting = stringResource(R.string.component_playground_textfield_supporting_default)
    var supportingValue by rememberSaveable(stateSaver = TextFieldValue.Saver, inputs = arrayOf(defaultSupporting)) { mutableStateOf(TextFieldValue(defaultSupporting)) }
    val defaultContentDescription = stringResource(R.string.component_playground_textfield_content_description_default)
    var contentDescriptionValue by rememberSaveable(stateSaver = TextFieldValue.Saver, inputs = arrayOf(defaultContentDescription)) {
        mutableStateOf(TextFieldValue(defaultContentDescription))
    }

    val resolvedMaxLength = if (useMaxLength) maxLengthOption.length else null
    val effectivePlaceholder = placeholderValue.text.ifBlank { defaultPlaceholder }
    val inputConfig = FinsibleTextFieldDefaults.inputConfig(
        keyboardType = keyboardOption.keyboardType,
        imeAction = imeActionOption.imeAction,
        maxLength = resolvedMaxLength
    )

    val inputFilter: ((String) -> Boolean)? = when (filterOption) {
        InputFilterOption.None -> null
        InputFilterOption.DigitsOnly -> { candidate -> candidate.all(Char::isDigit) }
        InputFilterOption.Amount -> { candidate ->
            candidate.all { it.isDigit() || it == '.' || it == ',' } &&
                candidate.count { it == '.' || it == ',' } <= 1
        }
    }

    val textAlign = textAlignOption.value
    val visualTransformation = when (visualTransformationOption) {
        VisualTransformationOption.None -> VisualTransformation.None
        VisualTransformationOption.Password -> PasswordVisualTransformation()
    }

    val appliedContentDescription = if (useContentDescription) {
        contentDescriptionValue.text.ifBlank { defaultContentDescription }
    } else {
        null
    }

    val supportingText = if (showSupporting && supportingValue.text.isNotBlank()) {
        supportingValue.text
    } else {
        null
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = FinsibleTheme.dimes.d16, vertical = FinsibleTheme.dimes.d12),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12)
    ) {
        FinsibleTextField(
            value = value.text,
            onValueChange = { value = value.copy(text = it) },
            placeholder = effectivePlaceholder,
            label = if (showLabel && labelValue.text.isNotBlank()) labelValue.text else null,
            supportingText = supportingText,
            enabled = enabled,
            isError = error,
            readOnly = readOnly,
            size = size,
            shapeVariant = shape,
            inputConfig = inputConfig,
            visualTransformation = visualTransformation,
            textAlign = textAlign,
            singleLine = singleLine,
            minLines = if (singleLine) 1 else minLines,
            maxLines = if (singleLine) 1 else maxLines,
            contentDescription = appliedContentDescription,
            inputFilter = inputFilter,
            leadingIcon = if (leadingIcon) {
                {
                    Icon(
                        painter = painterResource(LucideR.drawable.lucide_ic_search),
                        contentDescription = null,
                        modifier = Modifier.clickable(enabled = enabled) { leadingTapCount += 1 }
                    )
                }
            } else null,
            trailingIcon = if (trailingIcon) {
                {
                    Icon(
                        painter = painterResource(LucideR.drawable.lucide_ic_x),
                        contentDescription = null,
                        modifier = Modifier.clickable(enabled = enabled) {
                            trailingTapCount += 1
                            value = value.copy(text = "")
                        }
                    )
                }
            } else null
        )

        FinsibleText(
            text = stringResource(
                R.string.component_playground_textfield_status,
                value.text.length,
                resolvedMaxLength?.toString() ?: "-",
                leadingTapCount,
                trailingTapCount
            ),
            variant = FinsibleTextVariant.SmallLabelRegular,
            color = FinsibleTheme.colors.secondaryContent
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_generic_enabled),
            checked = enabled,
            onCheckedChange = { enabled = it }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_textfield_read_only),
            checked = readOnly,
            onCheckedChange = { readOnly = it }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_textfield_error),
            checked = error,
            onCheckedChange = { error = it }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_generic_show_icons),
            checked = leadingIcon,
            onCheckedChange = { leadingIcon = it }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_textfield_trailing_icon),
            checked = trailingIcon,
            onCheckedChange = { trailingIcon = it }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_textfield_alignment),
            selectedLabel = textAlignOptionLabel(textAlignOption),
            options = TextAlignOption.entries,
            optionLabel = { textAlignOptionLabel(it) },
            onSelect = { textAlignOption = it }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_textfield_keyboard_type),
            selectedLabel = keyboardOptionLabel(keyboardOption),
            options = KeyboardOption.entries,
            optionLabel = { keyboardOptionLabel(it) },
            onSelect = { keyboardOption = it }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_textfield_ime_action),
            selectedLabel = imeActionOptionLabel(imeActionOption),
            options = ImeActionOption.entries,
            optionLabel = { imeActionOptionLabel(it) },
            onSelect = { imeActionOption = it }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_textfield_visual_transformation),
            selectedLabel = visualTransformationOptionLabel(visualTransformationOption),
            options = VisualTransformationOption.entries,
            optionLabel = { visualTransformationOptionLabel(it) },
            onSelect = { visualTransformationOption = it }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_textfield_input_filter),
            selectedLabel = inputFilterOptionLabel(filterOption),
            options = InputFilterOption.entries,
            optionLabel = { inputFilterOptionLabel(it) },
            onSelect = { filterOption = it }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_textfield_max_length_enabled),
            checked = useMaxLength,
            onCheckedChange = { useMaxLength = it },
            helperText = stringResource(R.string.component_playground_textfield_max_length_helper)
        )

        if (useMaxLength) {
            OptionDropdown(
                label = stringResource(R.string.component_playground_textfield_max_length),
                selectedLabel = maxLengthOptionLabel(maxLengthOption),
                options = MaxLengthOption.entries,
                optionLabel = { maxLengthOptionLabel(it) },
                onSelect = { maxLengthOption = it }
            )
        }

        OptionDropdown(
            label = stringResource(R.string.component_playground_generic_size),
            selectedLabel = sizeLabel(size),
            options = FinsibleSize.entries,
            optionLabel = { sizeLabel(it) },
            onSelect = { size = it }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_generic_shape),
            selectedLabel = shapeLabel(shape),
            options = listOf(FinsibleShape.Rounded, FinsibleShape.Pill, FinsibleShape.Sharp),
            optionLabel = { shapeLabel(it) },
            onSelect = { shape = it }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_textfield_single_line),
            checked = singleLine,
            onCheckedChange = { singleLine = it }
        )

        if (!singleLine) {
            OptionDropdown(
                label = stringResource(R.string.component_playground_textfield_min_lines),
                selectedLabel = minLines.toString(),
                options = (1..4).toList(),
                optionLabel = { it.toString() },
                onSelect = {
                    minLines = it
                    if (maxLines < minLines) maxLines = minLines
                }
            )

            OptionDropdown(
                label = stringResource(R.string.component_playground_textfield_max_lines),
                selectedLabel = maxLines.toString(),
                options = (minLines..6).toList(),
                optionLabel = { it.toString() },
                onSelect = { maxLines = it }
            )
        }

        OptionToggle(
            label = stringResource(R.string.component_playground_textfield_custom_content_description),
            checked = useContentDescription,
            onCheckedChange = { useContentDescription = it }
        )

        if (useContentDescription) {
            FinsibleLabeledTextField(
                label = stringResource(R.string.component_playground_textfield_content_description_label),
                value = contentDescriptionValue,
                onValueChange = { contentDescriptionValue = it }
            )
        }

        FinsibleLabeledTextField(
            label = stringResource(R.string.component_playground_textfield_placeholder_input),
            value = placeholderValue,
            onValueChange = { placeholderValue = it }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_textfield_show_label),
            checked = showLabel,
            onCheckedChange = { showLabel = it }
        )

        if (showLabel) {
            FinsibleLabeledTextField(
                label = stringResource(R.string.component_playground_textfield_label_input),
                value = labelValue,
                onValueChange = { labelValue = it }
            )
        }

        OptionToggle(
            label = stringResource(R.string.component_playground_textfield_show_supporting),
            checked = showSupporting,
            onCheckedChange = { showSupporting = it }
        )

        if (showSupporting) {
            FinsibleLabeledTextField(
                label = stringResource(R.string.component_playground_textfield_supporting_input),
                value = supportingValue,
                onValueChange = { supportingValue = it }
            )
        }
    }
}

@Immutable
private enum class TextAlignOption(val value: TextAlign) {
    Start(TextAlign.Start),
    Center(TextAlign.Center),
    End(TextAlign.End)
}

@Immutable
private enum class KeyboardOption(val keyboardType: KeyboardType) {
    Text(KeyboardType.Text),
    Number(KeyboardType.Number),
    Decimal(KeyboardType.Decimal),
    Email(KeyboardType.Email),
    Phone(KeyboardType.Phone),
    Password(KeyboardType.Password)
}

@Immutable
private enum class ImeActionOption(val imeAction: ImeAction) {
    Done(ImeAction.Done),
    Next(ImeAction.Next),
    Search(ImeAction.Search),
    Send(ImeAction.Send),
    Go(ImeAction.Go)
}

@Immutable
private enum class VisualTransformationOption {
    None,
    Password
}

@Immutable
private enum class InputFilterOption {
    None,
    DigitsOnly,
    Amount
}

@Immutable
private enum class MaxLengthOption(val length: Int) {
    Eight(8),
    Ten(10),
    Twenty(20),
    Fifty(50)
}

@Composable
private fun textAlignOptionLabel(option: TextAlignOption): String = when (option) {
    TextAlignOption.Start -> stringResource(R.string.component_playground_textfield_alignment_start)
    TextAlignOption.Center -> stringResource(R.string.component_playground_textfield_alignment_center)
    TextAlignOption.End -> stringResource(R.string.component_playground_textfield_alignment_end)
}

@Composable
private fun keyboardOptionLabel(option: KeyboardOption): String = when (option) {
    KeyboardOption.Text -> stringResource(R.string.component_playground_textfield_keyboard_text)
    KeyboardOption.Number -> stringResource(R.string.component_playground_textfield_keyboard_number)
    KeyboardOption.Decimal -> stringResource(R.string.component_playground_textfield_keyboard_decimal)
    KeyboardOption.Email -> stringResource(R.string.component_playground_textfield_keyboard_email)
    KeyboardOption.Phone -> stringResource(R.string.component_playground_textfield_keyboard_phone)
    KeyboardOption.Password -> stringResource(R.string.component_playground_textfield_keyboard_password)
}

@Composable
private fun imeActionOptionLabel(option: ImeActionOption): String = when (option) {
    ImeActionOption.Done -> stringResource(R.string.component_playground_textfield_ime_done)
    ImeActionOption.Next -> stringResource(R.string.component_playground_textfield_ime_next)
    ImeActionOption.Search -> stringResource(R.string.component_playground_textfield_ime_search)
    ImeActionOption.Send -> stringResource(R.string.component_playground_textfield_ime_send)
    ImeActionOption.Go -> stringResource(R.string.component_playground_textfield_ime_go)
}

@Composable
private fun visualTransformationOptionLabel(option: VisualTransformationOption): String = when (option) {
    VisualTransformationOption.None -> stringResource(R.string.component_playground_textfield_transformation_none)
    VisualTransformationOption.Password -> stringResource(R.string.component_playground_textfield_transformation_password)
}

@Composable
private fun inputFilterOptionLabel(option: InputFilterOption): String = when (option) {
    InputFilterOption.None -> stringResource(R.string.component_playground_textfield_filter_none)
    InputFilterOption.DigitsOnly -> stringResource(R.string.component_playground_textfield_filter_digits)
    InputFilterOption.Amount -> stringResource(R.string.component_playground_textfield_filter_amount)
}

@Composable
private fun maxLengthOptionLabel(option: MaxLengthOption): String {
    return stringResource(R.string.component_playground_textfield_max_length_value, option.length)
}

