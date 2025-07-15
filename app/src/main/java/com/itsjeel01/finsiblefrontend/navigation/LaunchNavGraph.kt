package com.itsjeel01.finsiblefrontend.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.itsjeel01.finsiblefrontend.feature.auth.ui.screen.LaunchScreen
import com.itsjeel01.finsiblefrontend.feature.auth.ui.screen.OnboardingScreen
import com.itsjeel01.finsiblefrontend.feature.dashboard.ui.screen.DashboardScreen

fun NavGraphBuilder.launchNavGraph(navController: NavHostController) {

    composable<Routes.LaunchScreen> {
        LaunchScreen(navController)
    }

    composable<Routes.DashboardScreen> {
        DashboardScreen(navController)
    }

    composable<Routes.OnboardingScreen> {
        OnboardingScreen(navController)
    }
}