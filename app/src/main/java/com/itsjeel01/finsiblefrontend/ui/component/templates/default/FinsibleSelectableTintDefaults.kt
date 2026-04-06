package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.ui.graphics.Color

/** Shared selected-tint math for selectable template components. */
internal object FinsibleSelectableTintDefaults {

    private const val TONAL_CONTAINER_ALPHA = 0.16f
    private const val OUTLINED_TONAL_CONTAINER_ALPHA = 0.1f

    fun selectedContainerColor(selectedTint: Color, variant: FinsibleSelectableTintVariant): Color {
        return when (variant) {
            FinsibleSelectableTintVariant.Filled -> selectedTint
            FinsibleSelectableTintVariant.Tonal -> selectedTint.copy(alpha = TONAL_CONTAINER_ALPHA)
            FinsibleSelectableTintVariant.Outlined -> Color.Transparent
            FinsibleSelectableTintVariant.OutlinedTonal ->
                selectedTint.copy(alpha = OUTLINED_TONAL_CONTAINER_ALPHA)
        }
    }
}

internal enum class FinsibleSelectableTintVariant {
    Filled,
    Tonal,
    Outlined,
    OutlinedTonal
}

