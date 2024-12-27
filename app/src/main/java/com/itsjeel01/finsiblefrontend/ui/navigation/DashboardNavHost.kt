package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.itsjeel01.finsiblefrontend.ui.view.screens.BalanceScreen
import com.itsjeel01.finsiblefrontend.ui.view.screens.HomeScreen
import com.itsjeel01.finsiblefrontend.ui.view.screens.InsightsScreen
import com.itsjeel01.finsiblefrontend.ui.view.screens.NewTransactionForm
import com.itsjeel01.finsiblefrontend.ui.view.screens.SettingsScreen

@Composable
fun DashboardNavHost(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(navController = navController, startDestination = Routes.HomeScreen) {
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
            SettingsScreen()
        }
        composable<Routes.NewTransactionForm> {
            NewTransactionForm()
        }
    }
}