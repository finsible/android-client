package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable

/** Centralized back handler for all tabs - minimizes app instead of navigating */
@Composable
fun TabBackHandler() {
    val activity = LocalActivity.current

    BackHandler(enabled = true) {
        activity?.moveTaskToBack(true)
    }
}
