package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.logging.Logger
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

    data class CarouselItem(
        val illustration: Int,
        val headline: String,
        val description: String
    ) {
        companion object {
            /** Returns all carousel items for onboarding. */
            fun getAll(): List<CarouselItem> = listOf(
                CarouselItem(
                    illustration = R.drawable.ill_welcome,
                    headline = "Welcome to Finsible",
                    description = "We make finance sensible and stress-free. Start your journey to financial clarity and transform how you manage money."
                ),
                CarouselItem(
                    illustration = R.drawable.ill_track,
                    headline = "Never Lose Track Again",
                    description = "Capture transactions in seconds and see your spending clearly. Understand your money habits without the headache."
                ),
                CarouselItem(
                    illustration = R.drawable.ill_audit,
                    headline = "Your money, decoded!",
                    description = "Turn financial data into insights. Set goals, get personalized tips, and reach them faster with advice in your pocket."
                ),
                CarouselItem(
                    illustration = R.drawable.ill_auth,
                    headline = "Let's Get You Set Up",
                    description = "Never lose your data, access from anywhere. Sign in with Google and start making your finances more sensible."
                )
            )
        }
    }

    companion object {
        const val TAG = "OnboardingViewModel"
    }
}