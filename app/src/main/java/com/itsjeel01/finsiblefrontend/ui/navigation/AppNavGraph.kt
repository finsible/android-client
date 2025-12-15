package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.itsjeel01.finsiblefrontend.BuildConfig
import com.itsjeel01.finsiblefrontend.ui.screen.HomeScreen
import com.itsjeel01.finsiblefrontend.ui.screen.LaunchScreen
import com.itsjeel01.finsiblefrontend.ui.screen.OnboardingScreen
import com.itsjeel01.finsiblefrontend.ui.screen.TestScreen

/** App-level navigation graph with navigation coordinator for better separation of concerns. */
fun NavGraphBuilder.appNavGraph(navController: NavHostController) {
    val navigationCoordinator = AppNavigationCoordinator(navController)

    // Test screen (debug builds only)
    if (BuildConfig.DEBUG) {
        composable<AppRoutes.Test> {
            TestScreen(
                onNavigateToApp = {
                    navigationCoordinator.navigateTo(AppRoutes.Launch) {
                        popUpTo<AppRoutes.Test> { inclusive = true }
                    }
                }
            )
        }
    }

    composable<AppRoutes.Launch> {
        LaunchScreen(
            navigateToOnboarding = {
                navigationCoordinator.navigateToOnboarding()
            },
            navigateToDashboard = {
                navigationCoordinator.navigateToHome(AppRoutes.Launch)
            }
        )
    }

    composable<AppRoutes.Onboarding> {
        OnboardingScreen(
            navigateToDashboard = {
                navigationCoordinator.navigateToHome(AppRoutes.Onboarding)
            }
        )
    }

    composable<AppRoutes.Home> {
        HomeScreen(
            navigateToOnboarding = {
                navigationCoordinator.navigateToOnboardingFromHome()
            },
        )
    }
}