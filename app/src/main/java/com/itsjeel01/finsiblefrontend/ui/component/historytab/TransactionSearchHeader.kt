
package com.itsjeel01.finsiblefrontend.ui.component.historytab
import androidx.compose.ui.graphics.Color


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleTextField
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleTextFieldDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** A search bar for filtering transactions in the history tab. */
@Composable
fun TransactionSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    val dismissAction = remember(keyboardController, onClose) {
        {
            keyboardController?.hide()
            onValueChange("")
            onClose()
        }
    }

    FinsibleTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = stringResource(R.string.search_transactions_placeholder),
        modifier = modifier.fillMaxWidth(),
        size = FinsibleSize.Small,
        shapeVariant = FinsibleShape.Rounded,
        focusRequester = focusRequester,
        inputConfig = FinsibleTextFieldDefaults.inputConfig(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
        leadingIcon = {
            Icon(
                painter = painterResource(com.composables.icons.tabler.outline.R.drawable.tabler_ic_search_outline),
                contentDescription = null
            )
        },
        trailingIcon = {
            FinsibleButton(
                onClick = dismissAction,
                iconOnly = true,
                variant = FinsibleButtonVariant.Text,
                size = FinsibleSize.ExtraSmall,
                shapeVariant = FinsibleShape.Circle,
                icon = {
                    Icon(
                        painter = painterResource(com.composables.icons.materialicons.outlined.R.drawable.materialicons_ic_close_outlined),
                        contentDescription = stringResource(R.string.cd_close_search)
                    )
                }
            )
        },
        colors = FinsibleTextFieldDefaults.colors(
            containerColor = Color.Transparent,
            borderColor = FinsibleTheme.colors.borderDefault,
            focusedBorderColor = FinsibleTheme.colors.borderStrong,
            contentColor = FinsibleTheme.colors.contentPrimary,
            placeholderColor = FinsibleTheme.colors.contentPlaceholder,
            iconTint = FinsibleTheme.colors.contentTertiary
        )
    )
}