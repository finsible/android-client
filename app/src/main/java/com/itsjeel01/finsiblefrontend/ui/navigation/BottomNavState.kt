package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator

class BottomNavState(
    val startRoute: Route,
    activeTab: MutableState<Route>,
    val tabsBackStacks: Map<Route, NavBackStack<Route>>
) {
    var activeTab by activeTab

    val backStack: List<Route>
        get() = if (activeTab == startRoute) listOf(startRoute)
        else listOf(startRoute, activeTab)
}

@Composable
fun rememberBottomNavState(
    startRoute: Route,
    bottomTabs: Set<Route>
): BottomNavState {
    val activeTab = rememberSerializable(startRoute, bottomTabs) {
        mutableStateOf(startRoute)
    }

    val backStacks = bottomTabs.associateWith { key ->
        @Suppress("UNCHECKED_CAST")
        rememberNavBackStack(key) as NavBackStack<Route>
    }

    return remember(startRoute, bottomTabs) {
        BottomNavState(startRoute, activeTab, backStacks)
    }
}

@Composable
fun BottomNavState.toEntries(
    entryProvider: (Route) -> NavEntry<Route>
): SnapshotStateList<NavEntry<Route>> {
    val decoratedEntries = tabsBackStacks.mapValues { (_, stack) ->
        val decorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator<Route>(),
            rememberViewModelStoreNavEntryDecorator()
        )
        rememberDecoratedNavEntries(
            backStack = stack,
            entryProvider = entryProvider,
            entryDecorators = decorators
        )
    }

    return backStack
        .flatMap { decoratedEntries[it] ?: emptyList() }
        .toMutableStateList()
}