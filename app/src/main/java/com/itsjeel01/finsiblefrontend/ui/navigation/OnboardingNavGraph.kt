package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.itsjeel01.finsiblefrontend.ui.view.AuthScreen

fun NavGraphBuilder.onboardingNavGraph() {
    composable<Routes.AuthScreen> {
        AuthScreen()
    }
}