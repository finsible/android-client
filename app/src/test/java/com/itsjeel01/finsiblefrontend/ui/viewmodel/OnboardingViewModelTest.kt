package com.itsjeel01.finsiblefrontend.ui.viewmodel

import com.google.common.truth.Truth.assertThat
import com.itsjeel01.finsiblefrontend.MainDispatcherRule
import org.junit.Rule
import org.junit.Test

class OnboardingViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val viewModel = OnboardingViewModel()

    @Test
    fun `initial carousel item is 0`() {
        assertThat(viewModel.currentCarouselItem.value).isEqualTo(0)
    }

    @Test
    fun `nextCarouselItem advances to next page`() {
        viewModel.nextCarouselItem()
        assertThat(viewModel.currentCarouselItem.value).isEqualTo(1)

        viewModel.nextCarouselItem()
        assertThat(viewModel.currentCarouselItem.value).isEqualTo(2)
    }

    @Test
    fun `nextCarouselItem does not advance beyond last page`() {
        viewModel.skipToLastCarouselItem()
        val lastIndex = viewModel.currentCarouselItem.value

        viewModel.nextCarouselItem()
        assertThat(viewModel.currentCarouselItem.value).isEqualTo(lastIndex)
    }

    @Test
    fun `previousCarouselItem goes back`() {
        viewModel.nextCarouselItem()
        assertThat(viewModel.currentCarouselItem.value).isEqualTo(1)

        viewModel.previousCarouselItem()
        assertThat(viewModel.currentCarouselItem.value).isEqualTo(0)
    }

    @Test
    fun `previousCarouselItem does not go below 0`() {
        viewModel.previousCarouselItem()
        assertThat(viewModel.currentCarouselItem.value).isEqualTo(0)
    }

    @Test
    fun `skipToLastCarouselItem goes to last page`() {
        viewModel.skipToLastCarouselItem()

        assertThat(viewModel.isLastCarouselItem()).isTrue()
    }

    @Test
    fun `isLastCarouselItem returns false for non-last pages`() {
        assertThat(viewModel.isLastCarouselItem()).isFalse()

        viewModel.nextCarouselItem()
        assertThat(viewModel.isLastCarouselItem()).isFalse()
    }
}
