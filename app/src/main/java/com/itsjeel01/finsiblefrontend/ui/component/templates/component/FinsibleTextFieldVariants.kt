package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleTextFieldDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTextFieldColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTextFieldSizes

/** Renders a numeric single-line amount field with sensible keyboard defaults.
 * @param value Current input text value.
 * @param onValueChange Callback invoked when the text changes.
 * @param placeholder Placeholder text shown when [value] is empty.
 * @param modifier Modifier applied to the field container.
 * @param label Optional field label rendered above the input.
 * @param supportingText Optional supporting or helper text rendered below the field.
 * @param size Size token used to resolve default dimensions.
 * @param shapeVariant Shape token used to resolve default field shape.
 * @param colors Color tokens used for all field states.
 * @param sizes Size tokens used for spacing, height, and typography.
 */
@Composable
fun FinsibleAmountTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    supportingText: String? = null,
    size: FinsibleSize = FinsibleSize.Medium,
    shapeVariant: FinsibleShape = FinsibleShape.Rounded,
    colors: FinsibleTextFieldColors = FinsibleTextFieldDefaults.colors(),
    sizes: FinsibleTextFieldSizes = FinsibleTextFieldDefaults.sizes(size, shapeVariant)
) {
    val amountKeyboard = FinsibleTextFieldDefaults.inputConfig(
        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number,
        imeAction = androidx.compose.ui.text.input.ImeAction.Done
    )

    FinsibleTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        modifier = modifier,
        label = label,
        supportingText = supportingText,
        size = size,
        shapeVariant = shapeVariant,
        colors = colors,
        sizes = sizes,
        inputConfig = amountKeyboard,
        singleLine = true,
        inputFilter = { new ->
            new.all { it.isDigit() || it == '.' || it == ',' } &&
                new.count { it == '.' || it == ',' } <= 1
        }
    )
}

/** Renders a multi-line note field wrapper with templatised defaults.
 * @param value Current input text value.
 * @param onValueChange Callback invoked when the text changes.
 * @param placeholder Placeholder text shown when [value] is empty.
 * @param modifier Modifier applied to the field container.
 * @param label Optional field label rendered above the input.
 * @param supportingText Optional supporting or helper text rendered below the field.
 * @param size Size token used to resolve default dimensions.
 * @param shapeVariant Shape token used to resolve default field shape.
 * @param colors Color tokens used for all field states.
 * @param sizes Size tokens used for spacing, height, and typography.
 */
@Composable
fun FinsibleNoteTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    supportingText: String? = null,
    size: FinsibleSize = FinsibleSize.Medium,
    shapeVariant: FinsibleShape = FinsibleShape.Rounded,
    colors: FinsibleTextFieldColors = FinsibleTextFieldDefaults.colors(),
    sizes: FinsibleTextFieldSizes = FinsibleTextFieldDefaults.sizes(size, shapeVariant)
) {
    FinsibleTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        modifier = modifier,
        label = label,
        supportingText = supportingText,
        size = size,
        shapeVariant = shapeVariant,
        colors = colors,
        sizes = sizes,
        singleLine = false,
        minLines = 2,
        maxLines = 4,
        textAlign = TextAlign.Start
    )
}


