package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.runtime.Immutable

/**
 * Layer 2 — Duration tokens for all animations and transitions.
 *
 * Every animation in the design system derives its timing from these
 * semantic roles. No component hardcodes an int ms value.
 *
 * Ranges progress from instant (0 ms) to slow deliberate (750 ms).
 * Accessed via [FinsibleTheme.animations.durations].
 */
@Immutable
data class FinsibleDurations(
    /** Instant — no animation (0 ms). */
    val instantMs: Int,
    /** Press feedback — button press, ripple, press scale (50 ms). */
    val pressMs: Int,
    /** Focus / hover state transitions (100 ms). */
    val focusMs: Int,
    /** Fade in/out — tooltips, toasts, simple reveals (150 ms). */
    val fadeMs: Int,
    /** Slide transitions — panels, drawers, list items (200 ms). */
    val slideMs: Int,
    /** Expand / collapse — accordion, section visibility (250 ms). */
    val expandMs: Int,
    /** Reveal — bottom sheets, dialogs, modals entering (300 ms). */
    val revealMs: Int,
    /** Shift — page transitions, reorder, layout animations (350 ms). */
    val shiftMs: Int,
    /** Emphasis — loaders, progress, skeleton shimmer (400 ms). */
    val emphasisMs: Int,
    /** Deliberate — full page transitions, onboarding sequences (500 ms). */
    val deliberateMs: Int,
    /** Slow — deliberate reveals, celebration effects (750 ms). */
    val slowMs: Int,
) {
    companion object {
        /** Base (unscaled) duration values. */
        val values = FinsibleDurations(
            instantMs = 0,
            pressMs = 50,
            focusMs = 100,
            fadeMs = 150,
            slideMs = 200,
            expandMs = 250,
            revealMs = 300,
            shiftMs = 350,
            emphasisMs = 400,
            deliberateMs = 500,
            slowMs = 750,
        )
    }
}
