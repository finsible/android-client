package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.RepeatableSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.IntOffset

// ── Easing Constants ─────────────────────────────────────────────────────

/**
 * Material 3 Emphasized Easing — a premium, natural-feeling cubic bezier.
 * Used by [NavigationTransitions] and available for custom transitions.
 */
val EmphasizedEasing = CubicBezierEasing(0.2f, 0.0f, 0f, 1.0f)

// ── Slide Direction ─────────────────────────────────────────────────────

/**
 * Direction shorthand for slide transitions built from [FinsibleAnimationSpecs].
 */
enum class SlideDirection {
    Left,
    Right,
    Up,
    Down,
}

// ── Animation Specs ──────────────────────────────────────────────────────

/**
 * Layer 2 — Pre-built animation specs referencing [FinsibleDurations].
 *
 * Every spec is parameterised on [Float] — the most common use case
 * (alpha, scale, offset, progress). For [Color], [Dp], or [Int] animations,
 * compose the spec at the call site:
 * ```
 * spring<Color>(dampingRatio = ..., stiffness = ...)
 * ```
 *
 * Accessed via [FinsibleTheme.animations.specs].
 *
 * ## Usage philosophy
 * - **Main content animations** (tab switches, list mutations, layout shifts):
 *   use spring-based specs (`spring*`) for fast, snappy, fluid feel.
 * - **Utility / ephemeral animations** (tooltips, micro-interactions, loaders):
 *   use tween-based or linear specs.
 * - **Navigation transitions**: use the `crossfade*`, `fadeSlide*`, or `springFade*`
 *   transition builder functions.
 */
@Immutable
data class FinsibleAnimationSpecs(
    // ── Tween-based specs (ephemeral / micro-interactions) ────────────
    /** Fade in — 150 ms, fast-out slow-in. */
    val fadeIn: TweenSpec<Float>,
    /** Fade out — 150 ms, fast-out slow-in. */
    val fadeOut: TweenSpec<Float>,
    /** Slide in — 200 ms, fast-out slow-in. */
    val slideIn: TweenSpec<Float>,
    /** Slide out — 200 ms, fast-out slow-in. */
    val slideOut: TweenSpec<Float>,
    /** Expand reveal — 250 ms, fast-out slow-in. */
    val expandIn: TweenSpec<Float>,
    /** Collapse hide — 250 ms, fast-out slow-in. */
    val expandOut: TweenSpec<Float>,
    /** Scale enter — 250 ms, linear-out slow-in (appear then settle). */
    val scaleIn: TweenSpec<Float>,
    /** Scale exit — 200 ms, fast-out slow-in (fast departure). */
    val scaleOut: TweenSpec<Float>,
    /** Color/theme change — 150 ms, fast-out slow-in. */
    val colorChange: TweenSpec<Float>,
    /** Size/layout change — 300 ms, fast-out slow-in. */
    val sizeChange: TweenSpec<Float>,
    /** Linear loop — 400 ms, linear easing for spinners / indeterminate progress. */
    val linearLoop: TweenSpec<Float>,

    // ── Spring-based specs (main content — fast, snappy, fluid) ──────
    /**
     * No-bouncy, medium stiffness — crisp, immediate lock-in.
     * Use for: precise position/size targets, drag-return animations,
     * scrubber progress, scroll-aware transforms.
     */
    val springStiff: SpringSpec<Float>,

    /**
     * Medium-bouncy, medium-low stiffness — natural feel with slight overshoot.
     * Use for: general UI transitions, tab switches, section reveals,
     * animated content size changes.
     */
    val springResponsive: SpringSpec<Float>,

    /**
     * No-bouncy, medium-low stiffness — smooth and subtle.
     * Use for: dropdown menus, bottom sheet entries, expand animations
     * where bounce would be distracting.
     */
    val springGentle: SpringSpec<Float>,

    /**
     * No-bouncy, high stiffness — nearly instant.
     * Use for: press-to-lock, immediate position correction,
     * value watchers that should feel instant.
     */
    val springSnappy: SpringSpec<Float>,

    /**
     * High-bouncy, low stiffness — playful, celebratory bounce.
     * Use for: confetti, achievement reveals, toggle state changes,
     * FAB selection transitions.
     */
    val springBouncy: SpringSpec<Float>,

    // ── Tactile feedback springs ──────────────────────────────────────
    /** Spring press — stiff, low damping for tactile press feedback. */
    val pressSpring: SpringSpec<Float>,
    /** Spring release — softer, lighter damping for return. */
    val releaseSpring: SpringSpec<Float>,

    // ── Linear tween specs ────────────────────────────────────────────
    /** Linear fade — same duration as [fadeIn] but linear easing. */
    val linearFade: TweenSpec<Float>,
    /** Linear rotation — 400 ms for spinners / progress indicators. */
    val linearRotate: TweenSpec<Float>,

    // ── Repeatable specs ──────────────────────────────────────────────
    /**
     * Pulse oscillation — 800 ms, infinite ping-pong.
     * Use for: skeleton loaders, shimmer placeholders, attention-seeking
     * indicators without a fixed end state.
     */
    val pulseSpec: RepeatableSpec<Float>,
) {
    companion object {
        /** Build all specs from a [FinsibleDurations] instance. */
        fun create(durations: FinsibleDurations): FinsibleAnimationSpecs = FinsibleAnimationSpecs(
            // ── Tween ──
            fadeIn = tween(
                durationMillis = durations.fadeMs,
                easing = FastOutSlowInEasing
            ),
            fadeOut = tween(
                durationMillis = durations.fadeMs,
                easing = FastOutSlowInEasing
            ),
            slideIn = tween(
                durationMillis = durations.slideMs,
                easing = FastOutSlowInEasing
            ),
            slideOut = tween(
                durationMillis = durations.slideMs,
                easing = FastOutSlowInEasing
            ),
            expandIn = tween(
                durationMillis = durations.expandMs,
                easing = FastOutSlowInEasing
            ),
            expandOut = tween(
                durationMillis = durations.expandMs,
                easing = FastOutSlowInEasing
            ),
            scaleIn = tween(
                durationMillis = durations.expandMs,
                easing = LinearOutSlowInEasing
            ),
            scaleOut = tween(
                durationMillis = durations.slideMs,
                easing = FastOutSlowInEasing
            ),
            colorChange = tween(
                durationMillis = durations.fadeMs,
                easing = FastOutSlowInEasing
            ),
            sizeChange = tween(
                durationMillis = durations.revealMs,
                easing = FastOutSlowInEasing
            ),
            linearLoop = tween(
                durationMillis = durations.emphasisMs,
                easing = LinearEasing
            ),

            // ── Springs ──
            springStiff = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium,
            ),
            springResponsive = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow,
            ),
            springGentle = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMediumLow,
            ),
            springSnappy = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessHigh,
            ),
            springBouncy = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessLow,
            ),

            // ── Tactile feedback ──
            pressSpring = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessHigh,
            ),
            releaseSpring = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium,
            ),

            // ── Linear ──
            linearFade = tween(
                durationMillis = durations.fadeMs,
                easing = LinearEasing,
            ),
            linearRotate = tween(
                durationMillis = durations.emphasisMs,
                easing = LinearEasing,
            ),

            // ── Repeatable ──
            pulseSpec = repeatable(
                iterations = Int.MAX_VALUE,
                animation = tween(
                    durationMillis = durations.emphasisMs * 2,
                    easing = FastOutSlowInEasing,
                ),
                repeatMode = RepeatMode.Reverse,
                initialStartOffset = StartOffset(0),
            ),
        )
    }

    // ── Crossfade Transitions ─────────────────────────────────────────

    /** [fadeIn] using the spec's [fadeIn] tween. */
    fun crossfadeIn(): EnterTransition = fadeIn(animationSpec = fadeIn)

    /** [fadeOut] using the spec's [fadeOut] tween. */
    fun crossfadeOut(): ExitTransition = fadeOut(animationSpec = fadeOut)

    /**
     * Clean crossfade — no sliding, no scaling.
     * Use for: full-page route transitions, modal open/close.
     */
    fun crossfadeTransition(): ContentTransform =
        crossfadeIn() togetherWith crossfadeOut()

    // ── Fade + Slide Transitions ──────────────────────────────────────

    /**
     * Combined [fadeIn] + [slideIn] using the spec's tween in the given direction.
     * Use for: panel slides, detail sheet entries.
     */
    fun fadeSlideIn(direction: SlideDirection = SlideDirection.Right): EnterTransition =
        fadeIn(animationSpec = fadeIn) + when (direction) {
            SlideDirection.Left -> slideInHorizontally(
                animationSpec = tween(durationMillis = slideIn.durationMillis, easing = FastOutSlowInEasing)
            ) { -it }

            SlideDirection.Right -> slideInHorizontally(
                animationSpec = tween(durationMillis = slideIn.durationMillis, easing = FastOutSlowInEasing)
            ) { it }

            SlideDirection.Up -> slideInVertically(
                animationSpec = tween(durationMillis = slideIn.durationMillis, easing = FastOutSlowInEasing)
            ) { -it }

            SlideDirection.Down -> slideInVertically(
                animationSpec = tween(durationMillis = slideIn.durationMillis, easing = FastOutSlowInEasing)
            ) { it }
        }

    /**
     * Combined [fadeOut] + [slideOut] using the spec's tween in the given direction.
     */
    fun fadeSlideOut(direction: SlideDirection = SlideDirection.Right): ExitTransition =
        fadeOut(animationSpec = fadeOut) + when (direction) {
            SlideDirection.Left -> slideOutHorizontally(
                animationSpec = tween(durationMillis = slideOut.durationMillis, easing = FastOutSlowInEasing)
            ) { it }

            SlideDirection.Right -> slideOutHorizontally(
                animationSpec = tween(durationMillis = slideOut.durationMillis, easing = FastOutSlowInEasing)
            ) { -it }

            SlideDirection.Up -> slideOutVertically(
                animationSpec = tween(durationMillis = slideOut.durationMillis, easing = FastOutSlowInEasing)
            ) { it }

            SlideDirection.Down -> slideOutVertically(
                animationSpec = tween(durationMillis = slideOut.durationMillis, easing = FastOutSlowInEasing)
            ) { -it }
        }

    /**
     * Fade + slide transition with configurable direction.
     * Use for: in-page panel reveals, navigation drawer push, sidebar transitions.
     */
    fun fadeSlideTransition(direction: SlideDirection = SlideDirection.Right): ContentTransform =
        fadeSlideIn(direction) togetherWith fadeSlideOut(direction)

    // ── Fade + Scale Transitions ──────────────────────────────────────

    /**
     * Combined [fadeIn] + [scaleIn] using the spec's tween.
     * Use for: popover menus, tooltip reveals, dialog entries.
     */
    fun fadeScaleIn(): EnterTransition =
        fadeIn(animationSpec = fadeIn) + scaleIn(animationSpec = scaleIn)

    /**
     * Combined [fadeOut] + [scaleOut] using the spec's tween.
     */
    fun fadeScaleOut(): ExitTransition =
        fadeOut(animationSpec = fadeOut) + scaleOut(animationSpec = scaleOut)

    /**
     * Fade + scale transition.
     * Use for: menu/dropdown open/close, contextual popups.
     */
    fun fadeScaleTransition(): ContentTransform =
        fadeScaleIn() togetherWith fadeScaleOut()

    // ── Spring-based Fade Transitions (main content) ──────────────────

    /**
     * [fadeIn] using [springGentle] — smooth, no bounce.
     * Use for: tab content appearance, list item entrance.
     */
    fun springFadeIn(): EnterTransition = fadeIn(animationSpec = springGentle)

    /**
     * [fadeOut] using [springGentle].
     */
    fun springFadeOut(): ExitTransition = fadeOut(animationSpec = springGentle)

    /**
     * Spring-powered crossfade — fluid fade for main content panels.
     * Use for: tab content switching, list-to-detail transitions,
     * any main-content swap where slide would be too much.
     */
    fun springFadeTransition(): ContentTransform =
        springFadeIn() togetherWith springFadeOut()

    // ── Spring-based Fade + Slide Transitions ─────────────────────────

    /**
     * Fade + slide using [springGentle] for both enter and exit.
     * Use for: in-page content transitions where slide direction matters.
     */
    fun springFadeSlideIn(direction: SlideDirection = SlideDirection.Right): EnterTransition =
        fadeIn(animationSpec = springGentle) + when (direction) {
            SlideDirection.Left -> slideInHorizontally(
                animationSpec = spring<IntOffset>(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)
            ) { -it }

            SlideDirection.Right -> slideInHorizontally(
                animationSpec = spring<IntOffset>(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)
            ) { it }

            SlideDirection.Up -> slideInVertically(
                animationSpec = spring<IntOffset>(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)
            ) { -it }

            SlideDirection.Down -> slideInVertically(
                animationSpec = spring<IntOffset>(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)
            ) { it }
        }

    /**
     * Fade + slide exit using [springGentle].
     */
    fun springFadeSlideOut(direction: SlideDirection = SlideDirection.Right): ExitTransition =
        fadeOut(animationSpec = springGentle) + when (direction) {
            SlideDirection.Left -> slideOutHorizontally(
                animationSpec = spring<IntOffset>(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)
            ) { it }

            SlideDirection.Right -> slideOutHorizontally(
                animationSpec = spring<IntOffset>(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)
            ) { -it }

            SlideDirection.Up -> slideOutVertically(
                animationSpec = spring<IntOffset>(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)
            ) { it }

            SlideDirection.Down -> slideOutVertically(
                animationSpec = spring<IntOffset>(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)
            ) { -it }
        }

    /**
     * Spring-powered fade + slide transition.
     * Use for: tab-to-tab navigation, list mutation animations.
     */
    fun springFadeSlideTransition(direction: SlideDirection = SlideDirection.Right): ContentTransform =
        springFadeSlideIn(direction) togetherWith springFadeSlideOut(direction)
}
