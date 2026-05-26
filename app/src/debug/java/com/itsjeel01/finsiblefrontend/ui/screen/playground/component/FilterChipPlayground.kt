
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
import com.composables.icons.lucide.R as LucideR
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleFilterChip
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleIconPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleFilterChipVariant
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.*
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun FilterChipPlayground() {
    var selected by rememberSaveable { mutableStateOf(true) }
    var enabled by rememberSaveable { mutableStateOf(true) }
    var size by rememberSaveable { mutableStateOf(FinsibleSize.Medium) }
    var shape by rememberSaveable { mutableStateOf(FinsibleShape.Pill) }
    var variant by rememberSaveable { mutableStateOf(FinsibleFilterChipVariant.Tonal) }
    var withIcon by rememberSaveable { mutableStateOf(true) }
    var iconPosition by rememberSaveable { mutableStateOf(FinsibleIconPosition.Leading) }
    var useSelectedTint by rememberSaveable { mutableStateOf(false) }
    var inverted by rememberSaveable { mutableStateOf(false) }
    var tintOption by rememberSaveable { mutableStateOf(FilterChipTintOption.BrandAccent) }
    var tintAlpha by rememberSaveable { mutableFloatStateOf(1f) }
    val allowedSizes = listOf(FinsibleSize.Small, FinsibleSize.Medium, FinsibleSize.Large)
    val allowedShapes = listOf(FinsibleShape.Pill, FinsibleShape.Rounded, FinsibleShape.Sharp)
    val baseTint = tintOption.resolveColor(FinsibleTheme.colors)
    val resolvedTint = baseTint.copy(alpha = tintAlpha)

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = FinsibleTheme.spacing.insetLg, vertical = FinsibleTheme.spacing.gapMd),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackMd)
    ) {
        FinsibleFilterChip(
            selected = selected,
            onSelectedChange = { selected = it },
            label = stringResource(R.string.component_playground_filterchip_label),
            enabled = enabled,
            size = size,
            shapeVariant = shape,
            variant = variant,
            icon = if (withIcon) {
                { Icon(painterResource(LucideR.drawable.lucide_ic_sparkles), contentDescription = null) }
            } else null,
            iconPosition = iconPosition,
            selectedTint = if (useSelectedTint) resolvedTint else Color.Unspecified,
            inverted = inverted
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_generic_enabled),
            checked = enabled,
            onCheckedChange = { enabled = it }
        )
        OptionToggle(
            label = stringResource(R.string.component_playground_generic_selected),
            checked = selected,
            onCheckedChange = { selected = it }
        )
        OptionDropdown(
            label = stringResource(R.string.component_playground_generic_size),
            selectedLabel = sizeLabel(size),
            options = allowedSizes,
            optionLabel = { sizeLabel(it) },
            onSelect = { size = it }
        )
        OptionDropdown(
            label = stringResource(R.string.component_playground_generic_shape),
            selectedLabel = shapeLabel(shape),
            options = allowedShapes,
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
            label = stringResource(R.string.component_playground_filterchip_icon),
            checked = withIcon,
            onCheckedChange = { withIcon = it }
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

        OptionDropdown(
            label = stringResource(R.string.component_playground_filterchip_icon_position),
            selectedLabel = iconPositionLabel(iconPosition),
            options = FinsibleIconPosition.entries,
            optionLabel = { iconPositionLabel(it) },
            onSelect = { iconPosition = it },
            enabled = withIcon
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


