package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Navigation event system for handling navigation results and one-time events.
 * 
 * This class provides a mechanism for screens to send results back to previous screens,
 * similar to startActivityForResult but for Compose Navigation.
 * 
 * Example usage:
 * 
 * // In source screen (that navigates)
 * val navigationEvents = NavigationEvents(navController)
 * 
 * navigationEvents.observeResult<TransactionData>("transaction_result") { data ->
 *     // Handle the result from NewTransactionScreen
 *     viewModel.onTransactionCreated(data)
 * }
 * 
 * // In destination screen (that returns result)
 * val navigationEvents = NavigationEvents(navController)
 * 
 * navigationEvents.setResult("transaction_result", transactionData)
 * navigationEvents.navigateBack()
 */
class NavigationEvents(private val navController: NavController) {
    
    companion object {
        private const val RESULT_KEY_PREFIX = "nav_result_"
    }
    
    /**
     * Set a result for the previous destination.
     * 
     * @param key Unique key for this result
     * @param result The result data to pass back
     */
    fun <T> setResult(key: String, result: T) {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.set(resultKey(key), result)
    }
    
    /**
     * Observe results from destinations navigated to.
     * 
     * @param key Unique key for this result
     * @param onResult Callback invoked when result is available
     */
    fun <T> observeResult(
        key: String,
        savedStateHandle: SavedStateHandle,
        onResult: (T) -> Unit
    ) {
        savedStateHandle.getLiveData<T>(resultKey(key)).observeForever { result ->
            result?.let {
                onResult(it)
                // Clear the result after consuming
                savedStateHandle.remove<T>(resultKey(key))
            }
        }
    }
    
    /**
     * Navigate back to the previous destination.
     */
    fun navigateBack(): Boolean {
        return navController.popBackStack()
    }
    
    /**
     * Set result and navigate back in a single call.
     */
    fun <T> setResultAndNavigateBack(key: String, result: T): Boolean {
        setResult(key, result)
        return navigateBack()
    }
    
    private fun resultKey(key: String) = "$RESULT_KEY_PREFIX$key"
}

/**
 * One-time navigation event holder for ViewModels.
 * 
 * Use this to emit navigation events from ViewModels that should only be consumed once.
 * 
 * Example:
 * ```kotlin
 * @HiltViewModel
 * class MyViewModel @Inject constructor() : ViewModel() {
 *     private val _navigationEvents = NavigationEventChannel<MyNavEvent>()
 *     val navigationEvents = _navigationEvents.events
 *     
 *     fun onSuccess() {
 *         _navigationEvents.send(MyNavEvent.NavigateToHome)
 *     }
 * }
 * 
 * @Composable
 * fun MyScreen(viewModel: MyViewModel = hiltViewModel()) {
 *     LaunchedEffect(Unit) {
 *         viewModel.navigationEvents.collect { event ->
 *             when (event) {
 *                 is MyNavEvent.NavigateToHome -> navController.navigate(AppRoutes.Home)
 *             }
 *         }
 *     }
 * }
 * ```
 */
class NavigationEventChannel<T> {
    private val _events = MutableSharedFlow<T>(replay = 0, extraBufferCapacity = 1)
    val events: SharedFlow<T> = _events.asSharedFlow()
    
    fun send(event: T) {
        _events.tryEmit(event)
    }
}

/**
 * Sealed class for common navigation events.
 * 
 * Extend this in your ViewModels to define navigation events.
 */
sealed class NavigationEvent {
    /** Navigate back to previous screen */
    data object NavigateBack : NavigationEvent()
    
    /** Navigate to a specific destination */
    data class NavigateTo(val destination: Any) : NavigationEvent()
    
    /** Navigate with clearing back stack */
    data class NavigateAndClearBackStack(
        val destination: Any,
        val clearUpTo: Any
    ) : NavigationEvent()
}
