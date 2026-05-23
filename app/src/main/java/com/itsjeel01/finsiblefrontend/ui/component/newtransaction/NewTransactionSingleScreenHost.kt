package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun NewTransactionSingleScreenHost(
    inputSection: @Composable (Modifier) -> Unit,
    ctaSection: @Composable (Modifier) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FinsibleTheme.colors.surfaceBase)
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus(true)
                    keyboardController?.hide()
                }
            }
            .padding(horizontal = FinsibleTheme.spacing.insetLg, vertical = FinsibleTheme.spacing.insetLg)
    ) {
        inputSection(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(Modifier.height(FinsibleTheme.spacing.gapMd))

        ctaSection(
            Modifier
                .fillMaxWidth()
                .background(FinsibleTheme.colors.surfaceBase)
                .windowInsetsPadding(WindowInsets.ime)
        )
    }
}

