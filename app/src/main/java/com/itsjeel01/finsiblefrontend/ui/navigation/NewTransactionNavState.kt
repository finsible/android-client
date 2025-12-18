package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator

val NewTransactionSteps = listOf(
    Route.Home.NewTransaction.Amount,
    Route.Home.NewTransaction.Date,
    Route.Home.NewTransaction.Category,
    Route.Home.NewTransaction.Accounts,
    Route.Home.NewTransaction.Description
)

class NewTransactionNavState(
    val backStack: NavBackStack<NavKey>
) {
    val currentStep: NavKey
        get() = backStack.last()

    val currentStepIndex: Int
        get() = NewTransactionSteps.indexOf(currentStep).takeIf { it != -1 } ?: 0

    val totalSteps: Int = NewTransactionSteps.size
}

@Composable
fun rememberNewTransactionNavState(
    startDestination: NavKey = Route.Home.NewTransaction.Amount
): NewTransactionNavState {
    val backStack = rememberNavBackStack(startDestination)
    return remember(backStack) {
        NewTransactionNavState(backStack)
    }
}

@Composable
fun NewTransactionNavState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>
): SnapshotStateList<NavEntry<NavKey>> {
    val decorators = listOf(
        rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
        rememberViewModelStoreNavEntryDecorator()
    )
    return rememberDecoratedNavEntries(
        backStack = backStack,
        entryProvider = entryProvider,
        entryDecorators = decorators
    ).toMutableStateList()
}
