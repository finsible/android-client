package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
    startDestination: Route = Route.Launch
) {
    val rootBackStack = rememberNavBackStack(startDestination)

    NavDisplay(
        modifier = modifier
            .fillMaxSize()
            .background(FinsibleTheme.semantic.surfaceBase),
        backStack = rootBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider =
            entryProvider {
                entry<Route.Launch> { LaunchRoute(rootBackStack) }
                entry<Route.Onboarding> { OnboardingRoute(rootBackStack) }
                entry<Route.Home> { HomeRoute() }
            }
    )
}

