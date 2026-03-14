package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

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
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleTextFieldDefaults
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleTextFieldWithCounter
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun Step5Description(
    description: String,
    onDescriptionChange: (String) -> Unit,
    focusRequester: FocusRequester,
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
            sizes = FinsibleTextFieldDefaults.largeSizes(),
            singleLine = false,
            minLines = 4,
            maxLines = 8,
            label = stringResource(R.string.description_label),
            placeholder = stringResource(R.string.description_placeholder),
            showCounter = true,
            interactionSource = interactionSource
        )
    }
}