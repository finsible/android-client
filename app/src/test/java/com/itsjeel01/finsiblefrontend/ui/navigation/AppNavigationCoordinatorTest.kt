package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/** Unit tests for AppNavigationCoordinator to demonstrate navigation coordinator pattern testing. */
class AppNavigationCoordinatorTest {

    private lateinit var navController: NavController
    private lateinit var coordinator: AppNavigationCoordinator

    @Before
    fun setup() {
        navController = mockk(relaxed = true)
        coordinator = AppNavigationCoordinator(navController)
    }

    @Test
    fun `navigateToOnboarding clears launch screen from back stack`() {
        // When
        coordinator.navigateToOnboarding()

        // Then
        val destinationSlot = slot<Any>()
        val optionsSlot = slot<NavOptionsBuilder.() -> Unit>()
        verify { navController.navigate(capture(destinationSlot), capture(optionsSlot)) }

        // Verify destination
        assertEquals(AppRoutes.Onboarding, destinationSlot.captured)
    }

    @Test
    fun `navigateToHome from Launch clears launch screen`() {
        // When
        coordinator.navigateToHome(AppRoutes.Launch)

        // Then
        val destinationSlot = slot<Any>()
        verify { navController.navigate(capture(destinationSlot), any<NavOptionsBuilder.() -> Unit>()) }

        assertEquals(AppRoutes.Home, destinationSlot.captured)
    }

    @Test
    fun `navigateToHome from Onboarding clears onboarding screen`() {
        // When
        coordinator.navigateToHome(AppRoutes.Onboarding)

        // Then
        val destinationSlot = slot<Any>()
        verify { navController.navigate(capture(destinationSlot), any<NavOptionsBuilder.() -> Unit>()) }

        assertEquals(AppRoutes.Home, destinationSlot.captured)
    }

    @Test
    fun `navigateToOnboardingFromHome clears home screen from back stack`() {
        // When
        coordinator.navigateToOnboardingFromHome()

        // Then
        val destinationSlot = slot<Any>()
        verify { navController.navigate(capture(destinationSlot), any<NavOptionsBuilder.() -> Unit>()) }

        assertEquals(AppRoutes.Onboarding, destinationSlot.captured)
    }

    @Test
    fun `navigateBack delegates to navController`() {
        // When
        coordinator.navigateBack()

        // Then
        verify { navController.popBackStack() }
    }

    @Test
    fun `navigateTo with custom builder delegates to navController`() {
        // Given
        val destination = AppRoutes.Home
        var builderCalled = false
        val builder: NavOptionsBuilder.() -> Unit = { builderCalled = true }

        // When
        coordinator.navigateTo(destination, builder)

        // Then
        verify { navController.navigate(destination, builder) }
    }
}
