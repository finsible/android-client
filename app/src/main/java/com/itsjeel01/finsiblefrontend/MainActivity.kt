package com.itsjeel01.finsiblefrontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.itsjeel01.finsiblefrontend.common.TestPreferenceManager
import com.itsjeel01.finsiblefrontend.data.di.hiltLoadingManager
import com.itsjeel01.finsiblefrontend.data.di.hiltNotificationManager
import com.itsjeel01.finsiblefrontend.ui.inappnotification.NotificationHost
import com.itsjeel01.finsiblefrontend.ui.loading.LoadingIndicatorHost
import com.itsjeel01.finsiblefrontend.ui.navigation.AppRoutes
import com.itsjeel01.finsiblefrontend.ui.navigation.appNavGraph
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var testPrefs: TestPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            // Determine start destination based on build type and debug preferences
            val startDestination = if (BuildConfig.DEBUG && !testPrefs.shouldSkipDebugScreen()) {
                AppRoutes.Test
            } else {
                AppRoutes.Launch
            }

            FinsibleTheme {
                LoadingIndicatorHost(loadingIndicatorManager = hiltLoadingManager()) {
                    NotificationHost(notificationManager = hiltNotificationManager()) {
                        NavHost(
                            navController = navController,
                            startDestination = startDestination
                        ) {
                            appNavGraph(navController)
                        }
                    }
                }
            }
        }
    }
}