package com.itsjeel01.finsiblefrontend.ui.screen

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.itsjeel01.finsiblefrontend.ui.component.BottomNavBar
import com.itsjeel01.finsiblefrontend.ui.navigation.DashboardNavHost

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
