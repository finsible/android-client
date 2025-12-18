package com.itsjeel01.finsiblefrontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.itsjeel01.finsiblefrontend.common.TestPreferenceManager
import com.itsjeel01.finsiblefrontend.data.di.hiltLoadingManager
import com.itsjeel01.finsiblefrontend.data.di.hiltNotificationManager
import com.itsjeel01.finsiblefrontend.ui.inappnotification.NotificationHost
import com.itsjeel01.finsiblefrontend.ui.loading.LoadingIndicatorHost
import com.itsjeel01.finsiblefrontend.ui.navigation.NavigationRoot
import com.itsjeel01.finsiblefrontend.ui.navigation.Route
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var testPrefs: TestPreferenceManager

    companion object {
        private const val KEY_HAS_SHOWN_TEST_SCREEN = "has_shown_test_screen"
    }

    private var hasShownTestScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        hasShownTestScreen = savedInstanceState?.getBoolean(KEY_HAS_SHOWN_TEST_SCREEN, false) ?: false

        val startDestination = if (BuildConfig.DEBUG) {
            if (!testPrefs.shouldSkipDebugScreen() && !hasShownTestScreen) {
                hasShownTestScreen = true
                Route.Test
            } else {
                Route.Launch
            }
        } else {
            Route.Launch
        }

        setContent {
            FinsibleTheme {
                LoadingIndicatorHost(loadingIndicatorManager = hiltLoadingManager()) {
                    NotificationHost(notificationManager = hiltNotificationManager()) {
                        NavigationRoot(startDestination = startDestination)
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_HAS_SHOWN_TEST_SCREEN, hasShownTestScreen)
    }
}