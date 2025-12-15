package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/** Unit tests for HomeViewModel demonstrating SavedStateHandle usage and state management. */
class HomeViewModelTest {

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        savedStateHandle = SavedStateHandle()
        viewModel = HomeViewModel(savedStateHandle)
    }

    @Test
    fun `initial active tab is 0`() = runTest {
        // Then
        assertEquals(0, viewModel.activeTab.value)
    }

    @Test
    fun `initial previous tab is 0`() = runTest {
        // Then
        assertEquals(0, viewModel.previousTab.value)
    }

    @Test
    fun `updateActiveTab updates active tab`() = runTest {
        // When
        viewModel.updateActiveTab(2)

        // Then
        assertEquals(2, viewModel.activeTab.value)
    }

    @Test
    fun `updateActiveTab updates previous tab to old active tab`() = runTest {
        // Given
        viewModel.updateActiveTab(1)

        // When
        viewModel.updateActiveTab(3)

        // Then
        assertEquals(3, viewModel.activeTab.value)
        assertEquals(1, viewModel.previousTab.value)
    }

    @Test
    fun `updateActiveTab does not update if index is same`() = runTest {
        // Given
        viewModel.updateActiveTab(2)
        val initialPrevious = viewModel.previousTab.value

        // When
        viewModel.updateActiveTab(2)

        // Then
        assertEquals(2, viewModel.activeTab.value)
        assertEquals(initialPrevious, viewModel.previousTab.value)
    }

    @Test
    fun `state persists across process death`() = runTest {
        // Given - Setup initial state
        viewModel.updateActiveTab(3)

        // When - Simulate process death and recreation
        val restoredViewModel = HomeViewModel(savedStateHandle)

        // Then - State should be restored
        assertEquals(3, restoredViewModel.activeTab.value)
    }

    @Test
    fun `state with custom initial values restored correctly`() = runTest {
        // Given - Create ViewModel with saved state
        val initialState = SavedStateHandle(
            mapOf(
                "active_tab" to 4,
                "previous_tab" to 2
            )
        )

        // When
        val viewModelWithState = HomeViewModel(initialState)

        // Then
        assertEquals(4, viewModelWithState.activeTab.value)
        assertEquals(2, viewModelWithState.previousTab.value)
    }

    @Test
    fun `multiple tab updates maintain correct previous tab`() = runTest {
        // Given/When
        viewModel.updateActiveTab(1) // previous: 0 -> active: 1
        viewModel.updateActiveTab(2) // previous: 1 -> active: 2
        viewModel.updateActiveTab(3) // previous: 2 -> active: 3

        // Then
        assertEquals(3, viewModel.activeTab.value)
        assertEquals(2, viewModel.previousTab.value)
    }
}
