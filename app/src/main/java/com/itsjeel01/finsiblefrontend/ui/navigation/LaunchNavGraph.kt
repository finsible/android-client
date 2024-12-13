package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.itsjeel01.finsiblefrontend.ui.view.screens.DashboardScreen
import com.itsjeel01.finsiblefrontend.ui.view.screens.LaunchScreen
import com.itsjeel01.finsiblefrontend.ui.view.screens.OnboardingScreen

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