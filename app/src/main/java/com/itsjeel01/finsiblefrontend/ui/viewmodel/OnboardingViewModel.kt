package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.ui.model.CarouselItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {

    private val _currentCarouselItem = MutableStateFlow(0)
    val currentCarouselItem: StateFlow<Int> = _currentCarouselItem

    /** Carousel items data for onboarding screens. */
    val carouselItems: List<CarouselItem> = CarouselItem.getAll()

    fun nextCarouselItem() {
        if (_currentCarouselItem.value < carouselItems.size - 1) _currentCarouselItem.value++
        Logger.UI.d("Next Carousel Item: ${_currentCarouselItem.value}")
    }

    fun previousCarouselItem() {
        if (_currentCarouselItem.value > 0) _currentCarouselItem.value--
        Logger.UI.d("Previous Carousel Item: ${_currentCarouselItem.value}")
    }

    fun skipToLastCarouselItem() {
        _currentCarouselItem.value = carouselItems.size - 1
        Logger.UI.d("Skip to Last Carousel Item: ${_currentCarouselItem.value}")
    }

    fun isLastCarouselItem(): Boolean {
        return _currentCarouselItem.value == carouselItems.size - 1
    }

    companion object {
        const val TAG = "OnboardingViewModel"
    }
}