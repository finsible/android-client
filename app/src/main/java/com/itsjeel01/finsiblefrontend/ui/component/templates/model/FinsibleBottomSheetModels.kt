package com.itsjeel01.finsiblefrontend.ui.component.templates.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant

/**
 * Represents an actionable CTA at the bottom of the FinsibleBottomSheet.
 * Maps 1:1 with the capabilities of a [FinsibleButton].
 */
@Immutable
data class FinsibleBottomSheetAction(
    val text: String,
    val onClick: () -> Unit,
    val variant: FinsibleButtonVariant = FinsibleButtonVariant.Filled,
    val enabled: Boolean = true,
    val loading: Boolean = false,
    val icon: (@Composable () -> Unit)? = null
)

/** Immutable holder for Bottom Sheet colors. */
@Immutable
data class FinsibleBottomSheetColors(
    val containerColor: Color,
    val scrimColor: Color,
    val titleColor: Color,
    val subtitleColor: Color,
    val dragHandleColor: Color,
    val accentColor: Color
)