
package com.itsjeel01.finsiblefrontend.ui.screen.playground.component
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleSemanticColors


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
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleSegmentedButtonRow
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleSegmentAlignment
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleSegmentedButtonOption
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleSegmentedButtonVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleSegmentedButtonArrangement
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.*
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.composables.icons.lucide.R as LucideR

@Composable
fun SegmentedButtonsPlayground() {
    var selection by rememberSaveable { mutableStateOf(setOf("1")) }
    var singleSelection by rememberSaveable { mutableStateOf(true) }
    var enabled by rememberSaveable { mutableStateOf(true) }
    var size by rememberSaveable { mutableStateOf(FinsibleSize.Medium) }
    var shape by rememberSaveable { mutableStateOf(FinsibleShape.Rounded) }
    var variant by rememberSaveable { mutableStateOf(FinsibleSegmentedButtonVariant.Filled) }
    var arrangement by rememberSaveable { mutableStateOf(FinsibleSegmentedButtonArrangement.Clubbed) }
    var useSelectedTint by rememberSaveable { mutableStateOf(false) }
    var inverted by rememberSaveable { mutableStateOf(false) }
    var tintOption by rememberSaveable { mutableStateOf(FilterChipTintOption.BrandAccent) }
    var tintAlpha by rememberSaveable { mutableFloatStateOf(1f) }
    val baseTint = tintOption.resolveColor(FinsibleTheme.colors)
    val resolvedTint = baseTint.copy(alpha = tintAlpha)

    val options = listOf(
        FinsibleSegmentedButtonOption(
            id = "1",
            label = stringResource(R.string.component_playground_segment_one),
            icon = { iconModifier ->
                Icon(
                    painter = painterResource(LucideR.drawable.lucide_ic_circle),
                    contentDescription = null,
                    modifier = iconModifier
                )
            },
            alignment = FinsibleSegmentAlignment.Center
        ),
        FinsibleSegmentedButtonOption(
            id = "2",
            label = stringResource(R.string.component_playground_segment_two),
            icon = { iconModifier ->
                Icon(
                    painter = painterResource(LucideR.drawable.lucide_ic_square),
                    contentDescription = null,
                    modifier = iconModifier
                )
            },
            alignment = FinsibleSegmentAlignment.Center
        ),
        FinsibleSegmentedButtonOption(
            id = "3",
            label = stringResource(R.string.component_playground_segment_three),
            icon = null,
            alignment = FinsibleSegmentAlignment.Center
        )
    )

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = FinsibleTheme.spacing.insetLg, vertical = FinsibleTheme.spacing.gapMd),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackMd)
    ) {
        if (singleSelection) {
            FinsibleSegmentedButtonRow(
                options = options,
                selectedValue = selection.firstOrNull(),
                onSelectedValueChange = { next -> selection = next?.let(::setOf).orEmpty() },
                arrangement = arrangement,
                enabled = enabled,
                size = size,
                shapeVariant = shape,
                variant = variant,
                selectedTint = if (useSelectedTint) resolvedTint else Color.Unspecified,
                inverted = inverted
            )
        } else {
            FinsibleSegmentedButtonRow(
                options = options,
                selectedValues = selection,
                onSelectedValuesChange = { selection = it },
                arrangement = arrangement,
                enabled = enabled,
                size = size,
                shapeVariant = shape,
                variant = variant,
                selectedTint = if (useSelectedTint) resolvedTint else Color.Unspecified,
                inverted = inverted
            )
        }

        OptionToggle(
            label = stringResource(R.string.component_playground_segment_single),
            checked = singleSelection,
            onCheckedChange = {
                singleSelection = it
                if (it && selection.size > 1) selection = selection.take(1).toSet()
            }
        )
        OptionToggle(
            label = stringResource(R.string.component_playground_generic_enabled),
            checked = enabled,
            onCheckedChange = { enabled = it }
        )
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
        OptionDropdown(
            label = stringResource(R.string.component_playground_segmentedbuttons_variant),
            selectedLabel = segmentedButtonVariantLabel(variant),
            options = FinsibleSegmentedButtonVariant.entries,
            optionLabel = { segmentedButtonVariantLabel(it) },
            onSelect = { variant = it }
        )
        OptionDropdown(
            label = stringResource(R.string.component_playground_segmentedbuttons_arrangement),
            selectedLabel = segmentedButtonArrangementLabel(arrangement),
            options = FinsibleSegmentedButtonArrangement.entries,
            optionLabel = { segmentedButtonArrangementLabel(it) },
            onSelect = { arrangement = it }
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
    }
}

private fun FilterChipTintOption.resolveColor(colors: FinsibleSemanticColors): Color = when (this) {
    FilterChipTintOption.BrandAccent -> colors.brandInteractive
    FilterChipTintOption.Success -> colors.feedbackSuccess
    FilterChipTintOption.Info -> colors.feedbackInfo
    FilterChipTintOption.Warning -> colors.feedbackWarning
    FilterChipTintOption.Error -> colors.feedbackError
}


