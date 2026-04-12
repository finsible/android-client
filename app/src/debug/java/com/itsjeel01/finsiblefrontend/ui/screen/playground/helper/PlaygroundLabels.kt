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
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleSegmentedButtonVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant

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
fun textVariantLabel(variant: FinsibleTextVariant): String = when (variant) {
    FinsibleTextVariant.XLargeHeadingBold -> stringResource(R.string.component_playground_text_variant_xlarge_heading_bold)
    FinsibleTextVariant.LargeHeadingBold -> stringResource(R.string.component_playground_text_variant_large_heading_bold)
    FinsibleTextVariant.MediumHeadingBold -> stringResource(R.string.component_playground_text_variant_medium_heading_bold)
    FinsibleTextVariant.SmallHeadingBold -> stringResource(R.string.component_playground_text_variant_small_heading_bold)
    FinsibleTextVariant.XLargeTitleNormal -> stringResource(R.string.component_playground_text_variant_xlarge_title_normal)
    FinsibleTextVariant.LargeTitleNormal -> stringResource(R.string.component_playground_text_variant_large_title_normal)
    FinsibleTextVariant.LargeTitleMedium -> stringResource(R.string.component_playground_text_variant_large_title_medium)
    FinsibleTextVariant.LargeTitleSemiBold -> stringResource(R.string.component_playground_text_variant_large_title_semibold)
    FinsibleTextVariant.LargeTitleExtraBold -> stringResource(R.string.component_playground_text_variant_large_title_extrabold)
    FinsibleTextVariant.MediumTitleNormal -> stringResource(R.string.component_playground_text_variant_medium_title_normal)
    FinsibleTextVariant.MediumTitleMedium -> stringResource(R.string.component_playground_text_variant_medium_title_medium)
    FinsibleTextVariant.MediumTitleSemiBold -> stringResource(R.string.component_playground_text_variant_medium_title_semibold)
    FinsibleTextVariant.MediumTitleBold -> stringResource(R.string.component_playground_text_variant_medium_title_bold)
    FinsibleTextVariant.SmallTitleNormal -> stringResource(R.string.component_playground_text_variant_small_title_normal)
    FinsibleTextVariant.SmallTitleBold -> stringResource(R.string.component_playground_text_variant_small_title_bold)
    FinsibleTextVariant.SmallTitleMedium -> stringResource(R.string.component_playground_text_variant_small_title_medium)
    FinsibleTextVariant.SmallTitleExtraBold -> stringResource(R.string.component_playground_text_variant_small_title_extrabold)
    FinsibleTextVariant.XSmallTitleNormal -> stringResource(R.string.component_playground_text_variant_xsmall_title_normal)
    FinsibleTextVariant.BodyRegular -> stringResource(R.string.component_playground_text_variant_body_regular)
    FinsibleTextVariant.BodyMedium -> stringResource(R.string.component_playground_text_variant_body_medium)
    FinsibleTextVariant.BodySemiBold -> stringResource(R.string.component_playground_text_variant_body_semibold)
    FinsibleTextVariant.BodyBold -> stringResource(R.string.component_playground_text_variant_body_bold)
    FinsibleTextVariant.SmallBodyRegular -> stringResource(R.string.component_playground_text_variant_small_body_regular)
    FinsibleTextVariant.SmallBodyMedium -> stringResource(R.string.component_playground_text_variant_small_body_medium)
    FinsibleTextVariant.SmallBodySemiBold -> stringResource(R.string.component_playground_text_variant_small_body_semibold)
    FinsibleTextVariant.SmallBodyBold -> stringResource(R.string.component_playground_text_variant_small_body_bold)
    FinsibleTextVariant.XLargeLabelSemiBold -> stringResource(R.string.component_playground_text_variant_xlarge_label_semibold)
    FinsibleTextVariant.LargeLabelSemiBold -> stringResource(R.string.component_playground_text_variant_large_label_semibold)
    FinsibleTextVariant.SmallLabelRegular -> stringResource(R.string.component_playground_text_variant_small_label_regular)
    FinsibleTextVariant.SmallLabelMedium -> stringResource(R.string.component_playground_text_variant_small_label_medium)
    FinsibleTextVariant.SmallLabelSemiBold -> stringResource(R.string.component_playground_text_variant_small_label_semibold)
    FinsibleTextVariant.MicroLabelMedium -> stringResource(R.string.component_playground_text_variant_micro_label_medium)
    FinsibleTextVariant.MicroLabelSemiBold -> stringResource(R.string.component_playground_text_variant_micro_label_semibold)
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

