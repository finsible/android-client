package com.itsjeel01.finsiblefrontend.ui.view.screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.itsjeel01.finsiblefrontend.ui.navigation.DashboardNavHost
import com.itsjeel01.finsiblefrontend.ui.view.components.BottomNavBar

@Composable
fun DashboardScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { innerPadding ->
        DashboardNavHost(navController = navController, paddingValues = innerPadding)
    }
}

@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(rememberNavController())
}