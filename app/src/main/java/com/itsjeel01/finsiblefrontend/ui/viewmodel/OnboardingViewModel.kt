package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.itsjeel01.finsiblefrontend.ui.data.OnboardingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {

    // --- State ---

    private val _currentItem = MutableStateFlow(0)
    val currentItem: StateFlow<Int> = _currentItem

    val carouselItems = OnboardingData().get()

    // --- Actions ---

    fun nextItem() {
        if (_currentItem.value < carouselItems.size - 1) _currentItem.value++
    }

    fun previousItem() {
        if (_currentItem.value > 0) _currentItem.value--
    }

    fun isLastItem(): Boolean {
        return _currentItem.value == carouselItems.size - 1
    }
}
