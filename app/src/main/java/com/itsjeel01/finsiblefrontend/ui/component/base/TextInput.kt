package com.itsjeel01.finsiblefrontend.ui.component.base

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import com.itsjeel01.finsiblefrontend.ui.theme.finsibleTextFieldColors

@Composable
fun FinsibleTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    commonProps: InputCommonProps = InputCommonProps(),
) {
    TextField(
        modifier = commonProps.fieldModifier(),
        value = value,
        label = commonProps.labelComposable(),
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        enabled = commonProps.enabled,
        isError = commonProps.isError,
        textStyle = commonProps.primaryTextStyle(),
        placeholder = commonProps.placeholderComposable(),
        leadingIcon = commonProps.leadingIconComposable(),
        trailingIcon = commonProps.trailingIconComposable(),
        supportingText = commonProps.supportingTextComposable(),
        colors = finsibleTextFieldColors(accentColor = commonProps.accentColor),
    )
}
