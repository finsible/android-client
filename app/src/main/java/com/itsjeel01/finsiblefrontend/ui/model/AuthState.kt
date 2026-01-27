package com.itsjeel01.finsiblefrontend.ui.model

import androidx.compose.runtime.Immutable

@Immutable
sealed class AuthState {
    @Immutable
    data object Loading : AuthState()

    @Immutable
    data class Negative(
        val message: String = "You are not logged in.",
        val isFailed: Boolean = false
    ) : AuthState()

    @Immutable
    data object Positive : AuthState()
}
