package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {
    private val _currentSlide = MutableStateFlow(0) // Initial slide state
    val currentSlide: StateFlow<Int> = _currentSlide

    fun updateSlide(newSlide: Int) {
        _currentSlide.value = newSlide
    }
}