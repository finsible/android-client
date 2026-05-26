package com.itsjeel01.finsiblefrontend.ui.component.newtransaction.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.NewTransactionSectionDefaults
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.NewTransactionSectionLabel
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleTextField
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleTextFieldDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.model.event.NewTransactionUiEvent
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionFormState
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun NewTransactionNotesSection(
    state: NewTransactionFormState,
    onEvent: (NewTransactionUiEvent) -> Unit,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(NewTransactionSectionDefaults.headerSpacing)
    ) {
        NewTransactionSectionLabel(
            label = "Note",
            hint = "Optional",
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(FinsibleTheme.sizes.icon.xs),
                    painter = painterResource(id = com.composables.icons.materialsymbols.outlined.R.drawable.materialsymbols_ic_edit_square_outlined),
                    contentDescription = null,
                    tint = FinsibleTheme.colors.contentSecondary
                )
            },
            trailingContent = {
                FinsibleText(
                    text = "${state.description.length}/256",
                    textStyle = FinsibleTheme.typography.bodyMd,
                    colorVariant = FinsibleTextColorVariant.Secondary
                )
            }
        )

        Column(verticalArrangement = Arrangement.spacedBy(NewTransactionSectionDefaults.contentSpacing)) {
            FinsibleTextField(
                value = state.description,
                onValueChange = { onEvent(NewTransactionUiEvent.NotesChanged(it)) },
                placeholder = stringResource(R.string.description_placeholder),
                inputConfig = FinsibleTextFieldDefaults.inputConfig(
                    maxLength = 256,
                    imeAction = ImeAction.Done
                ),
                size = FinsibleSize.Small,
                colors = FinsibleTextFieldDefaults.colors().copy(
                    focusedBorderColor = Color.Transparent,
                    borderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent,
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus(force = true)
                        keyboardController?.hide()
                    }
                ),
                singleLine = false,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

