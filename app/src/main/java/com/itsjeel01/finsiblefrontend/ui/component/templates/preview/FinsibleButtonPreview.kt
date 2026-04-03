package com.itsjeel01.finsiblefrontend.ui.component.templates.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleIconPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleBadgeType
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Preview(name = "Light Mode", showBackground = true, widthDp = 700, heightDp = 2800)
@Preview(name = "Dark Mode", showBackground = true, widthDp = 700, heightDp = 2800, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FinsibleButtonPreview() {
    val callIcon: @Composable () -> Unit = {
        Icon(
            painter = painterResource(android.R.drawable.ic_menu_call),
            contentDescription = null
        )
    }

    FinsibleComponentPreviewScaffold {
        val dimes = FinsibleTheme.dimes
        val colors = FinsibleTheme.colors

        // Header
        Column {
            FinsibleText(
                text = "Finsible Buttons",
                variant = FinsibleTextVariant.SmallHeadingBold,
                color = colors.brandAccent
            )
            FinsibleText(
                text = "Visual Component Guide",
                variant = FinsibleTextVariant.BodyRegular,
                color = colors.secondaryContent
            )
        }

        HorizontalDivider(color = colors.divider)

        FinsiblePreviewSection("Variants") {
            Row(horizontalArrangement = Arrangement.spacedBy(dimes.d12)) {
                FinsibleButton(onClick = {}, variant = FinsibleButtonVariant.Filled, text = "Filled")
                FinsibleButton(onClick = {}, variant = FinsibleButtonVariant.FilledTonal, text = "Tonal")
                FinsibleButton(onClick = {}, variant = FinsibleButtonVariant.Outlined, text = "Outlined")
                FinsibleButton(onClick = {}, variant = FinsibleButtonVariant.Text, text = "Text")
                FinsibleButton(onClick = {}, variant = FinsibleButtonVariant.Link, text = "Link Button")
            }
        }

        FinsiblePreviewSection("Interactive States") {
            // Filled Comparison
            Row(horizontalArrangement = Arrangement.spacedBy(dimes.d12)) {
                FinsibleButton(onClick = {}, variant = FinsibleButtonVariant.Filled, text = "Enabled")
                FinsibleButton(onClick = {}, enabled = false, variant = FinsibleButtonVariant.Filled, text = "Disabled")
                FinsibleButton(onClick = {}, loading = true, variant = FinsibleButtonVariant.Filled, text = "Loading")
            }
            // Outlined Comparison
            Row(horizontalArrangement = Arrangement.spacedBy(dimes.d12)) {
                FinsibleButton(onClick = {}, variant = FinsibleButtonVariant.Outlined, text = "Default")
                FinsibleButton(onClick = {}, enabled = false, variant = FinsibleButtonVariant.Outlined, text = "Disabled")
                FinsibleButton(onClick = {}, loading = true, variant = FinsibleButtonVariant.Outlined, text = "Loading")
            }
        }

        FinsiblePreviewSection("Sizes") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimes.d12),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SizeDemo(FinsibleSize.ExtraSmall, "XS")
                SizeDemo(FinsibleSize.Small, "SM")
                SizeDemo(FinsibleSize.Medium, "MD")
                SizeDemo(FinsibleSize.Large, "LG")
                SizeDemo(FinsibleSize.ExtraLarge, "XL")
            }
        }

        // 4. Content Layout (Icons & Badges)
        FinsiblePreviewSection("Icons & Badges") {
            Row(horizontalArrangement = Arrangement.spacedBy(dimes.d12)) {
                FinsibleButton(
                    onClick = {},
                    icon = callIcon,
                    iconPosition = FinsibleIconPosition.Leading,
                    text = "Leading"
                )

                FinsibleButton(
                    onClick = {},
                    variant = FinsibleButtonVariant.FilledTonal,
                    icon = callIcon,
                    iconPosition = FinsibleIconPosition.Trailing,
                    text = "Trailing"
                )

                FinsibleButton(
                    onClick = {},
                    variant = FinsibleButtonVariant.Outlined,
                    iconOnly = true,
                    icon = callIcon,
                    badgeType = FinsibleBadgeType.Dot
                )

                FinsibleButton(
                    onClick = {},
                    variant = FinsibleButtonVariant.Filled,
                    iconOnly = true,
                    icon = callIcon,
                    badgeType = FinsibleBadgeType.Count,
                    badgeCount = 24
                )
            }
        }

        FinsiblePreviewSection("Shapes & Layout") {
            Row(horizontalArrangement = Arrangement.spacedBy(dimes.d12)) {
                FinsibleButton(onClick = {}, shapeVariant = FinsibleShape.Pill, text = "Pill")
                FinsibleButton(
                    onClick = {},
                    shapeVariant = FinsibleShape.Rounded,
                    variant = FinsibleButtonVariant.FilledTonal,
                    text = "Rounded"
                )
                FinsibleButton(onClick = {}, shapeVariant = FinsibleShape.Sharp, variant = FinsibleButtonVariant.Outlined, text = "Sharp")
            }

            FinsibleButton(
                onClick = {},
                fullWidth = true,
                text = "Full Width Button"
            )
        }

        FinsiblePreviewSection("Coverage Matrix") {
            FinsibleButtonVariant.entries.forEach { matrixVariant ->
                FinsibleText(
                    text = matrixVariant.name,
                    variant = FinsibleTextVariant.SmallBodySemiBold,
                    color = colors.brandAccent
                )

                FinsibleSize.entries.forEach { matrixSize ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(dimes.d8),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FinsibleButton(
                            onClick = {},
                            variant = matrixVariant,
                            size = matrixSize,
                            text = "${matrixSize.name}-On"
                        )

                        FinsibleButton(
                            onClick = {},
                            variant = matrixVariant,
                            size = matrixSize,
                            enabled = false,
                            text = "Off"
                        )

                        FinsibleButton(
                            onClick = {},
                            variant = matrixVariant,
                            size = matrixSize,
                            loading = true,
                            text = "Load"
                        )

                        if (matrixVariant != FinsibleButtonVariant.Text && matrixVariant != FinsibleButtonVariant.Link && matrixSize != FinsibleSize.ExtraSmall) {
                            FinsibleButton(
                                onClick = {},
                                variant = matrixVariant,
                                size = matrixSize,
                                iconOnly = true,
                                icon = callIcon,
                                badgeType = FinsibleBadgeType.Dot
                            )
                        }

                        if (matrixVariant != FinsibleButtonVariant.Text && matrixVariant != FinsibleButtonVariant.Link && matrixSize != FinsibleSize.ExtraSmall) {
                            FinsibleButton(
                                onClick = {},
                                variant = matrixVariant,
                                size = matrixSize,
                                iconOnly = true,
                                icon = callIcon,
                                badgeType = FinsibleBadgeType.Count,
                                badgeCount = 24
                            )
                        }
                    }
                }

                HorizontalDivider(color = colors.divider)
            }
        }
    }
}

@Composable
private fun SizeDemo(size: FinsibleSize, label: String) {
    FinsibleButton(onClick = {}, size = size, text = label)
}