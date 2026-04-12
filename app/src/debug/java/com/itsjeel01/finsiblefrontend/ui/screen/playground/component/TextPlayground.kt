package com.itsjeel01.finsiblefrontend.ui.screen.playground.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.FinsibleLabeledTextField
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.OptionDropdown
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.OptionToggle
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.textColorVariantLabel
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.textVariantLabel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun TextPlayground() {
    val defaultText = stringResource(R.string.component_playground_text_sample)
    var textVariant by rememberSaveable { mutableStateOf(FinsibleTextVariant.BodyRegular) }
    var textColorVariant by rememberSaveable { mutableStateOf(FinsibleTextColorVariant.Primary) }
    var uppercase by rememberSaveable { mutableStateOf(false) }
    var underline by rememberSaveable { mutableStateOf(false) }
    var strikethrough by rememberSaveable { mutableStateOf(false) }
    var isDisplayFont by rememberSaveable { mutableStateOf<Boolean?>(null) }
    var maxLines by rememberSaveable { mutableIntStateOf(3) }
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver, inputs = arrayOf(defaultText)) {
        mutableStateOf(TextFieldValue(defaultText))
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = FinsibleTheme.dimes.d16, vertical = FinsibleTheme.dimes.d12),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12)
    ) {
        FinsibleText(
            text = text.text,
            variant = textVariant,
            colorVariant = textColorVariant,
            maxLines = maxLines,
            uppercase = uppercase,
            underline = underline,
            strikethrough = strikethrough,
            isDisplayFont = isDisplayFont
        )

        FinsibleLabeledTextField(
            label = stringResource(R.string.component_playground_text_label),
            value = text,
            onValueChange = { text = it }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_text_variant_selector),
            selectedLabel = textVariantLabel(textVariant),
            options = FinsibleTextVariant.entries,
            optionLabel = { textVariantLabel(it) },
            onSelect = { textVariant = it }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_text_color_variant_label),
            selectedLabel = textColorVariantLabel(textColorVariant),
            options = FinsibleTextColorVariant.entries,
            optionLabel = { textColorVariantLabel(it) },
            onSelect = { textColorVariant = it }
        )

        val fontLabelMap = { it: Boolean? ->
            when (it) {
                null -> "Default"; true -> "Display"; false -> "Interface"
            }
        }

        OptionDropdown(
            label = "Font Family",
            selectedLabel = fontLabelMap(isDisplayFont),
            options = listOf(null, true, false),
            optionLabel = { fontLabelMap(it) },
            onSelect = { isDisplayFont = it }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_text_max_lines),
            selectedLabel = maxLines.toString(),
            options = listOf(1, 2, 3, 4),
            optionLabel = { it.toString() },
            onSelect = { maxLines = it }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_text_uppercase),
            checked = uppercase,
            onCheckedChange = { uppercase = it }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_text_underline),
            checked = underline,
            onCheckedChange = { underline = it }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_text_strikethrough),
            checked = strikethrough,
            onCheckedChange = { strikethrough = it }
        )
    }
}
