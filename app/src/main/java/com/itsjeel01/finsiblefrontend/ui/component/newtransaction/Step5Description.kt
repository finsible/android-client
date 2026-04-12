package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleTextField
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleTextFieldDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun Step5Description(
    description: String,
    onDescriptionChange: (String) -> Unit,
    focusRequester: FocusRequester,
) {
    val maxLength = 256
    val counterText = "${description.length}/$maxLength"
    val isNearLimit = description.length.toFloat() / maxLength > 0.9f
    val counterColor = when {
        description.length >= maxLength -> FinsibleTheme.colors.error
        isNearLimit -> FinsibleTheme.colors.warning
        else -> FinsibleTheme.colors.secondaryContent
    }

    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
    }

    Column(Modifier.padding(vertical = FinsibleTheme.dimes.d8)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FinsibleText(
                text = stringResource(R.string.description_label),
                variant = FinsibleTextVariant.BodyMedium,
                colorVariant = FinsibleTextColorVariant.Secondary
            )
            FinsibleText(
                text = counterText,
                variant = FinsibleTextVariant.SmallBodyRegular,
                color = counterColor,
                textAlign = TextAlign.End
            )
        }

        FinsibleTextField(
            value = description,
            onValueChange = onDescriptionChange,
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = stringResource(R.string.description_placeholder),
            size = FinsibleSize.Large,
            singleLine = false,
            minLines = 4,
            maxLines = 8,
            inputConfig = FinsibleTextFieldDefaults.inputConfig(maxLength = maxLength),
            focusRequester = focusRequester
        )
    }
}