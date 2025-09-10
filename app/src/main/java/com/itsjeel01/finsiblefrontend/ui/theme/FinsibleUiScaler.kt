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

    fun scaleDimes(dimes: FinsibleDimes): FinsibleDimes {
        return FinsibleDimes(
            d0 = dimes.d0,
            d1 = dimes.d1,
            d2 = dimes.d2,
            d3 = dimes.d3,
            d4 = dimes.d4,
            d5 = dimes.d5,
            d6 = scaleDime(dimes.d6),
            d7 = scaleDime(dimes.d7),
            d8 = scaleDime(dimes.d8),
            d9 = scaleDime(dimes.d9),
            d10 = scaleDime(dimes.d10),
            d12 = scaleDime(dimes.d12),
            d14 = scaleDime(dimes.d14),
            d16 = scaleDime(dimes.d16),
            d18 = scaleDime(dimes.d18),
            d20 = scaleDime(dimes.d20),
            d22 = scaleDime(dimes.d22),
            d24 = scaleDime(dimes.d24),
            d26 = scaleDime(dimes.d26),
            d28 = scaleDime(dimes.d28),
            d30 = scaleDime(dimes.d30),
            d32 = scaleDime(dimes.d32),
            d36 = scaleDime(dimes.d36),
            d40 = scaleDime(dimes.d40),
            d44 = scaleDime(dimes.d44),
            d48 = scaleDime(dimes.d48),
            d52 = scaleDime(dimes.d52),
            d56 = scaleDime(dimes.d56),
            d60 = scaleDime(dimes.d60),
            d64 = scaleDime(dimes.d64),
            d68 = scaleDime(dimes.d68),
            d72 = scaleDime(dimes.d72),
            d80 = scaleDime(dimes.d80),
            d88 = scaleDime(dimes.d88),
            d96 = scaleDime(dimes.d96),
            d100 = scaleDime(dimes.d100),
            d112 = scaleDime(dimes.d112),
            d124 = scaleDime(dimes.d124),
            d136 = scaleDime(dimes.d136),
            d148 = scaleDime(dimes.d148),
            d160 = scaleDime(dimes.d160),
            d180 = scaleDime(dimes.d180),
            d200 = scaleDime(dimes.d200),
            d220 = scaleDime(dimes.d220),
            d240 = scaleDime(dimes.d240),
            d260 = scaleDime(dimes.d260),
            d280 = scaleDime(dimes.d280),
            d300 = scaleDime(dimes.d300),
            d800 = scaleDime(dimes.d800),
        )
    }

    fun scaleTypes(types: FinsibleTypes): FinsibleTypes {
        return FinsibleTypes(
            t72 = scaleText(types.t72),
            t64 = scaleText(types.t64),
            t56 = scaleText(types.t56),
            t48 = scaleText(types.t48),
            t44 = scaleText(types.t44),
            t40 = scaleText(types.t40),
            t36 = scaleText(types.t36),
            t32 = scaleText(types.t32),
            t28 = scaleText(types.t28),
            t24 = scaleText(types.t24),
            t20 = scaleText(types.t20),
            t16 = scaleText(types.t16),
            t14 = scaleText(types.t14),
            t12 = scaleText(types.t12),
            t10 = scaleText(types.t10),
            t8 = scaleText(types.t8)
        )
    }

    fun scaleCustomDime(dp: Dp): Dp {
        return scaleDime(dp)
    }
}