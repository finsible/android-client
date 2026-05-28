package com.itsjeel01.finsiblefrontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.itsjeel01.finsiblefrontend.ui.component.templates.util.FinsibleLoaderHost
import com.itsjeel01.finsiblefrontend.ui.component.templates.util.FinsibleLoaderManager
import com.itsjeel01.finsiblefrontend.ui.component.templates.util.FinsibleNotificationHost
import com.itsjeel01.finsiblefrontend.ui.component.templates.util.FinsibleNotificationManager
import com.itsjeel01.finsiblefrontend.ui.component.templates.util.LocalFinsibleLoader
import com.itsjeel01.finsiblefrontend.ui.component.templates.util.LocalFinsibleNotification
import com.itsjeel01.finsiblefrontend.ui.navigation.NavigationRoot
import com.itsjeel01.finsiblefrontend.ui.navigation.Route
import com.itsjeel01.finsiblefrontend.ui.navigation.StartDestinationResolver
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var startDestinationResolver: StartDestinationResolver

    @Inject
    lateinit var finsibleLoaderManager: FinsibleLoaderManager

    @Inject
    lateinit var finsibleNotificationManager: FinsibleNotificationManager

    companion object {
        private var hasShownTestScreen = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val startDestination = startDestinationResolver.resolveStartDestination(hasShownTestScreen)
        if (startDestination == Route.Test) {
            hasShownTestScreen = true
        }

        setContent {
            CompositionLocalProvider(
                LocalFinsibleLoader provides finsibleLoaderManager,
                LocalFinsibleNotification provides finsibleNotificationManager,
            ) {
                FinsibleTheme {
                    FinsibleLoaderHost(finsibleLoaderManager = finsibleLoaderManager) {
                        FinsibleNotificationHost(notificationManager = finsibleNotificationManager) {
                            NavigationRoot(startDestination = startDestination)
                        }
                    }
                }
            }
        }
    }
}