package com.itsjeel01.finsiblefrontend.data.model

sealed class AuthState {
    data object Loading : AuthState()
    data class Negative(val message: String = "Something went wrong, please log in again", val isFailed: Boolean = false) : AuthState()
    data object Positive : AuthState()
}