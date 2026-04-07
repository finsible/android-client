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
import com.itsjeel01.finsiblefrontend.ui.screen.playground.ComponentPlaygroundEntryScreen
import com.itsjeel01.finsiblefrontend.ui.screen.playground.ComponentPlaygroundListScreen
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
                            onNavigateToPlayground = {
                                rootBackStack.add(Route.Playground)
                            },
                            viewModel = viewModel
                        )
                    }

                    entry<Route.Playground> {
                        ComponentPlaygroundListScreen(
                            onBack = { popLast(rootBackStack) },
                            onSelect = { route -> rootBackStack.add(route) }
                        )
                    }

                    entry<Route.Playground.Accordion> {
                        ComponentPlaygroundEntryScreen(Route.Playground.Accordion) { popLast(rootBackStack) }
                    }
                    entry<Route.Playground.Button> {
                        ComponentPlaygroundEntryScreen(Route.Playground.Button) { popLast(rootBackStack) }
                    }
                    entry<Route.Playground.Checkbox> {
                        ComponentPlaygroundEntryScreen(Route.Playground.Checkbox) { popLast(rootBackStack) }
                    }
                    entry<Route.Playground.ChipsRow> {
                        ComponentPlaygroundEntryScreen(Route.Playground.ChipsRow) { popLast(rootBackStack) }
                    }
                    entry<Route.Playground.DatePicker> {
                        ComponentPlaygroundEntryScreen(Route.Playground.DatePicker) { popLast(rootBackStack) }
                    }
                    entry<Route.Playground.DateRangePicker> {
                        ComponentPlaygroundEntryScreen(Route.Playground.DateRangePicker) { popLast(rootBackStack) }
                    }
                    entry<Route.Playground.Dropdown> {
                        ComponentPlaygroundEntryScreen(Route.Playground.Dropdown) { popLast(rootBackStack) }
                    }
                    entry<Route.Playground.FilterChip> {
                        ComponentPlaygroundEntryScreen(Route.Playground.FilterChip) { popLast(rootBackStack) }
                    }
                    entry<Route.Playground.IconBadge> {
                        ComponentPlaygroundEntryScreen(Route.Playground.IconBadge) { popLast(rootBackStack) }
                    }
                    entry<Route.Playground.MonthYearPicker> {
                        ComponentPlaygroundEntryScreen(Route.Playground.MonthYearPicker) { popLast(rootBackStack) }
                    }
                    entry<Route.Playground.RadioButton> {
                        ComponentPlaygroundEntryScreen(Route.Playground.RadioButton) { popLast(rootBackStack) }
                    }
                    entry<Route.Playground.Scrubber> {
                        ComponentPlaygroundEntryScreen(Route.Playground.Scrubber) { popLast(rootBackStack) }
                    }
                    entry<Route.Playground.SegmentedButtons> {
                        ComponentPlaygroundEntryScreen(Route.Playground.SegmentedButtons) { popLast(rootBackStack) }
                    }
                    entry<Route.Playground.Text> {
                        ComponentPlaygroundEntryScreen(Route.Playground.Text) { popLast(rootBackStack) }
                    }
                    entry<Route.Playground.TextField> {
                        ComponentPlaygroundEntryScreen(Route.Playground.TextField) { popLast(rootBackStack) }
                    }
                    entry<Route.Playground.TileCards> {
                        ComponentPlaygroundEntryScreen(Route.Playground.TileCards) { popLast(rootBackStack) }
                    }
                    entry<Route.Playground.Toggle> {
                        ComponentPlaygroundEntryScreen(Route.Playground.Toggle) { popLast(rootBackStack) }
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

private fun <T> popLast(backStack: MutableList<T>) {
    if (backStack.size > 1) {
        backStack.removeAt(backStack.lastIndex)
    }
}
