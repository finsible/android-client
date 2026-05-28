package com.itsjeel01.finsiblefrontend.ui.component.templates.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleIconPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Action button specification for the header. */
@Immutable
data class FinsibleHeaderButton(
    val onClick: () -> Unit,
    val text: String? = null,
    val enabled: Boolean = true,
    val loading: Boolean = false,
    val iconOnly: Boolean = false,
    val variant: FinsibleButtonVariant = FinsibleButtonVariant.Text, // Default to Text for headers[cite: 32]
    val size: FinsibleSize = FinsibleSize.Medium,
    val shapeVariant: FinsibleShape = FinsibleShape.Pill,
    val badgeType: FinsibleBadgeType = FinsibleBadgeType.None,
    val badgeCount: Int = 0,
    val icon: (@Composable () -> Unit)? = null,
    val iconPosition: FinsibleIconPosition = FinsibleIconPosition.Leading,
)

/** Unified state for the screen header. */
@Immutable
data class FinsibleHeaderState(
    val title: String,
    val subtitle: String? = null,
    val hint: String? = null,
    val leftButtons: List<FinsibleHeaderButton> = emptyList(),
    val rightButtons: List<FinsibleHeaderButton> = emptyList(),
    val backgroundColor: Color? = null,
) {
    companion object {
        /** Factory to create state that resolves colors from the current theme. */
        @Composable
        fun themed(
            title: String,
            subtitle: String? = null,
            hint: String? = null,
            leftButtons: List<FinsibleHeaderButton> = emptyList(),
            rightButtons: List<FinsibleHeaderButton> = emptyList(),
            backgroundColor: Color = FinsibleTheme.colors.cardSurface,
        ): FinsibleHeaderState {
            return FinsibleHeaderState(
                title = title,
                subtitle = subtitle,
                hint = hint,
                leftButtons = leftButtons,
                rightButtons = rightButtons,
                backgroundColor = backgroundColor,
            )
        }
    }
}