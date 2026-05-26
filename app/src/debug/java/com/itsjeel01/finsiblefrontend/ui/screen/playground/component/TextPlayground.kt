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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.FinsibleLabeledTextField
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.OptionDropdown
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.OptionToggle
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.textColorVariantLabel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold

@Composable
fun TextPlayground() {
    val t = FinsibleTheme.typography
    val textStyleOptions = listOf(
        "Display XL" to t.displayXl.bold(),
        "Display LG" to t.displayLg.bold(),
        "Display MD" to t.displayMd.bold(),
        "Display SM" to t.displaySm.bold(),
        "Heading LG" to t.headingLg,
        "Heading MD" to t.headingMd,
        "Heading SM" to t.headingSm,
        "Body LG" to t.bodyLg,
        "Body MD" to t.bodyMd,
        "Body SM" to t.bodySm,
        "Label LG" to t.labelLg,
        "Label MD" to t.labelMd,
        "Label SM" to t.labelSm,
        "Caption" to t.caption,
        "Numeral LG" to t.numeralLg,
        "Numeral MD" to t.numeralMd,
    )

    val defaultText = stringResource(R.string.component_playground_text_sample)
    var selectedIndex by rememberSaveable { mutableIntStateOf(6) } // Body LG
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
            .padding(horizontal = FinsibleTheme.spacing.insetLg, vertical = FinsibleTheme.spacing.gapMd),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackMd)
    ) {
        FinsibleText(
            text = text.text,
            textStyle = textStyleOptions[selectedIndex].second,
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
            selectedLabel = textStyleOptions[selectedIndex].first,
            options = textStyleOptions.indices.toList(),
            optionLabel = { textStyleOptions[it].first },
            onSelect = { selectedIndex = it }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_text_color_variant_label),
            selectedLabel = textColorVariantLabel(textColorVariant),
            options = FinsibleTextColorVariant.entries,
            optionLabel = { textColorVariantLabel(it) },
            onSelect = { textColorVariant = it }
        )

        OptionDropdown(
            label = "Font",
            selectedLabel = when (isDisplayFont) { true -> "Display"; false -> "Interface"; null -> "Default" },
            options = listOf(null, true, false),
            optionLabel = { when (it) { true -> "Display"; false -> "Interface"; null -> "Default" } },
            onSelect = { isDisplayFont = it }
        )

        OptionToggle(
            label = "Uppercase",
            checked = uppercase,
            onCheckedChange = { uppercase = it }
        )

        OptionToggle(
            label = "Underline",
            checked = underline,
            onCheckedChange = { underline = it }
        )

        OptionToggle(
            label = "Strikethrough",
            checked = strikethrough,
            onCheckedChange = { strikethrough = it }
        )

        OptionDropdown(
            label = "Max Lines",
            selectedLabel = maxLines.toString(),
            options = (1..5).toList(),
            optionLabel = { it.toString() },
            onSelect = { maxLines = it }
        )
    }
}
