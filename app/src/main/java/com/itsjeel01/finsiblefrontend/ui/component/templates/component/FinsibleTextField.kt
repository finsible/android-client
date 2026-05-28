package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleTextFieldDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTextFieldColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTextFieldInputConfig
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTextFieldSizes
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Renders a stateless templatised text field with optional icons and validation hooks.
 * @param value Current input text value.
 * @param onValueChange Callback invoked when the text changes.
 * @param placeholder Placeholder text shown when [value] is empty.
 * @param modifier Modifier applied to the text field container.
 * @param label Optional field label rendered above the input.
 * @param enabled Whether the input is enabled for user interaction.
 * @param readOnly Whether text editing is disabled while keeping focus behavior.
 * @param isError Whether to render error visuals.
 * @param supportingText Optional supporting or error text shown below the field.
 * @param size Size token used to resolve default dimensions.
 * @param shapeVariant Shape token used to resolve default field shape.
 * @param colors Color tokens used for container, border, text, and icons.
 * @param sizes Size tokens used for spacing and typography.
 * @param inputConfig Input configuration for keyboard and max-length behavior.
 * @param leadingIcon Optional icon content placed before the text.
 * @param trailingIcon Optional icon content placed after the text.
 * @param visualTransformation Visual transformation applied to rendered text.
 * @param keyboardActions Keyboard action callbacks.
 * @param textAlign Horizontal alignment of the input text.
 * @param contentDescription Optional accessibility content description.
 * @param singleLine Whether the field should be restricted to a single line.
 * @param minLines Minimum number of lines to display.
 * @param maxLines Maximum number of lines to display.
 * @param focusRequester Optional focus requester attached to the input.
 * @param inputFilter Optional predicate used to allow or reject candidate text.
 */
@Composable
fun FinsibleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    supportingText: String? = null,
    size: FinsibleSize = FinsibleSize.Medium,
    shapeVariant: FinsibleShape = FinsibleShape.Rounded,
    colors: FinsibleTextFieldColors = FinsibleTextFieldDefaults.colors(),
    sizes: FinsibleTextFieldSizes = FinsibleTextFieldDefaults.sizes(size, shapeVariant),
    inputConfig: FinsibleTextFieldInputConfig = FinsibleTextFieldDefaults.inputConfig(),
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    textAlign: TextAlign = TextAlign.Start,
    contentDescription: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    focusRequester: FocusRequester? = null,
    inputFilter: ((String) -> Boolean)? = null
) {
    require(placeholder.isNotBlank()) { "placeholder must be non-blank." }
    require(label == null || label.isNotBlank()) { "label must be non-blank when provided." }
    require(inputConfig.maxLength == null || inputConfig.maxLength > 0) { "maxLength must be > 0 when provided." }
    require(minLines in 1 .. maxLines) { "minLines must be > 0 and maxLines >= minLines." }
    require(sizes.iconSize > 0.dp) { "sizes.iconSize must be > 0." }
    require(sizes.horizontalPadding >= 0.dp) { "sizes.horizontalPadding must be >= 0." }
    require(sizes.verticalPadding >= 0.dp) { "sizes.verticalPadding must be >= 0." }
    require(sizes.cornerRadius >= 0.dp) { "sizes.cornerRadius must be >= 0." }

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val resolvedBorderColor by animateColorAsState(
        targetValue = when {
            isError -> colors.errorBorderColor
            isFocused -> colors.focusedBorderColor
            else -> colors.borderColor
        },
        animationSpec = spring(),
        label = "borderColor"
    )

    val resolvedContainerColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.disabledContainerColor
            isError -> colors.errorContainerColor
            isFocused -> colors.focusedContainerColor
            else -> colors.containerColor
        },
        animationSpec = spring(),
        label = "containerColor"
    )

    val shape: Shape = RoundedCornerShape(sizes.cornerRadius)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                if (!contentDescription.isNullOrEmpty()) this.contentDescription = contentDescription
                if (!enabled) disabled()
            },
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackXs)
    ) {
        if (!label.isNullOrEmpty()) {
            FinsibleText(
                text = label,
                textStyle = sizes.supportingTextStyle,
                color = colors.supportingTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        SurfaceField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            enabled = enabled,
            readOnly = readOnly,
            interactionSource = interactionSource,
            colors = colors,
            sizes = sizes,
            borderColor = resolvedBorderColor,
            containerColor = resolvedContainerColor,
            shape = shape,
            inputConfig = inputConfig,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            keyboardActions = keyboardActions,
            textAlign = textAlign,
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            focusRequester = focusRequester,
            inputFilter = inputFilter
        )

        if (!supportingText.isNullOrEmpty()) {
            val supportingColor = if (isError) colors.errorTextColor else colors.supportingTextColor
            FinsibleText(
                text = supportingText,
                textStyle = sizes.supportingTextStyle,
                color = supportingColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun SurfaceField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    enabled: Boolean,
    readOnly: Boolean,
    interactionSource: MutableInteractionSource,
    colors: FinsibleTextFieldColors,
    sizes: FinsibleTextFieldSizes,
    borderColor: androidx.compose.ui.graphics.Color,
    containerColor: androidx.compose.ui.graphics.Color,
    shape: Shape,
    inputConfig: FinsibleTextFieldInputConfig,
    leadingIcon: (@Composable () -> Unit)?,
    trailingIcon: (@Composable () -> Unit)?,
    visualTransformation: VisualTransformation,
    keyboardActions: KeyboardActions,
    textAlign: TextAlign,
    singleLine: Boolean,
    minLines: Int,
    maxLines: Int,
    focusRequester: FocusRequester?,
    inputFilter: ((String) -> Boolean)?
) {
    BasicTextField(
        value = value,
        onValueChange = { newValue ->
            val constrainedValue = inputConfig.maxLength?.let { maxLength ->
                newValue.take(maxLength)
            } ?: newValue

            if (inputFilter != null && !inputFilter(constrainedValue)) return@BasicTextField
            if (constrainedValue != value) onValueChange(constrainedValue)
        },
        enabled = enabled,
        readOnly = readOnly,
        textStyle = sizes.textStyle.copy(color = if (enabled) colors.contentColor else colors.disabledContentColor, textAlign = textAlign),
        interactionSource = interactionSource,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        keyboardOptions = inputConfig.keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        cursorBrush = androidx.compose.ui.graphics.SolidColor(colors.contentColor),
        modifier = focusRequester?.let { Modifier.focusRequester(it) } ?: Modifier
    ) { innerTextField ->
        Row(
            modifier = Modifier
                .clip(shape)
                .background(containerColor)
                .border(width = FinsibleTheme.stroke.thin, color = borderColor, shape = shape)
                .padding(horizontal = sizes.horizontalPadding, vertical = sizes.verticalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.inlineMd)
        ) {
            val iconTint by animateColorAsState(
                targetValue = if (enabled) colors.iconTint else colors.disabledIconTint,
                animationSpec = spring(),
                label = "iconTint"
            )

            if (leadingIcon != null) {
                IconSlot(tint = iconTint, size = sizes.iconSize) { leadingIcon() }
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = if (singleLine && minLines == 1) {
                    Alignment.CenterStart
                } else {
                    Alignment.TopStart
                }
            ) {
                if (value.isEmpty()) {
                    FinsibleText(
                        text = placeholder,
                        textStyle = sizes.placeholderStyle,
                        color = if (enabled) colors.placeholderColor else colors.disabledContentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                innerTextField()
            }

            if (trailingIcon != null) {
                IconSlot(tint = iconTint, size = sizes.iconSize) { trailingIcon() }
            }
        }
    }
}

@Composable
private fun IconSlot(
    tint: androidx.compose.ui.graphics.Color,
    size: Dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides tint) {
            content()
        }
    }
}
