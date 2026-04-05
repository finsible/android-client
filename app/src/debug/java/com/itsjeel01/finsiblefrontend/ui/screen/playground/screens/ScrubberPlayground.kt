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
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleScrubber
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleScrubberDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleScrubberVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.screen.playground.OptionDropdown
import com.itsjeel01.finsiblefrontend.ui.screen.playground.OptionSlider
import com.itsjeel01.finsiblefrontend.ui.screen.playground.OptionToggle
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import kotlin.math.max

private enum class ScrubberColorPreset {
    Default,
    Info,
    Warning,
    Success
}

@Composable
fun ScrubberPlayground() {
    var index by rememberSaveable { mutableStateOf(1) }
    var total by rememberSaveable { mutableStateOf(5f) }
    var enabled by rememberSaveable { mutableStateOf(true) }
    var variant by rememberSaveable { mutableStateOf(FinsibleScrubberVariant.Separate) }
    var colorPreset by rememberSaveable { mutableStateOf(ScrubberColorPreset.Default) }
    var useCustomSizes by rememberSaveable { mutableStateOf(false) }
    var useCustomContentDescription by rememberSaveable { mutableStateOf(false) }
    var activeWidth by rememberSaveable { mutableStateOf(28f) }
    var inactiveWidth by rememberSaveable { mutableStateOf(12f) }
    var barHeight by rememberSaveable { mutableStateOf(4f) }
    var spacing by rememberSaveable { mutableStateOf(8f) }
    var cornerRadius by rememberSaveable { mutableStateOf(2f) }
    var minTouchTarget by rememberSaveable { mutableStateOf(24f) }
    var scrubEndCount by rememberSaveable { mutableStateOf(0) }

    val totalCount = total.toInt().coerceAtLeast(1)
    if (index >= totalCount) index = totalCount - 1
    if (index < 0) index = 0

    val colors = when (colorPreset) {
        ScrubberColorPreset.Default -> FinsibleScrubberDefaults.colors()
        ScrubberColorPreset.Info -> FinsibleScrubberDefaults.colors(
            currentColor = FinsibleTheme.colors.info,
            restColor = FinsibleTheme.colors.infoContainer
        )

        ScrubberColorPreset.Warning -> FinsibleScrubberDefaults.colors(
            currentColor = FinsibleTheme.colors.warning,
            restColor = FinsibleTheme.colors.warningContainer
        )

        ScrubberColorPreset.Success -> FinsibleScrubberDefaults.colors(
            currentColor = FinsibleTheme.colors.success,
            restColor = FinsibleTheme.colors.successContainer
        )
    }

    val sizes = if (useCustomSizes) {
        FinsibleScrubberDefaults.sizes().copy(
            activeBarWidth = activeWidth.coerceAtLeast(1f).dp,
            inactiveBarWidth = inactiveWidth.coerceAtLeast(1f).dp,
            barHeight = barHeight.coerceAtLeast(1f).dp,
            barSpacing = spacing.coerceAtLeast(0f).dp,
            cornerRadius = cornerRadius.coerceAtLeast(0f).dp,
            minTouchTargetHeight = minTouchTarget.coerceAtLeast(1f).dp
        )
    } else {
        FinsibleScrubberDefaults.sizes()
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = FinsibleTheme.dimes.d16, vertical = FinsibleTheme.dimes.d12),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12)
    ) {
        FinsibleText(
            text = stringResource(
                R.string.component_playground_scrubber_status,
                index + 1,
                totalCount,
                scrubEndCount
            ),
            variant = FinsibleTextVariant.SmallBodyRegular,
            color = FinsibleTheme.colors.secondaryContent
        )

        FinsibleScrubber(
            currentIndex = index,
            totalCount = totalCount,
            onIndexChange = { index = it },
            enabled = enabled,
            variant = variant,
            colors = colors,
            sizes = sizes,
            scrubberContentDescription = if (useCustomContentDescription) {
                stringResource(R.string.component_playground_scrubber_content_description_custom)
            } else {
                null
            },
            onScrubEnd = { scrubEndCount += 1 }
        )

        OptionSlider(
            label = stringResource(R.string.component_playground_scrubber_index),
            value = index.toFloat(),
            valueRange = 0f..(totalCount - 1).toFloat(),
            steps = max(totalCount - 2, 0),
            onValueChange = { index = it.toInt() },
            enabled = totalCount > 1,
            helperText = stringResource(R.string.component_playground_scrubber_index_helper)
        )

        OptionSlider(
            label = stringResource(R.string.component_playground_scrubber_total),
            value = total,
            valueRange = 1f..24f,
            steps = 22,
            onValueChange = { total = it }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_scrubber_variant_label),
            selectedLabel = scrubberVariantLabel(variant),
            options = FinsibleScrubberVariant.entries,
            optionLabel = { scrubberVariantLabel(it) },
            onSelect = { variant = it }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_scrubber_color_preset),
            selectedLabel = scrubberColorPresetLabel(colorPreset),
            options = ScrubberColorPreset.entries,
            optionLabel = { scrubberColorPresetLabel(it) },
            onSelect = { colorPreset = it }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_generic_enabled),
            checked = enabled,
            onCheckedChange = { enabled = it }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_scrubber_custom_semantics),
            checked = useCustomContentDescription,
            onCheckedChange = { useCustomContentDescription = it },
            helperText = stringResource(R.string.component_playground_scrubber_custom_semantics_helper)
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_scrubber_custom_sizes),
            checked = useCustomSizes,
            onCheckedChange = { useCustomSizes = it }
        )

        OptionSlider(
            label = stringResource(R.string.component_playground_scrubber_size_active_width),
            value = activeWidth,
            valueRange = 8f..60f,
            steps = 51,
            onValueChange = { activeWidth = it },
            enabled = useCustomSizes
        )

        OptionSlider(
            label = stringResource(R.string.component_playground_scrubber_size_inactive_width),
            value = inactiveWidth,
            valueRange = 4f..40f,
            steps = 35,
            onValueChange = { inactiveWidth = it },
            enabled = useCustomSizes
        )

        OptionSlider(
            label = stringResource(R.string.component_playground_scrubber_size_height),
            value = barHeight,
            valueRange = 2f..12f,
            steps = 9,
            onValueChange = { barHeight = it },
            enabled = useCustomSizes
        )

        OptionSlider(
            label = stringResource(R.string.component_playground_scrubber_size_spacing),
            value = spacing,
            valueRange = 0f..24f,
            steps = 23,
            onValueChange = { spacing = it },
            enabled = useCustomSizes
        )

        OptionSlider(
            label = stringResource(R.string.component_playground_scrubber_size_corner),
            value = cornerRadius,
            valueRange = 0f..12f,
            steps = 11,
            onValueChange = { cornerRadius = it },
            enabled = useCustomSizes
        )

        OptionSlider(
            label = stringResource(R.string.component_playground_scrubber_size_touch_target),
            value = minTouchTarget,
            valueRange = 16f..56f,
            steps = 39,
            onValueChange = { minTouchTarget = it },
            enabled = useCustomSizes
        )

    }
}

@Composable
private fun scrubberColorPresetLabel(preset: ScrubberColorPreset): String = when (preset) {
    ScrubberColorPreset.Default -> stringResource(R.string.component_playground_scrubber_color_default)
    ScrubberColorPreset.Info -> stringResource(R.string.component_playground_scrubber_color_info)
    ScrubberColorPreset.Warning -> stringResource(R.string.component_playground_scrubber_color_warning)
    ScrubberColorPreset.Success -> stringResource(R.string.component_playground_scrubber_color_success)
}

@Composable
private fun scrubberVariantLabel(variant: FinsibleScrubberVariant): String = when (variant) {
    FinsibleScrubberVariant.Separate -> stringResource(R.string.component_playground_scrubber_variant_separate)
    FinsibleScrubberVariant.Continuous -> stringResource(R.string.component_playground_scrubber_variant_continuous)
}

