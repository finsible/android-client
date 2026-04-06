package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.itsjeel01.finsiblefrontend.data.di.hiltUserLocale
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleTextDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant

/** Stateless text wrapper that applies semantic variants and casing rules.
 * @param text The text to display.
 * @param modifier Modifier to apply to the text.
 * @param variant The variant to apply to the text.
 * @param color The color to apply to the text.
 * @param colorVariant The color variant to apply to the text.
 * @param onTextLayout Callback to invoke when the text layout is completed.
 * @param textStyleOverride Optional override for the text style.
 * @param textAlign The text alignment to apply to the text.
 * @param maxLines The maximum number of lines to display.
 * @param overflow The overflow behavior to apply to the text.
 * @param uppercase Whether to apply uppercase to the text.
 * @param underline Whether to apply underline to the text.
 * @param strikethrough Whether to apply strikethrough to the text.
 * @param softWrap Whether to apply soft wrap to the text.
 * @param minLines The minimum number of lines to display.
 * @param isDisplayFont Whether to apply the display font to the text.
 **/
@Composable
fun FinsibleText(
    text: String,
    modifier: Modifier = Modifier,
    variant: FinsibleTextVariant = FinsibleTextVariant.BodyRegular,
    color: Color = Color.Unspecified,
    colorVariant: FinsibleTextColorVariant = FinsibleTextColorVariant.Primary,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    textStyleOverride: TextStyle? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    uppercase: Boolean = false,
    underline: Boolean = false,
    strikethrough: Boolean = false,
    softWrap: Boolean = true,
    minLines: Int = 1,
    isDisplayFont: Boolean? = null
) {
    val spec = FinsibleTextDefaults.style(
        variant = variant,
        color = color,
        colorVariant = colorVariant,
        textStyleOverride = textStyleOverride,
        uppercase = uppercase,
        underline = underline,
        strikethrough = strikethrough,
        isDisplayFont = isDisplayFont
    )

    val displayText = if (spec.uppercase) text.uppercase(hiltUserLocale()) else text

    BasicText(
        text = displayText,
        modifier = modifier,
        style = spec.textStyle.copy(
            color = spec.color,
            textAlign = textAlign ?: spec.textStyle.textAlign,
            textDecoration = spec.textStyle.textDecoration
        ),
        onTextLayout = onTextLayout,
        maxLines = maxLines,
        overflow = overflow,
        softWrap = softWrap,
        minLines = minLines
    )
}
