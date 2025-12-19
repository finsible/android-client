package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.itsjeel01.finsiblefrontend.BuildConfig
import com.itsjeel01.finsiblefrontend.ui.screen.Launch
import com.itsjeel01.finsiblefrontend.ui.screen.Onboarding
import com.itsjeel01.finsiblefrontend.ui.screen.TestScreen
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AuthViewModel
import com.itsjeel01.finsiblefrontend.ui.viewmodel.OnboardingViewModel
import com.itsjeel01.finsiblefrontend.ui.viewmodel.TestViewModel

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
    startDestination: Route = Route.Launch
) {

    val rootBackStack = rememberNavBackStack(startDestination)

    NavDisplay(
        modifier = modifier,
        backStack = rootBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider =
            entryProvider {
                if (BuildConfig.DEBUG) {
                    entry<Route.Test> {
                        val viewModel: TestViewModel = hiltViewModel()
                        TestScreen(
                            onNavigateToApp = {
                                rootBackStack.clear()
                                rootBackStack.add(Route.Launch)
                            },
                            viewModel = viewModel
                        )
                    }
                }

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