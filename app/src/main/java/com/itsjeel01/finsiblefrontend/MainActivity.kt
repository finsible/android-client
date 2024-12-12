package com.itsjeel01.finsiblefrontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.itsjeel01.finsiblefrontend.ui.navigation.Routes
import com.itsjeel01.finsiblefrontend.ui.navigation.dashboardNavGraph
import com.itsjeel01.finsiblefrontend.ui.navigation.launchNavGraph
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleAppTheme
import com.itsjeel01.finsiblefrontend.utils.NavControllerWrapper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinsibleAppTheme {
                val navControllerWrapper: NavControllerWrapper = hiltViewModel()
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Routes.LaunchScreen) {
                    launchNavGraph()
                    dashboardNavGraph()
                }

                navControllerWrapper.navController = navController
            }
        }
    }
}