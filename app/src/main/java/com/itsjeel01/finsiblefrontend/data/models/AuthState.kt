package com.itsjeel01.finsiblefrontend.data.models

sealed class AuthState {
    data object Loading : AuthState()
    data class Negative(val message: String = "Something went wrong, please log in again") :
        AuthState()

    data object Positive : AuthState()
}