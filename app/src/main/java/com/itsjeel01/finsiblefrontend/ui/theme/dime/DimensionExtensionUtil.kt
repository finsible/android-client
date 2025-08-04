package com.itsjeel01.finsiblefrontend.ui.theme.dime

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.itsjeel01.finsiblefrontend.common.InputFieldSize

@Composable
fun AppDimensions.size(token: Size): Dp = when (token) {
    Size.ZERO -> size.size0
    Size.S1 -> size.size1
    Size.S2 -> size.size2
    Size.S4 -> size.size4
    Size.S6 -> size.size6
    Size.S8 -> size.size8
    Size.S10 -> size.size10
    Size.S12 -> size.size12
    Size.S14 -> size.size14
    Size.S16 -> size.size16
    Size.S18 -> size.size18
    Size.S20 -> size.size20
    Size.S22 -> size.size22
    Size.S24 -> size.size24
    Size.S26 -> size.size26
    Size.S28 -> size.size28
    Size.S30 -> size.size30
    Size.S32 -> size.size32
    Size.S34 -> size.size34
    Size.S36 -> size.size36
    Size.S38 -> size.size38
    Size.S40 -> size.size40
    Size.S42 -> size.size42
    Size.S44 -> size.size44
    Size.S46 -> size.size46
    Size.S48 -> size.size48
    Size.S50 -> size.size50
    Size.S52 -> size.size52
    Size.S54 -> size.size54
    Size.S56 -> size.size56
    Size.S58 -> size.size58
    Size.S60 -> size.size60
    Size.S64 -> size.size64
    Size.S68 -> size.size68
    Size.S72 -> size.size72
    Size.S76 -> size.size76
    Size.S80 -> size.size80
    Size.S88 -> size.size88
    Size.S96 -> size.size96
    Size.S104 -> size.size104
    Size.S112 -> size.size112
    Size.S120 -> size.size120
    Size.S128 -> size.size128
    Size.S136 -> size.size136
    Size.S144 -> size.size144
    Size.S152 -> size.size152
    Size.S160 -> size.size160
    Size.S168 -> size.size168
    Size.S176 -> size.size176
    Size.S184 -> size.size184
    Size.S192 -> size.size192
    Size.S200 -> size.size200
    Size.FULL -> screen.width
}

/** Legacy spacing function - now delegates to size() */
@Composable
fun AppDimensions.spacing(token: Size): Dp = size(token)

@Composable
fun AppDimensions.iconSize(token: IconSize): Dp = when (token) {
    IconSize.XS -> icon.xs
    IconSize.SM -> icon.sm
    IconSize.MD -> icon.md
    IconSize.LG -> icon.lg
    IconSize.XL -> icon.xl
}

@Composable
fun AppDimensions.cornerRadius(token: Radius): Dp = when (token) {
    Radius.ZERO -> radius.zero
    Radius.XS -> radius.xs
    Radius.SM -> radius.sm
    Radius.MD -> radius.md
    Radius.LG -> radius.lg
    Radius.XL -> radius.xl
    Radius.PILL -> radius.pill
    Radius.FULL -> radius.full
}

@Composable
fun AppDimensions.borderWidth(token: BorderStroke): Dp = when (token) {
    BorderStroke.THIN -> border.thin
    BorderStroke.MEDIUM -> border.medium
    BorderStroke.THICK -> border.thick
}

@Composable
fun AppDimensions.layoutSpacing(token: Layout): Dp = when (token) {
    Layout.SCREEN_PADDING -> layout.screenPadding
    Layout.CONTAINER_PADDING -> layout.containerPadding
    Layout.CARD_PADDING -> layout.cardPadding
    Layout.SECTION_SPACING -> layout.sectionSpacing
    Layout.ELEMENT_SPACING -> layout.elementSpacing
}

@Composable
fun AppDimensions.roundedCornerShape(token: Radius): Shape {
    return RoundedCornerShape(cornerRadius(token))
}

@Composable
fun AppDimensions.roundedCornerShape(
    topStart: Radius = Radius.ZERO,
    topEnd: Radius = Radius.ZERO,
    bottomEnd: Radius = Radius.ZERO,
    bottomStart: Radius = Radius.ZERO
): Shape {
    return RoundedCornerShape(
        topStart = cornerRadius(topStart),
        topEnd = cornerRadius(topEnd),
        bottomEnd = cornerRadius(bottomEnd),
        bottomStart = cornerRadius(bottomStart)
    )
}

/** Unified modifier extensions using Size enum */
@Composable
fun Modifier.iconSize(token: IconSize = IconSize.MD): Modifier {
    val dims = appDimensions()
    return this.size(dims.iconSize(token))
}

@Composable
fun Modifier.squareSize(size: Dp): Modifier = this.size(size)

@Composable
fun Modifier.squareSize(token: Size): Modifier {
    val dims = appDimensions()
    return this.size(dims.size(token))
}

@Composable
fun Modifier.height(token: Size): Modifier {
    val dims = appDimensions()
    return this.height(dims.size(token))
}

@Composable
fun Modifier.width(token: Size): Modifier {
    val dims = appDimensions()
    return when (token) {
        Size.FULL -> this.fillMaxWidth()
        else -> this.width(dims.size(token))
    }
}

@Composable
fun Modifier.size(token: Size): Modifier {
    val dims = appDimensions()
    return when (token) {
        Size.FULL -> this.fillMaxWidth()
        else -> this.size(dims.size(token))
    }
}

/** Padding extensions */
@Composable
fun Modifier.paddingAll(token: Size): Modifier {
    val dims = appDimensions()
    return this.padding(dims.size(token))
}

@Composable
fun Modifier.paddingHorizontal(token: Size): Modifier {
    val dims = appDimensions()
    return this.padding(horizontal = dims.size(token))
}

@Composable
fun Modifier.paddingVertical(token: Size): Modifier {
    val dims = appDimensions()
    return this.padding(vertical = dims.size(token))
}

@Composable
fun Modifier.paddingStart(token: Size): Modifier {
    val dims = appDimensions()
    return this.padding(start = dims.size(token))
}

@Composable
fun Modifier.paddingEnd(token: Size): Modifier {
    val dims = appDimensions()
    return this.padding(end = dims.size(token))
}

@Composable
fun Modifier.paddingTop(token: Size): Modifier {
    val dims = appDimensions()
    return this.padding(top = dims.size(token))
}

@Composable
fun Modifier.paddingBottom(token: Size): Modifier {
    val dims = appDimensions()
    return this.padding(bottom = dims.size(token))
}

@Composable
fun Modifier.paddingAsymmetric(
    horizontal: Size,
    vertical: Size
): Modifier {
    val dims = appDimensions()
    return this.padding(
        horizontal = dims.size(horizontal),
        vertical = dims.size(vertical)
    )
}

@Composable
fun Modifier.paddingEach(
    start: Size = Size.S16,
    top: Size = Size.S16,
    end: Size = Size.S16,
    bottom: Size = Size.S16
): Modifier {
    val dims = appDimensions()
    return this.padding(
        start = dims.size(start),
        top = dims.size(top),
        end = dims.size(end),
        bottom = dims.size(bottom)
    )
}

@Composable
fun Modifier.screenPadding(): Modifier {
    val dims = appDimensions()
    return this.padding(horizontal = dims.layoutSpacing(Layout.SCREEN_PADDING))
}

@Composable
fun Modifier.screenPadding(vertical: Size): Modifier {
    val dims = appDimensions()
    return this.padding(
        horizontal = dims.layoutSpacing(Layout.SCREEN_PADDING),
        vertical = dims.size(vertical)
    )
}

@Composable
fun Modifier.containerPadding(): Modifier {
    val dims = appDimensions()
    return this.padding(dims.layoutSpacing(Layout.CONTAINER_PADDING))
}

@Composable
fun Modifier.cardPadding(): Modifier {
    val dims = appDimensions()
    return this.padding(dims.layoutSpacing(Layout.CARD_PADDING))
}

/** Arrangement and PaddingValues extensions */
@Composable
fun spacedBy(token: Size): Arrangement.HorizontalOrVertical {
    val dims = appDimensions()
    return Arrangement.spacedBy(dims.size(token))
}

@Composable
fun paddingValues(token: Size): PaddingValues {
    val dims = appDimensions()
    return PaddingValues(dims.size(token))
}

@Composable
fun paddingValues(
    horizontal: Size,
    vertical: Size
): PaddingValues {
    val dims = appDimensions()
    return PaddingValues(
        horizontal = dims.size(horizontal),
        vertical = dims.size(vertical)
    )
}

@Composable
fun paddingValues(
    start: Size = Size.S16,
    top: Size = Size.S16,
    end: Size = Size.S16,
    bottom: Size = Size.S16
): PaddingValues {
    val dims = appDimensions()
    return PaddingValues(
        start = dims.size(start),
        top = dims.size(top),
        end = dims.size(end),
        bottom = dims.size(bottom)
    )
}

/** Convenience extensions for common sizes */
@Composable
fun AppDimensions.standardIcon(): Dp = iconSize(IconSize.MD)

@Composable
fun AppDimensions.smallIcon(): Dp = iconSize(IconSize.SM)

@Composable
fun AppDimensions.standardSpacing(): Dp = size(Size.S16)

@Composable
fun AppDimensions.tightSpacing(): Dp = size(Size.S8)

@Composable
fun AppDimensions.looseSpacing(): Dp = size(Size.S24)

/** Component-specific helpers */
@Composable
fun AppDimensions.buttonHeight(): Dp = size(Size.S48)

@Composable
fun AppDimensions.inputHeight(): Dp = size(Size.S56)

@Composable
fun AppDimensions.heroButtonHeight(): Dp = size(Size.S64)

@Composable
fun Modifier.buttonStyle(
    height: Size = Size.S48,
    radius: Radius = Radius.MD
): Modifier {
    val dims = appDimensions()
    return this
        .height(dims.size(height))
        .paddingHorizontal(Size.S24)
}

@Composable
fun Modifier.cardStyle(
    padding: Size = Size.S16
): Modifier {
    val dims = appDimensions()
    return this.padding(dims.size(padding))
}

@Composable
fun Modifier.iconButton(
    size: IconSize = IconSize.MD,
    padding: Size = Size.S8
): Modifier {
    val dims = appDimensions()
    return this
        .size(dims.iconSize(size) + dims.size(padding) * 2)
        .padding(dims.size(padding))
}

@Composable
fun Modifier.inputField(
    height: Size = Size.S56,
    radius: Radius = Radius.SM
): Modifier {
    val dims = appDimensions()
    return this
        .height(dims.size(height))
        .paddingHorizontal(Size.S16)
}

@Composable
fun AppDimensions.debugDimensions(tag: String = "AppDimensions") {
    Log.i(tag, "Screen: ${screen.width} x ${screen.height}")
    Log.i(tag, "Category: ${screen.category} (${screen.scaleFactor}x)")
    Log.i(tag, "Standard spacing: ${size(Size.S16)}")
    Log.i(tag, "Standard icon: ${iconSize(IconSize.MD)}")
    Log.i(tag, "Button height: ${size(Size.S48)}")
}

@Composable
fun AppDimensions.getDimension(token: Size): Float = size(token).value

@Composable
fun AppDimensions.getIconDimension(token: IconSize): Float = iconSize(token).value

@Composable
fun Modifier.buttonPadding(size: InputFieldSize): Modifier {
    return this.paddingAll(
        if (size == InputFieldSize.Small) Size.S12
        else Size.S16
    )
}

@Composable
fun Modifier.commonPropsModifier(
    size: InputFieldSize = InputFieldSize.Large,
): Modifier {
    val dims = appDimensions()
    return this.heightIn(
        min = if (size == InputFieldSize.Small) dims.size(Size.S48)
        else dims.size(Size.S56)
    )
}