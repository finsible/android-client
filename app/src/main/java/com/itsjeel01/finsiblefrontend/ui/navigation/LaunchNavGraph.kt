package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.itsjeel01.finsiblefrontend.ui.screen.DashboardScreen
import com.itsjeel01.finsiblefrontend.ui.screen.LaunchScreen
import com.itsjeel01.finsiblefrontend.ui.screen.OnboardingScreen

fun NavGraphBuilder.appNavGraph(navHostController: NavHostController) {

    composable<AppRoutes.Launch> {
        LaunchScreen(
            navigateToOnboarding = {
                navHostController.navigate(AppRoutes.Onboarding) {
                    popUpTo<AppRoutes.Launch> { inclusive = true }
                }
            },
            navigateToDashboard = {
                navHostController.navigate(AppRoutes.Dashboard) {
                    popUpTo<AppRoutes.Dashboard> { inclusive = true }
                }
            }
        )
    }

    composable<AppRoutes.Onboarding> {
        OnboardingScreen(
            navigateToDashboard = {
                navHostController.navigate(AppRoutes.Dashboard) {
                    popUpTo<AppRoutes.Dashboard> { inclusive = true }
                }
            }
        )
    }

    composable<AppRoutes.Dashboard> {
        DashboardScreen(
            navigateToOnboarding = {
                navHostController.navigate(AppRoutes.Onboarding) {
                    popUpTo<AppRoutes.Dashboard> { inclusive = true }
                }
            }
        )
    }
}
