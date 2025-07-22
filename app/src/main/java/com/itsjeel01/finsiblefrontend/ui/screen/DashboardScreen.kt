package com.itsjeel01.finsiblefrontend.ui.screen

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.itsjeel01.finsiblefrontend.ui.component.NavigationBar
import com.itsjeel01.finsiblefrontend.ui.navigation.DashboardNavHost

@Composable
fun DashboardScreen(navHostController: NavHostController) {

    val dashboardNavController = rememberNavController()

    Scaffold(
        bottomBar = { NavigationBar(navController = dashboardNavController) }
    ) { innerPadding ->
        DashboardNavHost(
            dashboardNavController = dashboardNavController,
            navHostController = navHostController,
            paddingValues = innerPadding
        )
    }
}
