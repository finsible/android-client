package com.itsjeel01.finsiblefrontend.ui.component.templates.util

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.PopupPositionProvider

/** Provides a position for a dropdown menu that aligns with the anchor element. */
class FlushDropdownPositionProvider(
    private val verticalOffset: Int = 0,
    private val onPositionCalculated: (isUpward: Boolean) -> Unit
) : PopupPositionProvider {

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val x = anchorBounds.left
        val spaceBelow = windowSize.height - anchorBounds.bottom
        val spaceAbove = anchorBounds.top

        var isUpward = false
        val y = if (popupContentSize.height <= spaceBelow) {
            anchorBounds.bottom + verticalOffset
        } else if (popupContentSize.height <= spaceAbove) {
            isUpward = true
            anchorBounds.top - popupContentSize.height - verticalOffset
        } else {
            if (spaceBelow >= spaceAbove) {
                anchorBounds.bottom + verticalOffset
            } else {
                isUpward = true
                anchorBounds.top - popupContentSize.height - verticalOffset
            }
        }

        onPositionCalculated(isUpward)

        return IntOffset(x, y)
    }
}