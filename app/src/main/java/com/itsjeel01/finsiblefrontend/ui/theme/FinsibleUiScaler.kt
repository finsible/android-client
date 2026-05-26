package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class FinsibleUiScaler(private val deviceInfo: DeviceInfo) {

    private fun scaleDime(baseDimension: Dp): Dp =
        (baseDimension.value * deviceInfo.uiScaleFactor).dp

    private fun scaleText(baseStyle: TextStyle): TextStyle {
        val textScale = deviceInfo.textScaleFactor

        return baseStyle.copy(
            fontSize = baseStyle.fontSize * textScale,
            lineHeight = baseStyle.lineHeight * textScale,
            fontWeight = baseStyle.fontWeight?.let { deviceInfo.adjustFontWeight(it) }
        )
    }

    fun scaleTypes(types: FinsibleTypes): FinsibleTypes {
        return FinsibleTypes(
            displayXl = scaleText(types.displayXl),
            displayLg = scaleText(types.displayLg),
            displayMd = scaleText(types.displayMd),
            displaySm = scaleText(types.displaySm),

            headingLg = scaleText(types.headingLg),
            headingMd = scaleText(types.headingMd),
            headingSm = scaleText(types.headingSm),

            bodyLg = scaleText(types.bodyLg),
            bodyMd = scaleText(types.bodyMd),
            bodySm = scaleText(types.bodySm),

            labelLg = scaleText(types.labelLg),
            labelMd = scaleText(types.labelMd),
            labelSm = scaleText(types.labelSm),

            caption = scaleText(types.caption),

            numeralXl = scaleText(types.numeralXl),
            numeralLg = scaleText(types.numeralLg),
            numeralMd = scaleText(types.numeralMd),
            numeralSm = scaleText(types.numeralSm),
            numeralXs = scaleText(types.numeralXs),
        )
    }

    fun scaleSpacing(spacing: FinsibleSpacing): FinsibleSpacing {
        return FinsibleSpacing(
            insetNone = spacing.insetNone,  // unscaled — zero
            insetMicro = scaleDime(spacing.insetMicro),
            insetXs  = scaleDime(spacing.insetXs),
            insetSm  = scaleDime(spacing.insetSm),
            insetMd  = scaleDime(spacing.insetMd),
            insetLg  = scaleDime(spacing.insetLg),
            insetXl  = scaleDime(spacing.insetXl),
            inset2xl = scaleDime(spacing.inset2xl),
            inset3xl = scaleDime(spacing.inset3xl),

            stackMicro = scaleDime(spacing.stackMicro),
            stackXs  = scaleDime(spacing.stackXs),
            stackSm  = scaleDime(spacing.stackSm),
            stackMd  = scaleDime(spacing.stackMd),
            stackLg  = scaleDime(spacing.stackLg),
            stackXl  = scaleDime(spacing.stackXl),
            stack2xl = scaleDime(spacing.stack2xl),

            gapMicro = scaleDime(spacing.gapMicro),
            gapXs = scaleDime(spacing.gapXs),
            gapSm = scaleDime(spacing.gapSm),
            gapMd = scaleDime(spacing.gapMd),
            gapLg = scaleDime(spacing.gapLg),

            inlineXs = scaleDime(spacing.inlineXs),
            inlineSm = scaleDime(spacing.inlineSm),
            inlineMd = scaleDime(spacing.inlineMd),
        )
    }

    fun scaleStroke(stroke: FinsibleStroke): FinsibleStroke {
        return FinsibleStroke(
            hairline = stroke.hairline,  // unscaled — sub-pixel
            thin     = scaleDime(stroke.thin),
            semiBold = scaleDime(stroke.semiBold),
            bold     = scaleDime(stroke.bold),
            heavy    = scaleDime(stroke.heavy),
        )
    }

    fun scaleRadius(radius: FinsibleRadius): FinsibleRadius {
        return FinsibleRadius(
            none = 0.dp,
            xs = scaleDime(radius.xs),
            sm = scaleDime(radius.sm),
            md = scaleDime(radius.md),
            lg = scaleDime(radius.lg),
            xl = scaleDime(radius.xl),
            pill = 999.dp,  // unscaled — pill is always fully rounded
            component = FinsibleComponentRadii(
                button = scaleDime(radius.component.button),
                input = scaleDime(radius.component.input),
                card = scaleDime(radius.component.card),
                cardFinance = scaleDime(radius.component.cardFinance),
                sheet = scaleDime(radius.component.sheet),
                chip = scaleDime(radius.component.chip),
                chipPill = 999.dp,
                fab = 999.dp,
                badge = 999.dp,
                snackbar = scaleDime(radius.component.snackbar),
                dialog = scaleDime(radius.component.dialog),
            ),
        )
    }

    fun scaleCustomDime(dp: Dp): Dp = scaleDime(dp)

    fun scaleSizes(sizes: FinsibleSizeTokens): FinsibleSizeTokens {
        return FinsibleSizeTokens(
            touch = TouchSizes(
                xs = scaleDime(sizes.touch.xs),
                sm = scaleDime(sizes.touch.sm),
                md = scaleDime(sizes.touch.md),
                lg = scaleDime(sizes.touch.lg),
                xl = scaleDime(sizes.touch.xl)
            ),
            icon = IconSizes(
                xs = scaleDime(sizes.icon.xs),
                sm = scaleDime(sizes.icon.sm),
                md = scaleDime(sizes.icon.md),
                lg = scaleDime(sizes.icon.lg),
                xl = scaleDime(sizes.icon.xl)
            ),
            avatar = AvatarSizes(
                sm = scaleDime(sizes.avatar.sm),
                md = scaleDime(sizes.avatar.md),
                lg = scaleDime(sizes.avatar.lg)
            ),
            shadow = ShadowSizes(
                raised = scaleDime(sizes.shadow.raised),
                floating = scaleDime(sizes.shadow.floating),
                modal = scaleDime(sizes.shadow.modal),
                focus = scaleDime(sizes.shadow.focus)
            )
        )
    }
}
