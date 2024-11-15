package com.itsjeel01.finsiblefrontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleAppTheme
import com.itsjeel01.finsiblefrontend.ui.view.AuthScreen
import com.itsjeel01.finsiblefrontend.ui.view.DashboardScreen
import com.itsjeel01.finsiblefrontend.ui.navigation.Routes
import com.itsjeel01.finsiblefrontend.ui.navigation.dashboardNavGraph
import com.itsjeel01.finsiblefrontend.ui.navigation.onboardingNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinsibleAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Routes.AuthScreen) {
                    onboardingNavGraph()
                    dashboardNavGraph()
                }
            }
        }
    }
}