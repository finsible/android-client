package com.itsjeel01.finsiblefrontend.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.itsjeel01.finsiblefrontend.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {

    private val _currentCarouselItem = MutableStateFlow(0)
    val currentCarouselItem: StateFlow<Int> = _currentCarouselItem

    private val carouselItems = CarouselItems().get()

    fun nextCarouselItem() {
        if (_currentCarouselItem.value < carouselItems.size - 1) _currentCarouselItem.value++
        Log.d(TAG, "Next Carousel Item: ${_currentCarouselItem.value}")
    }

    fun previousCarouselItem() {
        if (_currentCarouselItem.value > 0) _currentCarouselItem.value--
        Log.d(TAG, "Previous Carousel Item: ${_currentCarouselItem.value}")
    }

    fun skipToLastCarouselItem() {
        _currentCarouselItem.value = carouselItems.size - 1
        Log.d(TAG, "Skip to Last Carousel Item: ${_currentCarouselItem.value}")
    }

    fun isLastCarouselItem(): Boolean {
        return _currentCarouselItem.value == carouselItems.size - 1
    }

    data class CarouselItems(
        val illustration: Int = 0,
        val headline: String = "",
        val description: String = "",
    ) {
        fun get(): List<CarouselItems> {
            return listOf(
                CarouselItems(
                    illustration = R.drawable.ill_welcome,
                    headline = "Welcome to Finsible",
                    description = "We make finance sensible and stress-free. Start your journey to financial clarity and transform how you manage money."
                ),
                CarouselItems(
                    illustration = R.drawable.ill_track,
                    headline = "Never Lose Track Again",
                    description = "Capture transactions in seconds and see your spending clearly. Understand your money habits without the headache."
                ),
                CarouselItems(
                    illustration = R.drawable.ill_audit,
                    headline = "Your money, decoded!",
                    description = "Turn financial data into insights. Set goals, get personalized tips, and reach them faster with advice in your pocket."
                ),
                CarouselItems(
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