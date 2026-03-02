package com.itsjeel01.finsiblefrontend.ui.screen.newtransaction

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.itsjeel01.finsiblefrontend.ui.component.fin.ComponentSize
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleTextFieldWithCounter
import com.itsjeel01.finsiblefrontend.ui.component.fin.TextFieldConfig
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun Step5Description(
    description: String,
    onDescriptionChange: (String) -> Unit,
    focusRequester: FocusRequester
) {
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
    }

    Column(Modifier.padding(vertical = FinsibleTheme.dimes.d8)) {
        FinsibleTextFieldWithCounter(
            value = description,
            onValueChange = onDescriptionChange,
            maxLength = 256,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            config = TextFieldConfig(
                size = ComponentSize.Large,
                singleLine = false,
                minLines = 4,
                maxLines = 8
            ),
            label = "Description",
            placeholder = "Add notes about this transaction...",
            showCounter = true,
            interactionSource = interactionSource
        )
    }
}