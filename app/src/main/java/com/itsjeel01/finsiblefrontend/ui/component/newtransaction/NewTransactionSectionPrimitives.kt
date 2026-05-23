package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleRadius
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold

object NewTransactionSectionDefaults {
    val headerSpacing: Dp
        @Composable get() = FinsibleTheme.spacing.insetSm

    val contentSpacing: Dp
        @Composable get() = FinsibleTheme.spacing.stackSm
}

@Composable
fun NewTransactionSectionLabel(
    label: String,
    hint: String? = null,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.gapXs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                leadingIcon()
            }
            FinsibleText(
                text = label,
                textStyle = FinsibleTheme.typography.labelMd.semiBold(),
                uppercase = true,
                colorVariant = FinsibleTextColorVariant.Secondary
            )
            if (hint != null) {
                FinsibleText(
                    text = hint,
                    textStyle = FinsibleTheme.typography.labelSm.medium(),
                    colorVariant = FinsibleTextColorVariant.Tertiary
                )
            }
        }

        trailingContent?.invoke()
    }
}


@Composable
fun NewTransactionSectionCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(FinsibleRadius.Component.card))
            .background(FinsibleTheme.colors.surfaceSunken)
            .border(
                width = FinsibleTheme.stroke.hairline,
                color = FinsibleTheme.colors.borderSubtle,
                shape = RoundedCornerShape(FinsibleRadius.Component.card)
            )
            .padding(FinsibleTheme.spacing.insetMd)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
    ) {
        content()
    }
}

@Composable
fun NewTransactionExpandToggleButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FinsibleButton(
        onClick = onClick,
        text = if (expanded) "Less" else "More",
        size = FinsibleSize.ExtraSmall,
        shapeVariant = FinsibleShape.Pill,
        variant = FinsibleButtonVariant.Text,
        colors = FinsibleButtonDefaults.colors(
            variant = FinsibleButtonVariant.Text,
            contentColor = FinsibleTheme.colors.contentSecondary,
        ),
        enforceMinTouchTargetSize = false,
        modifier = modifier
    )
}

