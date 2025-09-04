package com.itsjeel01.finsiblefrontend.ui.screen

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DashboardScreen(navigateToOnboarding: () -> Unit) {
    Scaffold { _ ->
        Text("Dashboard Screen")
    }
}
