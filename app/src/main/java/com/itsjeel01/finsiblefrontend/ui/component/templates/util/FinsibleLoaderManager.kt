package com.itsjeel01.finsiblefrontend.ui.component.templates.util

import androidx.compose.runtime.staticCompositionLocalOf
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleOverlayOpacity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

val LocalFinsibleLoader = staticCompositionLocalOf<FinsibleLoaderManager> {
    error("FinsibleLoaderManager not provided! Ensure it is wrapped in CompositionLocalProvider at the App root.")
}

@Singleton
class FinsibleLoaderManager @Inject constructor() {

    private val _isActive = MutableStateFlow(false)
    val isActive: StateFlow<Boolean> = _isActive.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    private val _opacity = MutableStateFlow(FinsibleOverlayOpacity.Regular)
    val opacity: StateFlow<FinsibleOverlayOpacity> = _opacity.asStateFlow()

    fun show(
        message: String? = null,
        opacity: FinsibleOverlayOpacity = FinsibleOverlayOpacity.Regular
    ) {
        _message.value = message
        _opacity.value = opacity
        _isActive.value = true
    }

    fun hide() {
        _isActive.value = false
        _message.value = null
        _opacity.value = FinsibleOverlayOpacity.Regular // Reset to default
    }
}