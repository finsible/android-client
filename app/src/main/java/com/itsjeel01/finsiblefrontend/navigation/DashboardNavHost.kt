package com.itsjeel01.finsiblefrontend.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.itsjeel01.finsiblefrontend.feature.balance.ui.screen.BalanceScreen
import com.itsjeel01.finsiblefrontend.feature.form.ui.screen.NewTransactionForm
import com.itsjeel01.finsiblefrontend.feature.home.ui.screen.HomeScreen
import com.itsjeel01.finsiblefrontend.feature.insights.ui.screen.InsightsScreen
import com.itsjeel01.finsiblefrontend.feature.settings.ui.screen.SettingsScreen

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