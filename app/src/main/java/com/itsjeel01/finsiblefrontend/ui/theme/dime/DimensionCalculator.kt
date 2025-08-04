package com.itsjeel01.finsiblefrontend.ui.theme.dime

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object DimensionCalculator {

    fun calculate(screenWidth: Dp, screenHeight: Dp, density: Float): AppDimensions {
        val screenCategory = resolveScreenCategory(screenWidth)
        val scaleFactor = screenCategory.scale
        val baseUnit = calculateBaseUnit(screenWidth)

        val screenMetadata = ScreenMetadata(
            width = screenWidth,
            height = screenHeight,
            density = density,
            scaleFactor = scaleFactor,
            category = screenCategory
        )

        return AppDimensions(
            size = calculateSizing(baseUnit, scaleFactor),
            icon = calculateIcons(baseUnit, scaleFactor),
            radius = calculateRadius(scaleFactor),
            border = calculateBorders(scaleFactor),
            layout = calculateLayout(screenWidth, screenHeight, scaleFactor),
            screen = screenMetadata
        )
    }

    /** Resolves the screen category based on the width in Dp. */
    private fun resolveScreenCategory(screenWidth: Dp): ScreenCategory {
        return when {
            screenWidth < 360.dp -> ScreenCategory.COMPACT
            screenWidth < 400.dp -> ScreenCategory.STANDARD
            screenWidth < 450.dp -> ScreenCategory.LARGE
            else -> ScreenCategory.EXTRA_LARGE
        }
    }

    /** Calculates the base unit for spacing and dimensions based on the screen width. */
    private fun calculateBaseUnit(screenWidth: Dp): Dp {
        return (screenWidth.value * 0.02f).dp.coerceIn(6.dp, 12.dp)
    }

    private fun calculateSizing(baseUnit: Dp, scaleFactor: Float): SizeDimensions {
        fun scale(baseDp: Float): Dp = (baseDp * scaleFactor).dp

        return SizeDimensions(
            size0 = scale(0f),
            size1 = scale(1f),
            size2 = scale(2f),
            size4 = scale(4f),
            size6 = scale(6f),
            size8 = scale(8f),
            size10 = scale(10f),
            size12 = scale(12f),
            size14 = scale(14f),
            size16 = scale(16f),
            size18 = scale(18f),
            size20 = scale(20f),
            size22 = scale(22f),
            size24 = scale(24f),
            size26 = scale(26f),
            size28 = scale(28f),
            size30 = scale(30f),
            size32 = scale(32f),
            size34 = scale(34f),
            size36 = scale(36f),
            size38 = scale(38f),
            size40 = scale(40f),
            size42 = scale(42f),
            size44 = scale(44f),
            size46 = scale(46f),
            size48 = scale(48f),
            size50 = scale(50f),
            size52 = scale(52f),
            size54 = scale(54f),
            size56 = scale(56f),
            size58 = scale(58f),
            size60 = scale(60f),
            size64 = scale(64f),
            size68 = scale(68f),
            size72 = scale(72f),
            size76 = scale(76f),
            size80 = scale(80f),
            size88 = scale(88f),
            size96 = scale(96f),
            size104 = scale(104f),
            size112 = scale(112f),
            size120 = scale(120f),
            size128 = scale(128f),
            size136 = scale(136f),
            size144 = scale(144f),
            size152 = scale(152f),
            size160 = scale(160f),
            size168 = scale(168f),
            size176 = scale(176f),
            size184 = scale(184f),
            size192 = scale(192f),
            size200 = scale(200f)
        )
    }

    // Remove calculateComponents function

    private fun calculateIcons(baseUnit: Dp, scaleFactor: Float): IconDimensions {
        fun scale(baseDp: Float): Dp = (baseDp * scaleFactor).dp

        return IconDimensions(
            xs = scale(12f),
            sm = scale(16f),
            md = scale(20f),
            lg = scale(28f),
            xl = scale(40f)
        )
    }

    private fun calculateRadius(scaleFactor: Float): RadiusDimensions {
        fun scale(baseDp: Float): Dp = (baseDp * scaleFactor).dp

        return RadiusDimensions(
            zero = scale(0f),
            xs = scale(2f),
            sm = scale(4f),
            md = scale(8f),
            lg = scale(16f),
            xl = scale(24f),
            pill = scale(1000f),
            full = scale(9999f)
        )
    }

    private fun calculateBorders(scaleFactor: Float): BorderDimensions {
        fun scale(baseDp: Float): Dp = (baseDp * scaleFactor).dp

        return BorderDimensions(
            thin = scale(0.5f),
            medium = scale(1f),
            thick = scale(2f)
        )
    }

    private fun calculateLayout(
        screenWidth: Dp,
        screenHeight: Dp,
        scaleFactor: Float
    ): LayoutDimensions {
        fun scale(baseDp: Float): Dp = (baseDp * scaleFactor).dp

        val screenPaddingBase = (screenWidth.value * 0.08f).coerceIn(16f, 32f)

        return LayoutDimensions(
            screenPadding = (screenPaddingBase * scaleFactor).dp,
            containerPadding = scale(24f),
            cardPadding = scale(16f),
            sectionSpacing = scale(32f),
            elementSpacing = scale(12f)
        )
    }
}