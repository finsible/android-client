package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.itsjeel01.finsiblefrontend.ui.view.screens.DashboardScreen

fun NavGraphBuilder.dashboardNavGraph() {
    composable<Routes.DashboardScreen> {
        DashboardScreen()
    }
}