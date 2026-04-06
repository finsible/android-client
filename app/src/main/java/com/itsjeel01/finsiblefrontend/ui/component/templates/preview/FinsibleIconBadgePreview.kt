package com.itsjeel01.finsiblefrontend.ui.component.templates.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleIconBadge
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleIconBadgeDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

private val SupportedIconBadgeSizes = listOf(FinsibleSize.Small, FinsibleSize.Medium, FinsibleSize.Large)
private val SupportedIconBadgeShapes = listOf(FinsibleShape.Circle, FinsibleShape.Rounded, FinsibleShape.Sharp)

@Preview(name = "Icon Badge - Light", showBackground = true, widthDp = 500, heightDp = 1050)
@Preview(name = "Icon Badge - Dark", showBackground = true, widthDp = 500, heightDp = 1050, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FinsibleIconBadgePreview() {
    val walletIcon: @Composable () -> Unit = {
        Icon(
            painter = painterResource(android.R.drawable.ic_menu_manage),
            contentDescription = null
        )
    }

    FinsibleComponentPreviewScaffold {
        val d = FinsibleTheme.dimes
        val colors = FinsibleTheme.colors

        Column {
            FinsibleText(
                text = "Finsible Icon Badge",
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

        FinsiblePreviewSection("Background Shapes") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(d.d12),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(d.d6)
                ) {
                    FinsibleIconBadge(
                        icon = walletIcon,
                        showBackground = false,
                        contentDescription = "Icon badge with no background"
                    )
                    FinsibleText(text = "None", variant = FinsibleTextVariant.SmallLabelRegular, color = colors.secondaryContent)
                }
                SupportedIconBadgeShapes.forEach { badgeShape ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(d.d6)
                    ) {
                        FinsibleIconBadge(icon = walletIcon, shapeVariant = badgeShape, contentDescription = badgeShape.name)
                        FinsibleText(text = badgeShape.name, variant = FinsibleTextVariant.SmallLabelRegular, color = colors.secondaryContent)
                    }
                }
            }
        }

        FinsiblePreviewSection("Sizes") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(d.d12),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SupportedIconBadgeSizes.forEach { badgeSize ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(d.d6)
                    ) {
                        FinsibleIconBadge(icon = walletIcon, size = badgeSize, contentDescription = badgeSize.name)
                        FinsibleText(text = badgeSize.name, variant = FinsibleTextVariant.SmallLabelRegular, color = colors.secondaryContent)
                    }
                }
            }
        }

        FinsiblePreviewSection("Background Alpha") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(d.d12),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FinsibleIconBadge(icon = walletIcon, backgroundAlpha = 0.08f, contentDescription = "Background alpha 0.08")
                FinsibleIconBadge(
                    icon = walletIcon,
                    backgroundAlpha = FinsibleIconBadgeDefaults.DEFAULT_BACKGROUND_ALPHA,
                    contentDescription = "Default background alpha"
                )
                FinsibleIconBadge(icon = walletIcon, backgroundAlpha = 0.28f, contentDescription = "Background alpha 0.28")
            }
        }

        FinsiblePreviewSection("Tint Overrides") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(d.d12),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FinsibleIconBadge(
                    icon = walletIcon,
                    colors = FinsibleIconBadgeDefaults.colors(
                        iconTint = colors.link,
                        backgroundTint = colors.info
                    ),
                    contentDescription = "Icon badge with custom icon and background colors"
                )
                FinsibleIconBadge(
                    icon = walletIcon,
                    shapeVariant = FinsibleShape.Rounded,
                    colors = FinsibleIconBadgeDefaults.colors(
                        iconTint = colors.brandAccent,
                        backgroundTint = colors.brandAccent20
                    ),
                    backgroundAlpha = 1f,
                    contentDescription = "Icon badge with custom icon and background colors"
                )
                FinsibleIconBadge(
                    icon = walletIcon,
                    showBackground = false,
                    colors = FinsibleIconBadgeDefaults.colors(iconTint = colors.warning),
                    contentDescription = "Icon badge with custom icon tint and no background"
                )
            }
        }

        FinsiblePreviewSection("Coverage Matrix") {
            SupportedIconBadgeSizes.forEach { badgeSize ->
                FinsibleText(
                    text = badgeSize.name,
                    variant = FinsibleTextVariant.SmallBodySemiBold,
                    color = colors.brandAccent
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(d.d10),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FinsibleIconBadge(
                        icon = walletIcon,
                        size = badgeSize,
                        showBackground = false,
                        contentDescription = "Icon badge with size ${badgeSize.name} and no background"
                    )
                    SupportedIconBadgeShapes.forEach { badgeShape ->
                        FinsibleIconBadge(
                            icon = walletIcon,
                            size = badgeSize,
                            shapeVariant = badgeShape,
                            contentDescription = "Icon badge with size ${badgeSize.name}, shape ${badgeShape.name}, and no background"
                        )
                    }
                }

                HorizontalDivider(color = colors.divider)
            }
        }
    }
}




