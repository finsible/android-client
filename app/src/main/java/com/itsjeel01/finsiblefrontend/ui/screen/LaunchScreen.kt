package com.itsjeel01.finsiblefrontend.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.di.hiltLoadingManager
import com.itsjeel01.finsiblefrontend.ui.model.AuthState
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AuthViewModel

@Composable
fun LaunchScreen(
    navigateToOnboarding: () -> Unit,
    navigateToDashboard: () -> Unit,
) {

    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Negative -> {
                Logger.UI.d("AuthState = Negative; Navigating to Onboarding")
                navigateToOnboarding()
            }

            AuthState.Positive -> {
                Logger.UI.d("AuthState = Positive; Navigating to Dashboard")
                navigateToDashboard()
            }

            else -> {
                Logger.UI.d("AuthState = Loading")
            }
        }
    }

    if (authState is AuthState.Loading) {
        hiltLoadingManager().show()
    }
}