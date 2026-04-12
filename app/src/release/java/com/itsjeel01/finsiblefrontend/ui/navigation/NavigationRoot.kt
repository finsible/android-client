package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.itsjeel01.finsiblefrontend.ui.screen.Launch
import com.itsjeel01.finsiblefrontend.ui.screen.Onboarding
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AuthViewModel
import com.itsjeel01.finsiblefrontend.ui.viewmodel.OnboardingViewModel

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
    startDestination: Route = Route.Launch
) {
    val rootBackStack = rememberNavBackStack(startDestination)

    NavDisplay(
        modifier = modifier
            .fillMaxSize()
            .background(FinsibleTheme.colors.primaryBackground),
        backStack = rootBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider =
            entryProvider {
                entry<Route.Launch> {
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

                entry<Route.Onboarding> {
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
                entry<Route.Home> {
                    NavigationHome()
                }
            }
    )
}

