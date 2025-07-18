package com.itsjeel01.finsiblefrontend.data.model

import com.itsjeel01.finsiblefrontend.common.Strings

sealed class AuthState {
    data object Loading : AuthState()

    data class Negative(
        val message: String = Strings.AUTH_FAILED_GENERIC_MESSAGE,
        val isFailed: Boolean = false
    ) : AuthState()

    data object Positive : AuthState()
}
