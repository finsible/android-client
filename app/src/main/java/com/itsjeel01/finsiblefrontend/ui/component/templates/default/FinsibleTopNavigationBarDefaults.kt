package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

object FinsibleHeaderDefaults {
    @Composable
    fun contentHeight(): Dp = FinsibleTheme.sizes.touch.xl

    @Composable
    fun horizontalPadding(): Dp = FinsibleTheme.spacing.insetLg

    @Composable
    fun contentSpacing(): Dp = FinsibleTheme.spacing.gapMd

    @Composable
    fun backgroundColor(): Color = FinsibleTheme.colors.surfaceDefault

    @Composable
    fun titleColor(): Color = FinsibleTheme.colors.contentPrimary

    @Composable
    fun subtitleColor(): Color = FinsibleTheme.colors.contentSecondary
}
