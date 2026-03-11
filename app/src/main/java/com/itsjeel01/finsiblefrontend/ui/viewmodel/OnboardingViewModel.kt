package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {

    private val _currentCarouselItem = MutableStateFlow(0)
    val currentCarouselItem: StateFlow<Int> = _currentCarouselItem.asStateFlow()

    private val carouselItems = CarouselItem.entries

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

    /** Onboarding carousel items backed by string resources for localization. */
    enum class CarouselItem(
        @DrawableRes val illustration: Int,
        @StringRes val headline: Int,
        @StringRes val description: Int,
    ) {
        WELCOME(R.drawable.ill_welcome, R.string.onboarding_headline_welcome, R.string.onboarding_description_welcome),
        TRACK(R.drawable.ill_track, R.string.onboarding_headline_track, R.string.onboarding_description_track),
        AUDIT(R.drawable.ill_audit, R.string.onboarding_headline_audit, R.string.onboarding_description_audit),
        AUTH(R.drawable.ill_auth, R.string.onboarding_headline_auth, R.string.onboarding_description_auth);
    }
}