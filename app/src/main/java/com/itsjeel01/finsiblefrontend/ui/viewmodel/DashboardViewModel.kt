package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {

    // --- State ---

    private val _tabIdx = MutableStateFlow(0)
    val tabIdx: StateFlow<Int> = _tabIdx

    // --- Actions ---

    fun changeTab(index: Int) {
        _tabIdx.value = index
    }
}
