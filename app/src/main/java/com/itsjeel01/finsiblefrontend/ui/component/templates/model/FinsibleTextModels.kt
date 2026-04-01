package com.itsjeel01.finsiblefrontend.ui.component.templates.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

/** Semantic variants for templatised text. */
@Immutable
enum class FinsibleTextVariant {
    XLargeHeadingBold,
    LargeHeadingBold,
    MediumHeadingBold,
    SmallHeadingBold,
    XLargeTitleNormal,
    LargeTitleNormal,
    LargeTitleMedium,
    LargeTitleSemiBold,
    LargeTitleExtraBold,
    MediumTitleNormal,
    MediumTitleMedium,
    MediumTitleSemiBold,
    MediumTitleBold,
    SmallTitleNormal,
    SmallTitleBold,
    SmallTitleMedium,
    SmallTitleExtraBold,
    XSmallTitleNormal,
    BodyRegular,
    BodyMedium,
    BodySemiBold,
    BodyBold,
    SmallBodyRegular,
    SmallBodyMedium,
    SmallBodySemiBold,
    SmallBodyBold,
    XLargeLabelSemiBold,
    LargeLabelSemiBold,
    SmallLabelRegular,
    SmallLabelMedium,
    SmallLabelSemiBold,
    MicroLabelMedium,
    MicroLabelSemiBold
}

@Immutable
enum class FinsibleTextColorVariant {
    Primary,
    Secondary,
    Accent,
    Link,
    Error,
}


/** Immutable holder for resolved text styling. */
@Immutable
data class FinsibleTextStyleSpec(
    val textStyle: TextStyle,
    val color: Color,
    val uppercase: Boolean,
)


