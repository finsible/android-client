package com.itsjeel01.finsiblefrontend.ui.model

sealed class AuthState {
    data object Loading : AuthState()

    data class Negative(
        val message: String = "You are not logged in.",
        val isFailed: Boolean = false
    ) : AuthState()

    data object Positive : AuthState()
}
