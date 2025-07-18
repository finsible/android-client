package com.itsjeel01.finsiblefrontend.ui.screen

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.itsjeel01.finsiblefrontend.ui.component.NavigationBar
import com.itsjeel01.finsiblefrontend.ui.navigation.DashboardNavHost

@Composable
fun DashboardScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = { NavigationBar(navController = navController) }
    ) { innerPadding ->
        DashboardNavHost(navController = navController, paddingValues = innerPadding)
    }
}
