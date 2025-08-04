package com.itsjeel01.finsiblefrontend.ui.theme.dime

import androidx.compose.ui.unit.Dp
import javax.annotation.concurrent.Immutable

@Immutable
data class AppDimensions(
    val size: SizeDimensions,
    val icon: IconDimensions,
    val radius: RadiusDimensions,
    val border: BorderDimensions,
    val layout: LayoutDimensions,
    val screen: ScreenMetadata
)

@Immutable
data class SizeDimensions(
    val size0: Dp,
    val size1: Dp,
    val size2: Dp,
    val size4: Dp,
    val size6: Dp,
    val size8: Dp,
    val size10: Dp,
    val size12: Dp,
    val size14: Dp,
    val size16: Dp,
    val size18: Dp,
    val size20: Dp,
    val size22: Dp,
    val size24: Dp,
    val size26: Dp,
    val size28: Dp,
    val size30: Dp,
    val size32: Dp,
    val size34: Dp,
    val size36: Dp,
    val size38: Dp,
    val size40: Dp,
    val size42: Dp,
    val size44: Dp,
    val size46: Dp,
    val size48: Dp,
    val size50: Dp,
    val size52: Dp,
    val size54: Dp,
    val size56: Dp,
    val size58: Dp,
    val size60: Dp,
    val size64: Dp,
    val size68: Dp,
    val size72: Dp,
    val size76: Dp,
    val size80: Dp,
    val size88: Dp,
    val size96: Dp,
    val size104: Dp,
    val size112: Dp,
    val size120: Dp,
    val size128: Dp,
    val size136: Dp,
    val size144: Dp,
    val size152: Dp,
    val size160: Dp,
    val size168: Dp,
    val size176: Dp,
    val size184: Dp,
    val size192: Dp,
    val size200: Dp
)

@Immutable
data class IconDimensions(
    val xs: Dp,
    val sm: Dp,
    val md: Dp,
    val lg: Dp,
    val xl: Dp
)

@Immutable
data class RadiusDimensions(
    val zero: Dp,
    val xs: Dp,
    val sm: Dp,
    val md: Dp,
    val lg: Dp,
    val xl: Dp,
    val pill: Dp,
    val full: Dp
)

@Immutable
data class BorderDimensions(
    val thin: Dp,
    val medium: Dp,
    val thick: Dp
)

@Immutable
data class LayoutDimensions(
    val screenPadding: Dp,
    val containerPadding: Dp,
    val cardPadding: Dp,
    val sectionSpacing: Dp,
    val elementSpacing: Dp
)

@Immutable
data class ScreenMetadata(
    val width: Dp,
    val height: Dp,
    val density: Float,
    val scaleFactor: Float,
    val category: ScreenCategory
)

enum class ScreenCategory(val scale: Float) {
    COMPACT(0.85f),
    STANDARD(1.0f),
    LARGE(1.15f),
    EXTRA_LARGE(1.3f);
}