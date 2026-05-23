package com.itsjeel01.finsiblefrontend.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.ui.component.templates.util.LocalFinsibleLoader
import com.itsjeel01.finsiblefrontend.ui.model.state.AuthState
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AuthViewModel

@Composable
fun Launch(
    navigateToOnboarding: () -> Unit,
    navigateToApp: () -> Unit,
    authViewModel: AuthViewModel
) {
    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val loadingManager = LocalFinsibleLoader.current

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Loading -> {
                Logger.UI.d("AuthState = Loading")
                loadingManager.show()
            }

            is AuthState.Negative -> {
                loadingManager.hide()
                Logger.UI.d("AuthState = Negative; Navigating to Onboarding")
                navigateToOnboarding()
            }

            AuthState.Positive -> {
                loadingManager.hide()
                Logger.UI.d("AuthState = Positive; Navigating to Dashboard")
                navigateToApp()
            }
        }
    }
}
