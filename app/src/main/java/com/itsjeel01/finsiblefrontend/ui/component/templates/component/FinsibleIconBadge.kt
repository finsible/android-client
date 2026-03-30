package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleIconBadgeDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleIconBadgeColors

/** Decorative icon badge with optional background surface. */
@Composable
fun FinsibleIconBadge(
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    size: FinsibleSize = FinsibleSize.Medium,
    showBackground: Boolean = true,
    shapeVariant: FinsibleShape = FinsibleShape.Circle,
    colors: FinsibleIconBadgeColors = FinsibleIconBadgeDefaults.colors(),
    backgroundAlpha: Float = FinsibleIconBadgeDefaults.DEFAULT_BACKGROUND_ALPHA,
    contentDescription: String? = null
) {
    require(size == FinsibleSize.Small || size == FinsibleSize.Medium || size == FinsibleSize.Large) {
        "FinsibleIconBadge supports only Small, Medium, and Large sizes."
    }
    require(backgroundAlpha in 0f .. 1f) {
        "backgroundAlpha must be between 0f and 1f."
    }
    require(showBackground || backgroundAlpha == FinsibleIconBadgeDefaults.DEFAULT_BACKGROUND_ALPHA) {
        "backgroundAlpha can be customized only when showBackground is true."
    }
    require(contentDescription == null || contentDescription.isNotBlank()) {
        "contentDescription must be null or non-blank."
    }

    val badgeSizes = FinsibleIconBadgeDefaults.sizes(size)
    val badgeShape = FinsibleIconBadgeDefaults.shape(
        shapeVariant = shapeVariant,
        sizes = badgeSizes
    )

    val baseModifier = modifier
        .size(badgeSizes.containerSize)
        .semantics(mergeDescendants = true) {
            contentDescription?.let { this.contentDescription = it }
        }

    val surfaceModifier = if (!showBackground) {
        baseModifier
    } else {
        baseModifier
            .clip(badgeShape)
            .background(colors.backgroundTint.copy(alpha = backgroundAlpha))
    }

    Box(
        modifier = surfaceModifier,
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides colors.iconTint) {
            Box(
                modifier = Modifier.size(badgeSizes.iconSize),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
        }
    }
}





