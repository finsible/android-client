package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.itsjeel01.finsiblefrontend.ui.screen.HomeScreen
import com.itsjeel01.finsiblefrontend.ui.screen.LaunchScreen
import com.itsjeel01.finsiblefrontend.ui.screen.OnboardingScreen

fun NavGraphBuilder.appNavGraph(navController: NavHostController) {

    composable<AppRoutes.Launch> {
        LaunchScreen(
            navigateToOnboarding = {
                navController.navigate(AppRoutes.Onboarding) {
                    popUpTo<AppRoutes.Launch> { inclusive = true }
                }
            },
            navigateToDashboard = {
                navController.navigate(AppRoutes.Home) {
                    popUpTo<AppRoutes.Launch> { inclusive = true }
                }
            }
        )
    }

    composable<AppRoutes.Onboarding> {
        OnboardingScreen(
            navigateToDashboard = {
                navController.navigate(AppRoutes.Home) {
                    popUpTo<AppRoutes.Onboarding> { inclusive = true }
                }
            }
        )
    }

    composable<AppRoutes.Home> {
        HomeScreen(
            navigateToOnboarding = {
                navController.navigate(AppRoutes.Onboarding) {
                    popUpTo<AppRoutes.Home> { inclusive = true }
                }
            },
        )
    }
}