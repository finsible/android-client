package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDropdownColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDropdownSizes
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleRadius
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium

/** Defaults for templatised dropdowns. */
object FinsibleDropdownDefaults {

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        menuColor: Color = Color.Unspecified,
        borderColor: Color = Color.Unspecified,
        selectedOptionColor: Color = Color.Unspecified,
        selectedOptionTextColor: Color = Color.Unspecified,
        selectedIconTint: Color = Color.Unspecified,
        optionTextColor: Color = Color.Unspecified,
        placeholderColor: Color = Color.Unspecified,
        iconTint: Color = Color.Unspecified,
        disabledIconTint: Color = Color.Unspecified,
        rippleColor: Color = Color.Unspecified
    ): FinsibleDropdownColors {
        val s = FinsibleTheme.colors

        return FinsibleDropdownColors(
            containerColor = if (containerColor != Color.Unspecified) containerColor else s.inputSurface,
            menuColor = if (menuColor != Color.Unspecified) menuColor else s.surfaceBase,
            borderColor = if (borderColor != Color.Unspecified) borderColor else s.borderDefault,
            selectedOptionColor = if (selectedOptionColor != Color.Unspecified) selectedOptionColor else s.surfaceDefault,
            selectedOptionTextColor = if (selectedOptionTextColor != Color.Unspecified) selectedOptionTextColor else s.contentPrimary,
            selectedIconTint = if (selectedIconTint != Color.Unspecified) selectedIconTint else s.contentPrimary,
            optionTextColor = if (optionTextColor != Color.Unspecified) optionTextColor else s.contentPrimary.copy(alpha = 0.8f),
            placeholderColor = if (placeholderColor != Color.Unspecified) placeholderColor else s.contentSecondary,
            iconTint = if (iconTint != Color.Unspecified) iconTint else s.contentPrimary,
            disabledIconTint = if (disabledIconTint != Color.Unspecified) disabledIconTint else s.contentDisabled,
            rippleColor = if (rippleColor != Color.Unspecified) rippleColor else s.contentPrimary.copy(alpha = 0.12f)
        )
    }

    @Composable
    fun sizes(size: FinsibleSize, shapeVariant: FinsibleShape = FinsibleShape.Rounded): FinsibleDropdownSizes {
        val sp = FinsibleTheme.spacing
        val t = FinsibleTheme.typography

        val base = when (size) {
            FinsibleSize.ExtraSmall -> FinsibleDropdownSizes(
                horizontalPadding = sp.inlineMd,
                verticalPadding = sp.inlineMd,
                textStyle = t.bodyMd,
                placeholderStyle = t.bodyMd,
                iconSize = FinsibleTheme.sizes.icon.md,
                iconSpacing = sp.inlineMd,
                cornerRadius = FinsibleTheme.radius.sm,
                itemPadding = sp.inlineMd,
                borderWidth = FinsibleTheme.stroke.thin
            )

            FinsibleSize.Small -> FinsibleDropdownSizes(
                horizontalPadding = sp.gapSm + sp.insetXs / 2,
                verticalPadding = sp.gapSm + sp.insetXs / 2,
                textStyle = t.bodyLg,
                placeholderStyle = t.bodyLg,
                iconSize = FinsibleTheme.sizes.icon.md,
                iconSpacing = sp.inlineMd,
                cornerRadius = FinsibleRadius.sm,
                itemPadding = sp.gapSm + sp.insetXs / 2,
                borderWidth = FinsibleTheme.stroke.thin
            )

            FinsibleSize.Medium -> FinsibleDropdownSizes(
                horizontalPadding = sp.gapMd,
                verticalPadding = sp.gapMd,
                textStyle = t.bodyLg.medium(),
                placeholderStyle = t.bodyLg,
                iconSize = FinsibleTheme.sizes.icon.lg - sp.insetXs,
                iconSpacing = sp.inlineMd,
                cornerRadius = FinsibleTheme.radius.md,
                itemPadding = sp.gapMd,
                borderWidth = FinsibleTheme.stroke.thin
            )

            FinsibleSize.Large -> FinsibleDropdownSizes(
                horizontalPadding = sp.insetLg - sp.insetXs / 2,
                verticalPadding = sp.insetLg - sp.insetXs / 2,
                textStyle = t.headingSm,
                placeholderStyle = t.headingSm,
                iconSize = FinsibleTheme.sizes.icon.lg,
                iconSpacing = sp.inlineMd,
                cornerRadius = FinsibleRadius.md,
                itemPadding = sp.insetLg - sp.insetXs / 2,
                borderWidth = FinsibleTheme.stroke.thin
            )

            FinsibleSize.ExtraLarge -> FinsibleDropdownSizes(
                horizontalPadding = sp.insetLg,
                verticalPadding = sp.insetLg,
                textStyle = t.headingMd,
                placeholderStyle = t.headingMd,
                iconSize = FinsibleTheme.sizes.icon.xl - sp.insetXs,
                iconSpacing = sp.inlineMd,
                cornerRadius = FinsibleTheme.radius.lg,
                itemPadding = sp.insetLg,
                borderWidth = FinsibleTheme.stroke.thin
            )
        }

        val cornerRadius = when (shapeVariant) {
            FinsibleShape.Sharp -> FinsibleTheme.radius.none
            FinsibleShape.Rounded -> base.cornerRadius
            FinsibleShape.Pill,
            FinsibleShape.Circle -> (base.iconSize / 2) + base.verticalPadding
        }

        return base.copy(cornerRadius = cornerRadius)
    }
}
