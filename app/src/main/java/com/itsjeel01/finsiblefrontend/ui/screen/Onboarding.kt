package com.itsjeel01.finsiblefrontend.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.di.hiltLoadingManager
import com.itsjeel01.finsiblefrontend.data.di.hiltNotificationManager
import com.itsjeel01.finsiblefrontend.ui.component.onboarding.OnboardingBackground
import com.itsjeel01.finsiblefrontend.ui.component.onboarding.OnboardingContent
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.model.AuthState
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AuthViewModel
import com.itsjeel01.finsiblefrontend.ui.viewmodel.OnboardingViewModel

@Composable
fun Onboarding(
    navigateToHome: () -> Unit,
    onboardingViewModel: OnboardingViewModel,
    authViewModel: AuthViewModel
) {
    val inAppNotificationManager = hiltNotificationManager()
    val loadingManager = hiltLoadingManager()
    val context = LocalContext.current

    val carouselItems = remember { OnboardingViewModel.CarouselItem.entries }
    val currentItem by onboardingViewModel.currentCarouselItem.collectAsStateWithLifecycle()
    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val pleaseWait = stringResource(R.string.please_wait)
    val authFailed = stringResource(R.string.authentication_failed)

    LaunchedEffect(authState) {
        loadingManager.hide()

        when (authState) {
            is AuthState.Positive -> {
                Logger.UI.d("AuthState = Positive; navigating to dashboard")
                navigateToHome()
            }

            is AuthState.Loading -> {
                Logger.UI.d("AuthState = Loading")
                loadingManager.show(pleaseWait)
            }

            is AuthState.Negative -> {
                val authState = authState as AuthState.Negative

                if (authState.isFailed) {
                    Logger.UI.d("Auth status: ${authState.message}")

                    inAppNotificationManager.showError(
                        title = authFailed,
                        subtitle = authState.message,
                        autoDismiss = true,
                        autoDismissDelay = Duration.MS_3000,
                    )
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        OnboardingBackground()

        OnboardingContent(
            currentItem = currentItem,
            carouselItems = carouselItems,
            isLastItem = onboardingViewModel::isLastCarouselItem,
            onNextItem = onboardingViewModel::nextCarouselItem,
            onPreviousItem = onboardingViewModel::previousCarouselItem,
            onSkip = onboardingViewModel::skipToLastCarouselItem,
            onGoogleLogin = { authViewModel.signInWithGoogle(context) }
        )
    }
}