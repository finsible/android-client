package com.itsjeel01.finsiblefrontend.ui.loading

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadingIndicatorManager @Inject constructor() {

    private val _isActive = MutableStateFlow(false)
    val isActive: StateFlow<Boolean> = _isActive.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    fun show(message: String? = null) {
        _message.value = message
        _isActive.value = true
    }

    fun hide() {
        _isActive.value = false
        _message.value = null
    }
}