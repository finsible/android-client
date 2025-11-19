package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable

@Composable
fun TabBackHandler(enabled: Boolean = true, customBackHandler: (() -> Unit)? = null) {
    val activity = LocalActivity.current

    BackHandler(enabled = enabled) {
        customBackHandler?.invoke() ?: activity?.moveTaskToBack(true)
    }
}
