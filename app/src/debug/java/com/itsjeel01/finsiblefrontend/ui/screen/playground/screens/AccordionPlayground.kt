package com.itsjeel01.finsiblefrontend.ui.screen.playground.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleAccordion
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleAccordionIconVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.screen.playground.OptionDropdown
import com.itsjeel01.finsiblefrontend.ui.screen.playground.OptionToggle
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.composables.icons.lucide.R as LucideR

@Composable
fun AccordionPlayground() {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var enabled by rememberSaveable { mutableStateOf(true) }
    var withSubtitle by rememberSaveable { mutableStateOf(true) }
    var withLeadingIcon by rememberSaveable { mutableStateOf(true) }
    var size by rememberSaveable { mutableStateOf(FinsibleSize.Medium) }
    var shape by rememberSaveable { mutableStateOf(FinsibleShape.Rounded) }
    var iconVariantChevron by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = FinsibleTheme.dimes.d16, vertical = FinsibleTheme.dimes.d12),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12)
    ) {
        FinsibleAccordion(
            title = stringResource(R.string.component_playground_accordion_title_sample),
            subtitle = if (withSubtitle) stringResource(R.string.component_playground_accordion_subtitle_sample) else null,
            expanded = expanded,
            onExpandedChange = { expanded = it },
            enabled = enabled,
            size = size,
            shapeVariant = shape,
            iconVariant = if (iconVariantChevron) FinsibleAccordionIconVariant.Chevron else FinsibleAccordionIconVariant.PlusMinus,
            leadingIcon = if (withLeadingIcon) {
                { Icon(painterResource(LucideR.drawable.lucide_ic_circle_alert), contentDescription = null) }
            } else null,
            content = {
                FinsibleText(
                    text = stringResource(R.string.component_playground_accordion_content_sample),
                    variant = FinsibleTextVariant.SmallLabelRegular,
                    color = FinsibleTheme.colors.secondaryContent
                )
            }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_generic_enabled),
            checked = enabled,
            onCheckedChange = { enabled = it }
        )
        OptionToggle(
            label = stringResource(R.string.component_playground_accordion_option_expanded),
            checked = expanded,
            onCheckedChange = { expanded = it }
        )
        OptionToggle(
            label = stringResource(R.string.component_playground_accordion_option_subtitle),
            checked = withSubtitle,
            onCheckedChange = { withSubtitle = it }
        )
        OptionToggle(
            label = stringResource(R.string.component_playground_accordion_option_leading),
            checked = withLeadingIcon,
            onCheckedChange = { withLeadingIcon = it }
        )
        OptionDropdown(
            label = stringResource(R.string.component_playground_generic_size),
            selectedLabel = sizeLabel(size),
            options = listOf(FinsibleSize.Small, FinsibleSize.Medium, FinsibleSize.Large),
            optionLabel = { sizeLabel(it) },
            onSelect = { size = it }
        )
        OptionDropdown(
            label = stringResource(R.string.component_playground_generic_shape),
            selectedLabel = shapeLabel(shape),
            options = listOf(FinsibleShape.Rounded, FinsibleShape.Sharp),
            optionLabel = { shapeLabel(it) },
            onSelect = { shape = it }
        )
        OptionToggle(
            label = stringResource(R.string.component_playground_accordion_option_icon_variant),
            checked = iconVariantChevron,
            onCheckedChange = { iconVariantChevron = it },
            helperText = stringResource(R.string.component_playground_accordion_option_icon_variant_helper)
        )
    }
}


