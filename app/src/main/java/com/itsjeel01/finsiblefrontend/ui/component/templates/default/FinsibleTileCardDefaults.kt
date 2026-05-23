package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTileCardColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTileCardSizes
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleRadius
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
        val s = FinsibleTheme.colors
        val resolvedForeground = if (inverted) s.surfaceBase else s.contentPrimary

        return FinsibleTileCardColors(
            containerColor = if (containerColor != Color.Unspecified) containerColor else s.surfaceDefault,
            contentColor = if (contentColor != Color.Unspecified) contentColor else resolvedForeground,
            decorativeIconTint = if (decorativeIconTint != Color.Unspecified) decorativeIconTint else resolvedForeground,
            rotationIconTint = if (rotationIconTint != Color.Unspecified) rotationIconTint else resolvedForeground,
            subtitleColor = resolvedForeground.copy(alpha = 0.84f),
            dividerColor = resolvedForeground.copy(alpha = 0.22f),
            pillContainerColor = resolvedForeground.copy(alpha = 0.14f),
            pillContentColor = resolvedForeground,
            positiveKpiColor = s.feedbackSuccess,
            negativeKpiColor = s.feedbackError
        )
    }

    @Composable
    fun sizes(size: FinsibleSize): FinsibleTileCardSizes {
        val sp = FinsibleTheme.spacing
        val t = FinsibleTheme.typography

        return when (size) {
            FinsibleSize.ExtraSmall -> FinsibleTileCardSizes(
                cornerRadius = FinsibleTheme.radius.md,
                padding = sp.gapMd,
                statSpacing = sp.insetSm - sp.insetXs / 2,
                statTitleStyle = t.bodySm,
                statValueStyle = t.bodyMd.semiBold(),
                titleStyle = t.bodyMd.semiBold(),
                heroStyle = t.headingSm.bold(),
                iconSize = FinsibleTheme.sizes.icon.md,
                subtitleStyle = t.bodySm,
                pillStyle = t.bodySm.semiBold(),
                kpiStyle = t.bodySm.semiBold()
            )

            FinsibleSize.Small -> FinsibleTileCardSizes(
                cornerRadius = FinsibleRadius.md,
                padding = sp.insetLg - sp.insetXs / 2,
                statSpacing = sp.inlineMd,
                statTitleStyle = t.bodySm,
                statValueStyle = t.bodyLg.semiBold(),
                titleStyle = t.bodyLg.semiBold(),
                heroStyle = t.headingMd.bold(),
                iconSize = FinsibleTheme.sizes.icon.md,
                subtitleStyle = t.bodySm,
                pillStyle = t.bodySm.semiBold(),
                kpiStyle = t.bodySm.semiBold()
            )

            FinsibleSize.Medium -> FinsibleTileCardSizes(
                cornerRadius = FinsibleRadius.md,
                padding = sp.insetLg,
                statSpacing = sp.gapSm + sp.insetXs / 2,
                statTitleStyle = t.bodySm,
                statValueStyle = t.bodyLg.semiBold(),
                titleStyle = t.bodyLg.semiBold(),
                heroStyle = t.headingLg.bold(),
                iconSize = FinsibleTheme.sizes.icon.lg - sp.insetXs,
                subtitleStyle = t.bodyMd,
                pillStyle = t.bodySm.semiBold(),
                kpiStyle = t.bodyMd.semiBold()
            )

            FinsibleSize.Large -> FinsibleTileCardSizes(
                cornerRadius = FinsibleTheme.radius.lg,
                padding = sp.insetLg + sp.insetXs / 2,
                statSpacing = sp.gapMd,
                statTitleStyle = t.bodyMd,
                statValueStyle = t.headingSm.semiBold(),
                titleStyle = t.headingSm.semiBold(),
                heroStyle = t.displaySm.bold(),
                iconSize = FinsibleTheme.sizes.icon.lg,
                subtitleStyle = t.bodyMd,
                pillStyle = t.bodyMd.semiBold(),
                kpiStyle = t.bodyMd.semiBold()
            )

            FinsibleSize.ExtraLarge -> FinsibleTileCardSizes(
                cornerRadius = FinsibleRadius.lg,
                padding = sp.insetXl,
                statSpacing = sp.gapMd,
                statTitleStyle = t.bodyMd,
                statValueStyle = t.headingMd.semiBold(),
                titleStyle = t.headingMd.semiBold(),
                heroStyle = t.displayMd.bold(),
                iconSize = FinsibleTheme.sizes.icon.xl - sp.insetXs,
                subtitleStyle = t.bodyLg,
                pillStyle = t.bodyMd.semiBold(),
                kpiStyle = t.bodyMd.semiBold()
            )
        }
    }
}
