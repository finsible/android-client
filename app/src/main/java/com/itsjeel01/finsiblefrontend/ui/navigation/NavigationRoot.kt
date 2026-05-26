@file:JvmName("NavigationRootSharedUtils")

package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.itsjeel01.finsiblefrontend.ui.screen.Launch
import com.itsjeel01.finsiblefrontend.ui.screen.Onboarding
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AuthViewModel
import com.itsjeel01.finsiblefrontend.ui.viewmodel.OnboardingViewModel

@Composable
fun LaunchRoute(rootBackStack: NavBackStack<NavKey>) {
    val authViewModel: AuthViewModel = hiltViewModel()

    Launch(
        navigateToOnboarding = {
            rootBackStack.clear()
            rootBackStack.add(Route.Onboarding)
        },
        navigateToApp = {
            rootBackStack.clear()
            rootBackStack.add(Route.Home)
        },
        authViewModel = authViewModel
    )
}

@Composable
fun OnboardingRoute(rootBackStack: NavBackStack<NavKey>) {
    val onboardingViewModel: OnboardingViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()

    Onboarding(
        navigateToHome = {
            rootBackStack.clear()
            rootBackStack.add(Route.Home)
        },
        onboardingViewModel = onboardingViewModel,
        authViewModel = authViewModel
    )
}

@Composable
fun HomeRoute() {
    NavigationHome()
}

fun popLast(backStack: NavBackStack<NavKey>) {
    if (backStack.size > 1) {
        backStack.removeAt(backStack.lastIndex)
    }
}
