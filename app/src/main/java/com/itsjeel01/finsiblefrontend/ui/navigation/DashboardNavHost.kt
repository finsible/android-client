package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.itsjeel01.finsiblefrontend.ui.screen.BalanceScreen
import com.itsjeel01.finsiblefrontend.ui.screen.FormScreen
import com.itsjeel01.finsiblefrontend.ui.screen.HomeScreen
import com.itsjeel01.finsiblefrontend.ui.screen.InsightsScreen
import com.itsjeel01.finsiblefrontend.ui.screen.SettingsScreen

@Composable
fun DashboardNavHost(
    dashboardNavController: NavHostController,
    navHostController: NavHostController,
    paddingValues: PaddingValues,
) {
    NavHost(navController = dashboardNavController, startDestination = Routes.HomeScreen) {
        composable<Routes.HomeScreen> {
            HomeScreen()
        }
        composable<Routes.InsightsScreen> {
            InsightsScreen()
        }
        composable<Routes.BalanceScreen> {
            BalanceScreen()
        }
        composable<Routes.SettingsScreen> {
            SettingsScreen(onLogout = {
                navHostController.navigate(Routes.OnboardingScreen) {
                    popUpTo(Routes.LaunchScreen) { inclusive = true }
                }
            })
        }
        composable<Routes.FormScreen> {
            FormScreen()
        }
    }
}
