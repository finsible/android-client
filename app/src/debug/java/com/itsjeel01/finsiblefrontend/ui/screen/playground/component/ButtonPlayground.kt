package com.itsjeel01.finsiblefrontend.ui.screen.playground.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleIconPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleBadgeType
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.*
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.composables.icons.lucide.R as LucideR

@Composable
fun ButtonPlayground() {
    var variant by rememberSaveable { mutableStateOf(FinsibleButtonVariant.Filled) }
    var size by rememberSaveable { mutableStateOf(FinsibleSize.Medium) }
    var shape by rememberSaveable { mutableStateOf(FinsibleShape.Pill) }
    var iconEnabled by rememberSaveable { mutableStateOf(true) }
    var iconOnly by rememberSaveable { mutableStateOf(false) }
    var fullWidth by rememberSaveable { mutableStateOf(false) }
    var iconPosition by rememberSaveable { mutableStateOf(FinsibleIconPosition.Leading) }
    var badgeType by rememberSaveable { mutableStateOf(FinsibleBadgeType.None) }
    var badgeCount by rememberSaveable { mutableStateOf(3) }
    var loading by rememberSaveable { mutableStateOf(false) }
    var enabled by rememberSaveable { mutableStateOf(true) }

    val hasIcon by rememberUpdatedState(iconEnabled)
    val effectiveIconOnly = iconOnly && hasIcon
    val effectiveFullWidth = fullWidth && !effectiveIconOnly
    val variantSupportsBadges = variant != FinsibleButtonVariant.Text && variant != FinsibleButtonVariant.Link
    val badgeSupported = effectiveIconOnly && variantSupportsBadges
    val badgeOptions = when {
        !badgeSupported -> listOf(FinsibleBadgeType.None)
        size == FinsibleSize.ExtraSmall -> FinsibleBadgeType.entries.filter { it != FinsibleBadgeType.Count }
        else -> FinsibleBadgeType.entries
    }
    val resolvedBadgeType = when {
        !badgeSupported -> FinsibleBadgeType.None
        size == FinsibleSize.ExtraSmall && badgeType == FinsibleBadgeType.Count -> FinsibleBadgeType.Dot
        else -> badgeType
    }
    val badgeCountAllowed = resolvedBadgeType == FinsibleBadgeType.Count && size != FinsibleSize.ExtraSmall

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = FinsibleTheme.dimes.d16, vertical = FinsibleTheme.dimes.d12),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12)
    ) {
        FinsibleButton(
            onClick = {},
            variant = variant,
            size = size,
            shapeVariant = shape,
            iconOnly = effectiveIconOnly,
            fullWidth = effectiveFullWidth,
            badgeType = resolvedBadgeType,
            badgeCount = badgeCount,
            loading = loading,
            enabled = enabled,
            icon = if (hasIcon) {
                { Icon(painterResource(LucideR.drawable.lucide_ic_chevron_right), contentDescription = null) }
            } else null,
            iconPosition = iconPosition,
            text = if (effectiveIconOnly) null else stringResource(R.string.component_playground_button_label)
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_button_option_variant),
            selectedLabel = buttonVariantLabel(variant),
            options = FinsibleButtonVariant.entries,
            optionLabel = { buttonVariantLabel(it) },
            onSelect = { variant = it }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_button_option_size),
            selectedLabel = sizeLabel(size),
            options = FinsibleSize.entries,
            optionLabel = { sizeLabel(it) },
            onSelect = { size = it }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_button_option_shape),
            selectedLabel = shapeLabel(shape),
            options = listOf(FinsibleShape.Pill, FinsibleShape.Rounded, FinsibleShape.Sharp),
            optionLabel = { shapeLabel(it) },
            onSelect = { shape = it }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_button_option_icon),
            checked = hasIcon,
            onCheckedChange = { checked ->
                iconEnabled = checked
                if (!checked) iconOnly = false
            }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_button_option_icon_only),
            checked = effectiveIconOnly,
            enabled = hasIcon,
            helperText = if (!hasIcon) stringResource(R.string.component_playground_button_hint_icon_only) else null,
            onCheckedChange = { checked ->
                iconOnly = checked
                if (checked) fullWidth = false
            }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_button_option_full_width),
            checked = effectiveFullWidth,
            enabled = !effectiveIconOnly,
            helperText = if (effectiveIconOnly) stringResource(R.string.component_playground_button_hint_full_width) else null,
            onCheckedChange = { fullWidth = it }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_button_option_icon_position),
            selectedLabel = iconPositionLabel(iconPosition),
            options = FinsibleIconPosition.entries,
            optionLabel = { iconPositionLabel(it) },
            enabled = hasIcon,
            onSelect = { iconPosition = it }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_button_option_badge),
            selectedLabel = badgeTypeLabel(resolvedBadgeType),
            options = badgeOptions,
            optionLabel = { badgeTypeLabel(it) },
            enabled = badgeSupported,
            helperText = when {
                !variantSupportsBadges -> stringResource(R.string.component_playground_button_hint_variant_block)
                !effectiveIconOnly -> stringResource(R.string.component_playground_button_hint_badge_support)
                else -> null
            },
            onSelect = { badgeType = it }
        )

        if (resolvedBadgeType == FinsibleBadgeType.Count) {
            OptionSlider(
                label = stringResource(R.string.component_playground_button_option_badge_count),
                value = badgeCount.toFloat(),
                valueRange = 0f .. 50f,
                steps = 50,
                enabled = badgeCountAllowed,
                helperText = if (!badgeCountAllowed) stringResource(R.string.component_playground_button_hint_badge_size) else null,
                onValueChange = { badgeCount = it.toInt() }
            )
        }

        OptionToggle(
            label = stringResource(R.string.component_playground_button_option_loading),
            checked = loading,
            onCheckedChange = { loading = it; if (it) enabled = true }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_button_option_enabled),
            checked = enabled,
            onCheckedChange = { enabled = it; if (!it) loading = false }
        )
    }
}