package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val ACTIVE_TAB_KEY = "active_tab"
        private const val DEFAULT_TAB = 0
    }

    private val _activeTab = MutableStateFlow(
        savedStateHandle.get<Int>(ACTIVE_TAB_KEY) ?: DEFAULT_TAB
    )
    val activeTab: StateFlow<Int> = _activeTab.asStateFlow()

    fun updateActiveTab(index: Int) {
        if (_activeTab.value != index) {
            _activeTab.value = index
            savedStateHandle[ACTIVE_TAB_KEY] = index
        }
    }
}