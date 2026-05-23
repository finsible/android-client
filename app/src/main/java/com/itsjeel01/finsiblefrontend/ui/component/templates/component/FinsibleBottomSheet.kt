package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleBottomSheetDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleBottomSheetAction
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleBottomSheetColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold

/**
 * Renders a highly optimized, templatized bottom sheet aligned with Finsible UI guidelines.
 *
 * @param onDismissRequest Callback invoked when the user attempts to dismiss the sheet.
 * @param modifier Optional [Modifier] for the bottom sheet.
 * @param sheetState The state of the bottom sheet, allowing external control over its visibility and behavior.
 * @param title Optional string for the main header of the sheet.
 * @param subtitle Optional string for the secondary header, displayed below the title.
 * @param trailingIcon Optional composable for an icon displayed at the end of the header row.
 * @param actions A list of [FinsibleBottomSheetAction] representing the CTAs at the bottom of the sheet.
 * @param colors A [FinsibleBottomSheetColors] object to customize the color scheme of the sheet.
 * @param windowInsets Custom [WindowInsets] to control how the sheet interacts with system UI elements. Defaults to no insets.
 * @param maxHeightPercent Optional float between 0.0 and 1.0 to restrict the maximum height of the sheet.
 * @param fixedHeightPercent Optional float between 0.0 and 1.0 to set an exact, non-flexible height for the sheet.
 * @param scrollContent If true, applies a vertical scroll state to the content body. Set to false if passing a LazyColumn.
 * @param content The main content of the sheet, defined as a composable lambda with a [ColumnScope] receiver for flexible layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinsibleBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    title: String? = null,
    subtitle: String? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    actions: List<FinsibleBottomSheetAction> = emptyList(),
    colors: FinsibleBottomSheetColors = FinsibleBottomSheetDefaults.colors(),
    windowInsets: WindowInsets = WindowInsets.safeDrawing,
    maxHeightPercent: Float? = null,
    fixedHeightPercent: Float? = null,
    scrollContent: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val sp = FinsibleTheme.spacing

    val screenHeight = FinsibleTheme.deviceInfo.height
    val maxSheetHeight = maxHeightPercent?.coerceIn(0f, 1f)?.let { screenHeight * it }
    val fixedSheetHeight = fixedHeightPercent?.coerceIn(0f, 1f)?.let { screenHeight * it }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        shape = FinsibleBottomSheetDefaults.shape(),
        containerColor = colors.containerColor,
        scrimColor = colors.scrimColor,
        dragHandle = null,
        contentWindowInsets = { windowInsets }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .let { baseModifier ->
                    when {
                        fixedSheetHeight != null -> baseModifier.height(fixedSheetHeight)
                        maxSheetHeight != null -> baseModifier.heightIn(max = maxSheetHeight)
                        else -> baseModifier
                    }
                }
                .padding(horizontal = sp.insetXl, vertical = sp.inset2xl)
        ) {

            if (title != null || subtitle != null || trailingIcon != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(sp.insetXs)
                    ) {
                        if (title != null) {
                            FinsibleText(
                                text = title,
                                textStyle = FinsibleTheme.typography.bodyLg.bold(),
                                color = colors.titleColor
                            )
                        }
                        if (subtitle != null) {
                            FinsibleText(
                                text = subtitle,
                                textStyle = FinsibleTheme.typography.bodyLg,
                                color = colors.subtitleColor
                            )
                        }
                    }

                    if (trailingIcon != null) {
                        Box(modifier = Modifier.padding(start = sp.gapMd)) {
                            trailingIcon()
                        }
                    }
                }
                Spacer(modifier = Modifier.height(sp.insetLg))
            }

            // By setting fill = true when fixedSheetHeight is not null, this Box forces the outer
            // Column to expand and pins any trailing actions perfectly to the bottom of the sheet.
            Box(modifier = Modifier.weight(1f, fill = fixedSheetHeight != null)) {
                Column(
                    modifier = if (scrollContent) Modifier.verticalScroll(rememberScrollState()) else Modifier,
                    content = content
                )
            }

            if (actions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(sp.inset2xl))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(sp.gapMd),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    actions.forEach { action ->
                        if (action.variant == FinsibleButtonVariant.Filled && colors.accentColor != Color.Unspecified) {
                            FinsibleButtonDefaults.colors(
                                variant = action.variant,
                                containerColor = colors.accentColor
                            )
                        } else {
                            FinsibleButtonDefaults.colors(variant = action.variant)
                        }
                        FinsibleButton(
                            text = action.text,
                            onClick = action.onClick,
                            variant = action.variant,
                            enabled = action.enabled,
                            loading = action.loading,
                            icon = action.icon,
                            fullWidth = actions.size == 1,
                            modifier = if (actions.size > 1) Modifier.weight(1f) else Modifier
                        )
                    }
                }
            }
        }
    }
}