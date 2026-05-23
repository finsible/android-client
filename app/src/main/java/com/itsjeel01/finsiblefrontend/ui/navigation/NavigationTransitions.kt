package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import com.itsjeel01.finsiblefrontend.ui.theme.EmphasizedEasing
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleDurations

object NavigationTransitions {
    /** Backward-compat delegate to centralized [EmphasizedEasing]. */
    val emphasizedEasing get() = EmphasizedEasing

    /**
     * Used by the Outer Router (NavigationRoot, NavigationPlayground).
     * Provides a clean crossfade without any scaling.
     * Duration sourced from the design system's revealMs (300 ms).
     */
    fun rootTransition(): ContentTransform {
        val fadeSpec = tween<Float>(durationMillis = FinsibleDurations.values.revealMs, easing = EmphasizedEasing)
        return fadeIn(fadeSpec) togetherWith fadeOut(fadeSpec)
    }

    /**
     * Used by the Inner Router (NavigationHome).
     * Handles the specific logic for Tabs vs. Modals.
     */
    fun homeTransition(): ContentTransform {
        return EnterTransition.None togetherWith ExitTransition.None
    }
}
