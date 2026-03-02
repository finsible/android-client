package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {

    private val carouselItems = CarouselItems().get()

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState

    fun nextCarouselItem() {
        if (_uiState.value.currentItem < carouselItems.size - 1) {
            _uiState.update { current ->
                val next = current.currentItem + 1
                OnboardingUiState(currentItem = next, isLastItem = next == carouselItems.lastIndex)
            }
            Logger.UI.d("Next Carousel Item: ${_uiState.value.currentItem}")
        }
    }

    fun previousCarouselItem() {
        if (_uiState.value.currentItem > 0) {
            _uiState.update { current ->
                val prev = current.currentItem - 1
                OnboardingUiState(currentItem = prev, isLastItem = prev == carouselItems.lastIndex)
            }
            Logger.UI.d("Previous Carousel Item: ${_uiState.value.currentItem}")
        }
    }

    fun skipToLastCarouselItem() {
        val last = carouselItems.lastIndex
        _uiState.update { OnboardingUiState(currentItem = last, isLastItem = true) }
        Logger.UI.d("Skip to Last Carousel Item: $last")
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

/** UI state for the onboarding carousel. */
@Immutable
data class OnboardingUiState(
    val currentItem: Int = 0,
    val isLastItem: Boolean = false
)