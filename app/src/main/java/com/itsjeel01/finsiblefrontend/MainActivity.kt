package com.itsjeel01.finsiblefrontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.itsjeel01.finsiblefrontend.data.di.hiltNotificationManager
import com.itsjeel01.finsiblefrontend.ui.navigation.InAppNotificationHost
import com.itsjeel01.finsiblefrontend.ui.navigation.Routes
import com.itsjeel01.finsiblefrontend.ui.navigation.launchNavGraph
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            FinsibleAppTheme {
                InAppNotificationHost(
                    inAppNotificationManager = hiltNotificationManager()
                ) {
                    NavHost(navController = navController, startDestination = Routes.LaunchScreen) {
                        launchNavGraph(navController)
                    }
                }
            }
        }
    }
}