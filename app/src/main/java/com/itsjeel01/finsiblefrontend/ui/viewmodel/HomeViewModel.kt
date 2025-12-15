package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * ViewModel for managing navigation state in the Home screen.
 * 
 * Lifecycle Scope: Screen-scoped to HomeScreen composable.
 * - Created when HomeScreen is first composed
 * - Survives tab navigation within HomeScreen
 * - Cleared when user navigates away from HomeScreen (e.g., logout)
 * - State persisted via SavedStateHandle across process death
 * 
 * State Management:
 * - activeTab: Current selected tab index
 * - previousTab: Previously selected tab index (for back navigation from modal screens)
 * 
 * Memory Optimization:
 * - Uses SavedStateHandle for automatic state restoration
 * - Cleared automatically when HomeScreen is removed from back stack
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val ACTIVE_TAB_KEY = "active_tab"
        private const val PREVIOUS_TAB_KEY = "previous_tab"
        private const val DEFAULT_TAB = 0
    }

    private val _activeTab = MutableStateFlow(
        savedStateHandle.get<Int>(ACTIVE_TAB_KEY) ?: DEFAULT_TAB
    )
    val activeTab: StateFlow<Int> = _activeTab.asStateFlow()

    private val _previousTab = MutableStateFlow(
        savedStateHandle.get<Int>(PREVIOUS_TAB_KEY) ?: DEFAULT_TAB
    )
    val previousTab: StateFlow<Int> = _previousTab.asStateFlow()

    fun updateActiveTab(index: Int) {
        if (_activeTab.value != index) {
            _previousTab.value = _activeTab.value
            savedStateHandle[PREVIOUS_TAB_KEY] = _activeTab.value

            _activeTab.value = index
            savedStateHandle[ACTIVE_TAB_KEY] = index
        }
    }
}