package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDropdownColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDropdownSizes
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
        val colors = FinsibleTheme.colors

        return FinsibleDropdownColors(
            containerColor = if (containerColor != Color.Unspecified) containerColor else colors.input,
            menuColor = if (menuColor != Color.Unspecified) menuColor else colors.primaryBackground,
            borderColor = if (borderColor != Color.Unspecified) borderColor else colors.border,
            selectedOptionColor = if (selectedOptionColor != Color.Unspecified) selectedOptionColor else colors.surfaceContainerLow,
            selectedOptionTextColor = if (selectedOptionTextColor != Color.Unspecified) selectedOptionTextColor else colors.primaryContent,
            selectedIconTint = if (selectedIconTint != Color.Unspecified) selectedIconTint else colors.primaryContent,
            optionTextColor = if (optionTextColor != Color.Unspecified) optionTextColor else colors.primaryContent80,
            placeholderColor = if (placeholderColor != Color.Unspecified) placeholderColor else colors.secondaryContent,
            iconTint = if (iconTint != Color.Unspecified) iconTint else colors.primaryContent,
            disabledIconTint = if (disabledIconTint != Color.Unspecified) disabledIconTint else colors.disabledContent,
            rippleColor = if (rippleColor != Color.Unspecified) rippleColor else colors.primaryContent.copy(alpha = 0.12f)
        )
    }

    @Composable
    fun sizes(size: FinsibleSize, shapeVariant: FinsibleShape = FinsibleShape.Rounded): FinsibleDropdownSizes {
        val d = FinsibleTheme.dimes
        val t = FinsibleTheme.typography

        val base = when (size) {
            FinsibleSize.ExtraSmall -> FinsibleDropdownSizes(
                horizontalPadding = d.d8,
                verticalPadding = d.d6,
                textStyle = t.t14,
                placeholderStyle = t.t14,
                iconSize = d.d14,
                iconSpacing = d.d10,
                cornerRadius = d.d8,
                itemPadding = d.d8,
                borderWidth = d.d1
            )

            FinsibleSize.Small -> FinsibleDropdownSizes(
                horizontalPadding = d.d10,
                verticalPadding = d.d8,
                textStyle = t.t16,
                placeholderStyle = t.t16,
                iconSize = d.d16,
                iconSpacing = d.d10,
                cornerRadius = d.d10,
                itemPadding = d.d10,
                borderWidth = d.d1
            )

            FinsibleSize.Medium -> FinsibleDropdownSizes(
                horizontalPadding = d.d12,
                verticalPadding = d.d10,
                textStyle = t.t18.medium(),
                placeholderStyle = t.t18,
                iconSize = d.d18,
                iconSpacing = d.d10,
                cornerRadius = d.d12,
                itemPadding = d.d12,
                borderWidth = d.d1
            )

            FinsibleSize.Large -> FinsibleDropdownSizes(
                horizontalPadding = d.d14,
                verticalPadding = d.d12,
                textStyle = t.t20.medium(),
                placeholderStyle = t.t20,
                iconSize = d.d20,
                iconSpacing = d.d10,
                cornerRadius = d.d14,
                itemPadding = d.d14,
                borderWidth = d.d1
            )

            FinsibleSize.ExtraLarge -> FinsibleDropdownSizes(
                horizontalPadding = d.d16,
                verticalPadding = d.d14,
                textStyle = t.t24.medium(),
                placeholderStyle = t.t24,
                iconSize = d.d24,
                iconSpacing = d.d10,
                cornerRadius = d.d16,
                itemPadding = d.d16,
                borderWidth = d.d1
            )
        }

        val cornerRadius = when (shapeVariant) {
            FinsibleShape.Sharp -> FinsibleTheme.dimes.d0
            FinsibleShape.Rounded -> base.cornerRadius
            FinsibleShape.Pill,
            FinsibleShape.Circle -> (base.iconSize / 2) + base.verticalPadding
        }

        return base.copy(cornerRadius = cornerRadius)
    }
}
