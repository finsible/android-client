package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import com.itsjeel01.finsiblefrontend.data.di.hiltUserLocale
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.displayFont
import com.itsjeel01.finsiblefrontend.ui.theme.interfaceFont

/**
 * Stateless text wrapper that consumes semantic typography tokens directly.
 *
 * @param text The text to display.
 * @param textStyle Base [TextStyle] — pass [FinsibleTheme.typography] tokens directly
 *   (e.g. [FinsibleTheme.typography.bodyLg], [FinsibleTheme.typography.headingMd], etc.).
 * @param color Optional explicit color override.
 * @param colorVariant Semantic color variant resolved from [FinsibleTheme.colors].
 * @param textAlign Optional text alignment.
 * @param maxLines Maximum number of lines.
 * @param overflow Text overflow behaviour.
 * @param uppercase Whether to force uppercase (locale-aware).
 * @param underline Whether to underline.
 * @param strikethrough Whether to strikethrough.
 * @param softWrap Whether text wraps.
 * @param minLines Minimum number of lines.
 * @param isDisplayFont Override to force display (Oswald) or interface (Manrope) font.
 */
@Composable
fun FinsibleText(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = FinsibleTheme.typography.bodyLg,
    color: Color = Color.Unspecified,
    colorVariant: FinsibleTextColorVariant = FinsibleTextColorVariant.Primary,
    onTextLayout: (TextLayoutResult) -> Unit = {},
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
    val semantic = FinsibleTheme.colors

    val resolvedColor = if (color != Color.Unspecified) color else when (colorVariant) {
        FinsibleTextColorVariant.Primary -> semantic.contentPrimary
        FinsibleTextColorVariant.Secondary -> semantic.contentSecondary
        FinsibleTextColorVariant.Tertiary -> semantic.contentTertiary
        FinsibleTextColorVariant.Accent -> semantic.brandInteractive
        FinsibleTextColorVariant.Link -> semantic.contentLink
        FinsibleTextColorVariant.Error -> semantic.feedbackError
    }

    val decoration = when {
        underline && strikethrough -> TextDecoration.combine(listOf(TextDecoration.Underline, TextDecoration.LineThrough))
        underline -> TextDecoration.Underline
        strikethrough -> TextDecoration.LineThrough
        else -> TextDecoration.None
    }

    var resolvedStyle = textStyle.copy(textDecoration = decoration)

    resolvedStyle = when {
        isDisplayFont == true -> resolvedStyle.displayFont()
        isDisplayFont == false -> resolvedStyle.interfaceFont()
        else -> resolvedStyle
    }

    val displayText = if (uppercase) text.uppercase(hiltUserLocale()) else text

    BasicText(
        text = displayText,
        modifier = modifier,
        style = resolvedStyle.copy(
            color = resolvedColor,
            textAlign = textAlign ?: resolvedStyle.textAlign,
        ),
        onTextLayout = onTextLayout,
        maxLines = maxLines,
        overflow = overflow,
        softWrap = softWrap,
        minLines = minLines
    )
}
