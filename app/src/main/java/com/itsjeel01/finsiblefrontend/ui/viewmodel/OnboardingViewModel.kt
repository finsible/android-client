package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.itsjeel01.finsiblefrontend.data.client.OnboardingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {
    private val _currentSlide = MutableStateFlow(0) // Initial slide state
    val currentSlide: StateFlow<Int> = _currentSlide
    val slides = OnboardingData().getOnboardingData()

    fun nextSlide() {
        if (_currentSlide.value < slides.size - 1) _currentSlide.value++
    }

    fun previousSlide() {
        if (_currentSlide.value > 0) _currentSlide.value--
    }
}