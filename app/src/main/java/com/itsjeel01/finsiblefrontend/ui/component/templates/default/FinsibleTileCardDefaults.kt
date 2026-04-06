package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTileCardColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTileCardSizes
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold

/** Defaults for templatised tile cards. */
object FinsibleTileCardDefaults {

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        decorativeIconTint: Color = Color.Unspecified,
        rotationIconTint: Color = Color.Unspecified,
        inverted: Boolean = false
    ): FinsibleTileCardColors {
        val colors = FinsibleTheme.colors
        val resolvedForeground = if (inverted) colors.primaryBackground else colors.primaryContent

        return FinsibleTileCardColors(
            containerColor = if (containerColor != Color.Unspecified) containerColor else colors.secondaryBackground,
            contentColor = if (contentColor != Color.Unspecified) contentColor else resolvedForeground,
            decorativeIconTint = if (decorativeIconTint != Color.Unspecified) decorativeIconTint else resolvedForeground,
            rotationIconTint = if (rotationIconTint != Color.Unspecified) rotationIconTint else resolvedForeground,
            subtitleColor = resolvedForeground.copy(alpha = 0.84f),
            dividerColor = resolvedForeground.copy(alpha = 0.22f),
            pillContainerColor = resolvedForeground.copy(alpha = 0.14f),
            pillContentColor = resolvedForeground,
            positiveKpiColor = colors.success,
            negativeKpiColor = colors.error
        )
    }

    @Composable
    fun sizes(size: FinsibleSize): FinsibleTileCardSizes {
        val d = FinsibleTheme.dimes
        val t = FinsibleTheme.typography

        return when (size) {
            FinsibleSize.ExtraSmall -> FinsibleTileCardSizes(
                cornerRadius = d.d10,
                padding = d.d12,
                statSpacing = d.d6,
                statTitleStyle = t.t12,
                statValueStyle = t.t14.semiBold(),
                titleStyle = t.t14.semiBold(),
                heroStyle = t.t20.bold(),
                iconSize = d.d16,
                subtitleStyle = t.t12,
                pillStyle = t.t12.semiBold(),
                kpiStyle = t.t12.semiBold()
            )

            FinsibleSize.Small -> FinsibleTileCardSizes(
                cornerRadius = d.d12,
                padding = d.d14,
                statSpacing = d.d8,
                statTitleStyle = t.t12,
                statValueStyle = t.t16.semiBold(),
                titleStyle = t.t16.semiBold(),
                heroStyle = t.t24.bold(),
                iconSize = d.d18,
                subtitleStyle = t.t12,
                pillStyle = t.t12.semiBold(),
                kpiStyle = t.t12.semiBold()
            )

            FinsibleSize.Medium -> FinsibleTileCardSizes(
                cornerRadius = d.d14,
                padding = d.d16,
                statSpacing = d.d10,
                statTitleStyle = t.t12,
                statValueStyle = t.t18.semiBold(),
                titleStyle = t.t18.semiBold(),
                heroStyle = t.t28.bold(),
                iconSize = d.d20,
                subtitleStyle = t.t14,
                pillStyle = t.t12.semiBold(),
                kpiStyle = t.t14.semiBold()
            )

            FinsibleSize.Large -> FinsibleTileCardSizes(
                cornerRadius = d.d16,
                padding = d.d18,
                statSpacing = d.d12,
                statTitleStyle = t.t14,
                statValueStyle = t.t20.semiBold(),
                titleStyle = t.t20.semiBold(),
                heroStyle = t.t32.bold(),
                iconSize = d.d24,
                subtitleStyle = t.t14,
                pillStyle = t.t14.semiBold(),
                kpiStyle = t.t14.semiBold()
            )

            FinsibleSize.ExtraLarge -> FinsibleTileCardSizes(
                cornerRadius = d.d18,
                padding = d.d20,
                statSpacing = d.d12,
                statTitleStyle = t.t14,
                statValueStyle = t.t24.semiBold(),
                titleStyle = t.t24.semiBold(),
                heroStyle = t.t36.bold(),
                iconSize = d.d28,
                subtitleStyle = t.t16,
                pillStyle = t.t14.semiBold(),
                kpiStyle = t.t14.semiBold()
            )
        }
    }
}


