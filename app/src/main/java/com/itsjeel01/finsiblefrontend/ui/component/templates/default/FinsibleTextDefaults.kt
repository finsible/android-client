package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTextStyleSpec
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.itsjeel01.finsiblefrontend.ui.theme.displayFont
import com.itsjeel01.finsiblefrontend.ui.theme.extraBold
import com.itsjeel01.finsiblefrontend.ui.theme.interfaceFont
import com.itsjeel01.finsiblefrontend.ui.theme.medium
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold

/** Defaults for templatised text component. */
object FinsibleTextDefaults {

    @Composable
    fun style(
        variant: FinsibleTextVariant,
        color: Color = Color.Unspecified,
        colorVariant: FinsibleTextColorVariant = FinsibleTextColorVariant.Primary,
        textStyleOverride: TextStyle? = null,
        uppercase: Boolean = false,
        underline: Boolean = false,
        strikethrough: Boolean = false,
        isDisplayFont: Boolean? = null
    ): FinsibleTextStyleSpec {
        val type = FinsibleTheme.typography
        val themeColor = FinsibleTheme.colors

        val baseStyle = when (variant) {
            FinsibleTextVariant.XLargeHeadingBold -> type.t56.bold()
            FinsibleTextVariant.LargeHeadingBold -> type.t48.bold()
            FinsibleTextVariant.MediumHeadingBold -> type.t40.bold()
            FinsibleTextVariant.SmallHeadingBold -> type.t32.bold()
            FinsibleTextVariant.XLargeTitleNormal -> type.t28
            FinsibleTextVariant.LargeTitleNormal -> type.t24
            FinsibleTextVariant.LargeTitleMedium -> type.t24.medium()
            FinsibleTextVariant.LargeTitleSemiBold -> type.t24.semiBold()
            FinsibleTextVariant.LargeTitleExtraBold -> type.t24.extraBold()
            FinsibleTextVariant.MediumTitleNormal -> type.t20
            FinsibleTextVariant.MediumTitleMedium -> type.t20.medium()
            FinsibleTextVariant.MediumTitleSemiBold -> type.t20.semiBold()
            FinsibleTextVariant.MediumTitleBold -> type.t20.bold()
            FinsibleTextVariant.SmallTitleNormal -> type.t18
            FinsibleTextVariant.SmallTitleBold -> type.t18.bold()
            FinsibleTextVariant.SmallTitleMedium -> type.t18.medium()
            FinsibleTextVariant.SmallTitleExtraBold -> type.t18.extraBold()
            FinsibleTextVariant.XSmallTitleNormal -> type.t16
            FinsibleTextVariant.BodyRegular -> type.t16
            FinsibleTextVariant.BodyMedium -> type.t16.medium()
            FinsibleTextVariant.BodySemiBold -> type.t16.semiBold()
            FinsibleTextVariant.BodyBold -> type.t16.bold()
            FinsibleTextVariant.SmallBodyRegular -> type.t14
            FinsibleTextVariant.SmallBodyMedium -> type.t14.medium()
            FinsibleTextVariant.SmallBodySemiBold -> type.t14.semiBold()
            FinsibleTextVariant.SmallBodyBold -> type.t14.bold()
            FinsibleTextVariant.XLargeLabelSemiBold -> type.t16.semiBold()
            FinsibleTextVariant.LargeLabelSemiBold -> type.t14.semiBold()
            FinsibleTextVariant.SmallLabelRegular -> type.t12
            FinsibleTextVariant.SmallLabelMedium -> type.t12.medium()
            FinsibleTextVariant.SmallLabelSemiBold -> type.t12.semiBold()
            FinsibleTextVariant.MicroLabelMedium -> type.t10.medium()
            FinsibleTextVariant.MicroLabelSemiBold -> type.t10.semiBold()
        }

        val resolvedColor = if (color != Color.Unspecified) color else when (colorVariant) {
            FinsibleTextColorVariant.Primary -> themeColor.primaryContent
            FinsibleTextColorVariant.Secondary -> themeColor.secondaryContent
            FinsibleTextColorVariant.Accent -> themeColor.brandAccent
            FinsibleTextColorVariant.Link -> themeColor.link
            FinsibleTextColorVariant.Error -> themeColor.error
        }

        val decoration = when {
            underline && strikethrough -> TextDecoration.combine(listOf(TextDecoration.Underline, TextDecoration.LineThrough))
            underline -> TextDecoration.Underline
            strikethrough -> TextDecoration.LineThrough
            else -> TextDecoration.None
        }

        val mergedStyle = textStyleOverride?.let { baseStyle.merge(it) } ?: baseStyle
        var resolvedStyle = mergedStyle.copy(textDecoration = decoration)

        if (isDisplayFont == true) {
            resolvedStyle = resolvedStyle.displayFont()
        } else if (isDisplayFont == false) {
            resolvedStyle = resolvedStyle.interfaceFont()
        }

        return FinsibleTextStyleSpec(
            textStyle = resolvedStyle,
            color = resolvedColor,
            uppercase = uppercase,
        )
    }
}
