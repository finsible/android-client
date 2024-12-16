package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {
    private val _selectedTabState = MutableStateFlow(0)
    val selectedTabState: StateFlow<Int> = _selectedTabState

    private val _showModalSheetState = MutableStateFlow(false)
    val showModalSheetState: StateFlow<Boolean> = _showModalSheetState

    fun changeTab(index: Int) {
        _selectedTabState.value = index
    }

    fun showModalSheet() {
        _showModalSheetState.value = true
    }

    fun hideModalSheet() {
        _showModalSheetState.value = false
    }
}