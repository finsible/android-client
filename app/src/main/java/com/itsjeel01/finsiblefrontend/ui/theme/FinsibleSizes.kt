package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Layer 2 — Size tokens for touch targets, icons, avatars, and shadows.
 *
 * Distinct from the template [com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize]
 * enum which represents a component size variant (XS…XL).
 */
object FinsibleSizes {

    /** Base (unscaled) size values. */
    val values = FinsibleSizeTokens(
        touch = TouchSizes(
            xs = 28.dp,   // Inline icon buttons in dense UI
            sm = 36.dp,   // Compact icon buttons, secondary actions
            md = 44.dp,   // Standard button / input field height
            lg = 52.dp,   // Primary CTA
            xl = 56.dp    // Hero action buttons, onboarding CTAs
        ),
        icon = IconSizes(
            xs = 12.dp,   // Inline status icons
            sm = 16.dp,   // Label-adjacent icons
            md = 20.dp,   // Standard UI icons
            lg = 24.dp,   // Navigation icons, prominent actions
            xl = 32.dp    // Empty states, feature illustrations
        ),
        avatar = AvatarSizes(
            sm = 32.dp,   // Compact lists
            md = 40.dp,   // Standard list rows
            lg = 48.dp    // Profile headers
        ),
        shadow = ShadowSizes(
            raised = FinsibleSpacing.values.stackSm,
            floating = FinsibleSpacing.values.stackMd,
            modal = FinsibleSpacing.values.stackXl,
            focus = FinsibleSpacing.values.stackXs
        )
    )

    /** Minimum touch-target heights (44dp baseline). */
    object Touch {
        val xs: Dp
            @Composable @ReadOnlyComposable get() = FinsibleTheme.sizes.touch.xs
        val sm: Dp
            @Composable @ReadOnlyComposable get() = FinsibleTheme.sizes.touch.sm
        val md: Dp
            @Composable @ReadOnlyComposable get() = FinsibleTheme.sizes.touch.md
        val lg: Dp
            @Composable @ReadOnlyComposable get() = FinsibleTheme.sizes.touch.lg
        val xl: Dp
            @Composable @ReadOnlyComposable get() = FinsibleTheme.sizes.touch.xl
    }

    /** Icon sizes. */
    object Icon {
        val xs: Dp
            @Composable @ReadOnlyComposable get() = FinsibleTheme.sizes.icon.xs
        val sm: Dp
            @Composable @ReadOnlyComposable get() = FinsibleTheme.sizes.icon.sm
        val md: Dp
            @Composable @ReadOnlyComposable get() = FinsibleTheme.sizes.icon.md
        val lg: Dp
            @Composable @ReadOnlyComposable get() = FinsibleTheme.sizes.icon.lg
        val xl: Dp
            @Composable @ReadOnlyComposable get() = FinsibleTheme.sizes.icon.xl
    }

    /** Avatar diameters. */
    object Avatar {
        val sm: Dp
            @Composable @ReadOnlyComposable get() = FinsibleTheme.sizes.avatar.sm
        val md: Dp
            @Composable @ReadOnlyComposable get() = FinsibleTheme.sizes.avatar.md
        val lg: Dp
            @Composable @ReadOnlyComposable get() = FinsibleTheme.sizes.avatar.lg
    }

    /** Shadow elevation sizes. */
    object Shadow {
        val raised: Dp
            @Composable @ReadOnlyComposable get() = FinsibleTheme.sizes.shadow.raised
        val floating: Dp
            @Composable @ReadOnlyComposable get() = FinsibleTheme.sizes.shadow.floating
        val modal: Dp
            @Composable @ReadOnlyComposable get() = FinsibleTheme.sizes.shadow.modal
        val focus: Dp
            @Composable @ReadOnlyComposable get() = FinsibleTheme.sizes.shadow.focus
    }
}

@Immutable
data class FinsibleSizeTokens(
    val touch: TouchSizes,
    val icon: IconSizes,
    val avatar: AvatarSizes,
    val shadow: ShadowSizes
)

@Immutable
data class TouchSizes(
    val xs: Dp,
    val sm: Dp,
    val md: Dp,
    val lg: Dp,
    val xl: Dp
)

@Immutable
data class IconSizes(
    val xs: Dp,
    val sm: Dp,
    val md: Dp,
    val lg: Dp,
    val xl: Dp
)

@Immutable
data class AvatarSizes(
    val sm: Dp,
    val md: Dp,
    val lg: Dp
)

@Immutable
data class ShadowSizes(
    val raised: Dp,
    val floating: Dp,
    val modal: Dp,
    val focus: Dp
)
