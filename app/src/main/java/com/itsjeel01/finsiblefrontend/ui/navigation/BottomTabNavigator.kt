package com.itsjeel01.finsiblefrontend.ui.navigation

import com.itsjeel01.finsiblefrontend.common.logging.Logger

class BottomTabNavigator(val state: BottomNavState) {
    fun navigate(route: Route) {
        if (route in state.tabsBackStacks.keys) {
            state.activeTab = route
        } else {
            state.tabsBackStacks[state.activeTab]?.add(route)
        }
    }

    fun goBack() {
        val currentStack = state.tabsBackStacks[state.activeTab] ?: run {
            Logger.App.e("No back stack found for active tab: ${state.activeTab}")
            throw IllegalStateException("No back stack found for active tab: ${state.activeTab}")
        }

        val currentRoute = currentStack.last()
        if (currentRoute == state.activeTab) {
            state.activeTab = state.startRoute
        } else {
            currentStack.removeLastOrNull()
        }
    }
}