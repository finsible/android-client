package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.navigation3.runtime.NavKey

class NewTransactionNavigator(
    val state: NewTransactionNavState
) {
    fun navigate(route: NavKey) {
        state.backStack.add(route)
    }

    fun back(onExit: () -> Unit) {
        if (state.backStack.size > 1) {
            state.backStack.removeAt(state.backStack.lastIndex)
        } else {
            onExit()
        }
    }

    fun next(onComplete: () -> Unit) {
        val currentIndex = state.currentStepIndex
        if (currentIndex < NewTransactionSteps.lastIndex) {
            navigate(NewTransactionSteps[currentIndex + 1])
        } else {
            onComplete()
        }
    }
}