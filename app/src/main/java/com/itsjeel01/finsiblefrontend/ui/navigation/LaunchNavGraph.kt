package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.itsjeel01.finsiblefrontend.ui.screen.DashboardScreen
import com.itsjeel01.finsiblefrontend.ui.screen.LaunchScreen
import com.itsjeel01.finsiblefrontend.ui.screen.OnboardingScreen

fun NavGraphBuilder.launchNavGraph(navHostController: NavHostController) {

    composable<Routes.LaunchScreen> {
        LaunchScreen(navHostController)
    }

    composable<Routes.DashboardScreen> {
        DashboardScreen(navHostController)
    }

    composable<Routes.OnboardingScreen> {
        OnboardingScreen(navHostController)
    }
}
