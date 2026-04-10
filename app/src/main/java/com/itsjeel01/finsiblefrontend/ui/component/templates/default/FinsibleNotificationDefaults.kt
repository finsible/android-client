package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleNotificationColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleNotificationVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

object FinsibleNotificationDefaults {

    @Composable
    fun colors(
        variant: FinsibleNotificationVariant,
        containerColor: Color = Color.Unspecified,
        titleColor: Color = Color.Unspecified,
        subtitleColor: Color = Color.Unspecified,
        progressTrackColor: Color = Color.Unspecified,
    ): FinsibleNotificationColors {
        val theme = FinsibleTheme.colors

        val variantColor = when (variant) {
            FinsibleNotificationVariant.Success -> theme.success
            FinsibleNotificationVariant.Error -> theme.error
            FinsibleNotificationVariant.Warning -> theme.warning
            FinsibleNotificationVariant.Info -> theme.info
        }

        return FinsibleNotificationColors(
            containerColor = if (containerColor != Color.Unspecified) containerColor else theme.surfaceContainerLow,
            titleColor = if (titleColor != Color.Unspecified) titleColor else theme.primaryContent,
            subtitleColor = if (subtitleColor != Color.Unspecified) subtitleColor else theme.secondaryContent,
            iconContainerColor = variantColor.copy(alpha = 0.2f),
            iconTintColor = variantColor,
            progressIndicatorColor = variantColor.copy(alpha = 0.8f),
            progressTrackColor = if (progressTrackColor != Color.Unspecified) progressTrackColor else theme.border.copy(alpha = 0.4f)
        )
    }

    fun iconFor(variant: FinsibleNotificationVariant): Int {
        return when (variant) {
            FinsibleNotificationVariant.Success -> com.composables.icons.tabler.filled.R.drawable.tabler_ic_circle_check_filled
            FinsibleNotificationVariant.Error -> com.composables.icons.tabler.filled.R.drawable.tabler_ic_circle_x_filled
            FinsibleNotificationVariant.Warning -> com.composables.icons.tabler.filled.R.drawable.tabler_ic_alert_triangle_filled
            FinsibleNotificationVariant.Info -> com.composables.icons.tabler.filled.R.drawable.tabler_ic_alert_circle_filled
        }
    }
}