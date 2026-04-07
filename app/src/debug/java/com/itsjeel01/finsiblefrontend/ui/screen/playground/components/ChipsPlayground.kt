package com.itsjeel01.finsiblefrontend.ui.screen.playground.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.composables.icons.lucide.R as LucideR
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleChipsRow
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleFilterChip
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleIconPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleFilterChipVariant
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.*
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleColors
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun ChipsPlayground() {
    var wrap by rememberSaveable { mutableStateOf(true) }
    var count by rememberSaveable { mutableStateOf(5f) }
    var selectedIndex by rememberSaveable { mutableStateOf(0) }
    var includeIcons by rememberSaveable { mutableStateOf(true) }
    var size by rememberSaveable { mutableStateOf(FinsibleSize.Medium) }
    var shape by rememberSaveable { mutableStateOf(FinsibleShape.Pill) }
    var variant by rememberSaveable { mutableStateOf(FinsibleFilterChipVariant.Tonal) }
    var useSelectedTint by rememberSaveable { mutableStateOf(false) }
    var inverted by rememberSaveable { mutableStateOf(false) }
    var tintOption by rememberSaveable { mutableStateOf(FilterChipTintOption.BrandAccent) }
    var tintAlpha by rememberSaveable { mutableFloatStateOf(1f) }

    val labels = listOf(
        stringResource(R.string.component_playground_chip_one),
        stringResource(R.string.component_playground_chip_two),
        stringResource(R.string.component_playground_chip_three),
        stringResource(R.string.component_playground_chip_four),
        stringResource(R.string.component_playground_chip_five),
        stringResource(R.string.component_playground_chip_six)
    ).take(count.toInt().coerceAtLeast(1))
    val resolvedTint = tintOption.resolveColor(FinsibleTheme.colors).copy(alpha = tintAlpha)

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = FinsibleTheme.dimes.d16, vertical = FinsibleTheme.dimes.d12),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12)
    ) {
        FinsibleChipsRow(
            chips = labels.mapIndexed { index, label ->
                @Composable {
                    FinsibleFilterChip(
                        selected = index == selectedIndex,
                        onSelectedChange = { isSelected ->
                            selectedIndex = if (isSelected) index else -1
                        },
                        label = label,
                        size = size,
                        shapeVariant = shape,
                        variant = variant,
                        icon = if (includeIcons) {
                            { Icon(painterResource(LucideR.drawable.lucide_ic_circle), contentDescription = null) }
                        } else null,
                        iconPosition = FinsibleIconPosition.Leading,
                        selectedTint = if (useSelectedTint) resolvedTint else Color.Unspecified,
                        inverted = inverted,
                        enforceMinTouchTarget = !wrap
                    )
                }
            },
            chipKeys = labels.mapIndexed { index, label -> "$index-$label" },
            wrap = wrap
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_chips_wrap),
            checked = wrap,
            onCheckedChange = { wrap = it }
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
            options = listOf(FinsibleShape.Pill, FinsibleShape.Rounded, FinsibleShape.Sharp),
            optionLabel = { shapeLabel(it) },
            onSelect = { shape = it }
        )
        OptionDropdown(
            label = stringResource(R.string.component_playground_filterchip_variant),
            selectedLabel = filterChipVariantLabel(variant),
            options = FinsibleFilterChipVariant.entries,
            optionLabel = { filterChipVariantLabel(it) },
            onSelect = { variant = it }
        )
        OptionToggle(
            label = stringResource(R.string.component_playground_chips_icons),
            checked = includeIcons,
            onCheckedChange = { includeIcons = it }
        )
        OptionToggle(
            label = stringResource(R.string.component_playground_filterchip_tint_enabled),
            checked = useSelectedTint,
            onCheckedChange = { useSelectedTint = it }
        )
        OptionToggle(
            label = stringResource(R.string.component_playground_filterchip_tint_inverted),
            checked = inverted,
            onCheckedChange = { inverted = it }
        )
        OptionDropdown(
            label = stringResource(R.string.component_playground_filterchip_tint_color),
            selectedLabel = filterChipTintOptionLabel(tintOption),
            options = FilterChipTintOption.entries,
            optionLabel = { filterChipTintOptionLabel(it) },
            onSelect = { tintOption = it }
        )
        OptionSlider(
            label = stringResource(R.string.component_playground_filterchip_tint_alpha),
            value = tintAlpha,
            valueRange = 0.2f..1f,
            steps = 7,
            onValueChange = { tintAlpha = it }
        )
        OptionSlider(
            label = stringResource(R.string.component_playground_chips_count),
            value = count,
            valueRange = 1f..6f,
            steps = 5,
            onValueChange = {
                count = it
                selectedIndex = selectedIndex.coerceIn(-1, it.toInt() - 1)
            }
        )
    }
}

private fun FilterChipTintOption.resolveColor(colors: FinsibleColors): Color = when (this) {
    FilterChipTintOption.BrandAccent -> colors.brandAccent
    FilterChipTintOption.Success -> colors.success
    FilterChipTintOption.Info -> colors.info
    FilterChipTintOption.Warning -> colors.warning
    FilterChipTintOption.Error -> colors.error
}


