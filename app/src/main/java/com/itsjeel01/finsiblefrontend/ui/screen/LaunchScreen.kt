package com.itsjeel01.finsiblefrontend.ui.screen

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.data.di.hiltLoadingManager
import com.itsjeel01.finsiblefrontend.ui.model.AuthState
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AuthViewModel

private val Tag = "LaunchScreen"

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
                Log.d(Tag, "AuthState = Negative; Navigating to Onboarding")
                navigateToOnboarding()
            }

            AuthState.Positive -> {
                Log.d(Tag, "AuthState = Positive; Navigating to Dashboard")
                navigateToDashboard()
            }

            else -> {
                Log.d(Tag, "AuthState = Loading")
            }
        }
    }

    if (authState is AuthState.Loading) {
        hiltLoadingManager().show()
    }
}