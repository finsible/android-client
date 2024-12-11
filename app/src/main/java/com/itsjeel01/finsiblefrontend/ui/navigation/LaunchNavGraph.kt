package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.itsjeel01.finsiblefrontend.ui.view.screens.DashboardScreen
import com.itsjeel01.finsiblefrontend.ui.view.screens.LaunchScreen
import com.itsjeel01.finsiblefrontend.ui.view.screens.OnboardingScreen

fun NavGraphBuilder.launchNavGraph() {

    composable<Routes.LaunchScreen> {
        LaunchScreen()
    }

    composable<Routes.DashboardScreen> {
        DashboardScreen()
    }

    composable<Routes.OnboardingScreen> {
        OnboardingScreen()
    }
}