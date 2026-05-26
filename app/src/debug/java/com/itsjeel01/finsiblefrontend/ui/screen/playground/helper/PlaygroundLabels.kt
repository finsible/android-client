package com.itsjeel01.finsiblefrontend.ui.screen.playground.helper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleIconPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleBadgeType
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleToggleArrangement
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleToggleLabelPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleCheckboxVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleFilterChipVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleSegmentedButtonArrangement
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleSegmentedButtonVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant

enum class FilterChipTintOption {
    BrandAccent,
    Success,
    Info,
    Warning,
    Error
}

@Composable
fun sizeLabel(size: FinsibleSize): String = when (size) {
    FinsibleSize.ExtraSmall -> stringResource(R.string.finsible_size_extra_small)
    FinsibleSize.Small -> stringResource(R.string.finsible_size_small)
    FinsibleSize.Medium -> stringResource(R.string.finsible_size_medium)
    FinsibleSize.Large -> stringResource(R.string.finsible_size_large)
    FinsibleSize.ExtraLarge -> stringResource(R.string.finsible_size_extra_large)
}

@Composable
fun shapeLabel(shape: FinsibleShape): String = when (shape) {
    FinsibleShape.Circle -> stringResource(R.string.finsible_shape_circle)
    FinsibleShape.Pill -> stringResource(R.string.finsible_shape_pill)
    FinsibleShape.Rounded -> stringResource(R.string.finsible_shape_rounded)
    FinsibleShape.Sharp -> stringResource(R.string.finsible_shape_sharp)
}

@Composable
fun iconPositionLabel(iconPosition: FinsibleIconPosition): String = when (iconPosition) {
    FinsibleIconPosition.Leading -> stringResource(R.string.finsible_icon_position_leading)
    FinsibleIconPosition.Trailing -> stringResource(R.string.finsible_icon_position_trailing)
}

@Composable
fun filterChipTintOptionLabel(option: FilterChipTintOption): String = when (option) {
    FilterChipTintOption.BrandAccent -> stringResource(R.string.component_playground_filterchip_tint_brand)
    FilterChipTintOption.Success -> stringResource(R.string.component_playground_filterchip_tint_success)
    FilterChipTintOption.Info -> stringResource(R.string.component_playground_filterchip_tint_info)
    FilterChipTintOption.Warning -> stringResource(R.string.component_playground_filterchip_tint_warning)
    FilterChipTintOption.Error -> stringResource(R.string.component_playground_filterchip_tint_error)
}

@Composable
fun filterChipVariantLabel(variant: FinsibleFilterChipVariant): String = when (variant) {
    FinsibleFilterChipVariant.Filled -> stringResource(R.string.finsible_filter_chip_variant_filled)
    FinsibleFilterChipVariant.Tonal -> stringResource(R.string.finsible_filter_chip_variant_tonal)
    FinsibleFilterChipVariant.Outlined -> stringResource(R.string.finsible_filter_chip_variant_outlined)
    FinsibleFilterChipVariant.OutlinedTonal -> stringResource(R.string.finsible_filter_chip_variant_outlined_tonal)
}

@Composable
fun segmentedButtonVariantLabel(variant: FinsibleSegmentedButtonVariant): String = when (variant) {
    FinsibleSegmentedButtonVariant.Filled -> stringResource(R.string.finsible_segmented_button_variant_filled)
    FinsibleSegmentedButtonVariant.Tonal -> stringResource(R.string.finsible_segmented_button_variant_tonal)
}

@Composable
fun segmentedButtonArrangementLabel(arrangement: FinsibleSegmentedButtonArrangement): String =
    when (arrangement) {
        FinsibleSegmentedButtonArrangement.Clubbed -> stringResource(R.string.finsible_segmented_button_arrangement_clubbed)
        FinsibleSegmentedButtonArrangement.Separated -> stringResource(R.string.finsible_segmented_button_arrangement_separated)
    }

@Composable
fun badgeTypeLabel(badgeType: FinsibleBadgeType): String = when (badgeType) {
    FinsibleBadgeType.None -> stringResource(R.string.finsible_badge_none)
    FinsibleBadgeType.Dot -> stringResource(R.string.finsible_badge_dot)
    FinsibleBadgeType.Count -> stringResource(R.string.finsible_badge_count)
}

@Composable
fun checkboxVariantLabel(variant: FinsibleCheckboxVariant): String = when (variant) {
    FinsibleCheckboxVariant.Monochrome -> stringResource(R.string.finsible_checkbox_variant_monochrome)
    FinsibleCheckboxVariant.Colorful -> stringResource(R.string.finsible_checkbox_variant_colorful)
}

@Composable
fun buttonVariantLabel(variant: FinsibleButtonVariant): String = when (variant) {
    FinsibleButtonVariant.Filled -> stringResource(R.string.finsible_button_variant_filled)
    FinsibleButtonVariant.FilledTonal -> stringResource(R.string.finsible_button_variant_filled_tonal)
    FinsibleButtonVariant.Outlined -> stringResource(R.string.finsible_button_variant_outlined)
    FinsibleButtonVariant.Text -> stringResource(R.string.finsible_button_variant_text)
    FinsibleButtonVariant.Link -> stringResource(R.string.finsible_button_variant_link)
}

@Composable
fun textColorVariantLabel(variant: FinsibleTextColorVariant): String = when (variant) {
    FinsibleTextColorVariant.Primary -> stringResource(R.string.component_playground_text_color_primary)
    FinsibleTextColorVariant.Secondary -> stringResource(R.string.component_playground_text_color_secondary)
    FinsibleTextColorVariant.Tertiary -> stringResource(R.string.component_playground_text_color_tertiary)
    FinsibleTextColorVariant.Accent -> stringResource(R.string.component_playground_text_color_accent)
    FinsibleTextColorVariant.Link -> stringResource(R.string.component_playground_text_color_link)
    FinsibleTextColorVariant.Error -> stringResource(R.string.component_playground_text_color_error)
}


@Composable
fun toggleLabelPositionLabel(position: FinsibleToggleLabelPosition): String = when (position) {
    FinsibleToggleLabelPosition.Leading -> stringResource(R.string.component_playground_toggle_label_position_leading)
    FinsibleToggleLabelPosition.Trailing -> stringResource(R.string.component_playground_toggle_label_position_trailing)
}

@Composable
fun toggleArrangementLabel(arrangement: FinsibleToggleArrangement): String = when (arrangement) {
    FinsibleToggleArrangement.Attached -> stringResource(R.string.component_playground_toggle_arrangement_attached)
    FinsibleToggleArrangement.SpaceBetween -> stringResource(R.string.component_playground_toggle_arrangement_space_between)
}

