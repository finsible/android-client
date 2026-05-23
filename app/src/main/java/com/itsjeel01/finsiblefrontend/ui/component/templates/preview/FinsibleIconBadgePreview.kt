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
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold
import com.itsjeel01.finsiblefrontend.ui.theme.bold

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
        val sp = FinsibleTheme.spacing
        val s = FinsibleTheme.colors

        Column {
            FinsibleText(
                text = "Finsible Icon Badge",
                textStyle = FinsibleTheme.typography.displaySm.bold(),
                color = s.brandInteractive
            )
            FinsibleText(
                text = "Visual Component Guide",
                textStyle = FinsibleTheme.typography.bodyLg,
                color = s.contentSecondary
            )
        }

        HorizontalDivider(color = s.borderSubtle)

        FinsiblePreviewSection("Background Shapes") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(sp.gapMd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(sp.insetSm)
                ) {
                    FinsibleIconBadge(
                        icon = walletIcon,
                        showBackground = false,
                        contentDescription = "Icon badge with no background"
                    )
                    FinsibleText(text = "None", textStyle = FinsibleTheme.typography.bodySm, color = s.contentSecondary)
                }
                SupportedIconBadgeShapes.forEach { badgeShape ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(sp.insetSm)
                    ) {
                        FinsibleIconBadge(icon = walletIcon, shapeVariant = badgeShape, contentDescription = badgeShape.name)
                        FinsibleText(text = badgeShape.name, textStyle = FinsibleTheme.typography.bodySm, color = s.contentSecondary)
                    }
                }
            }
        }

        FinsiblePreviewSection("Sizes") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(sp.gapMd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SupportedIconBadgeSizes.forEach { badgeSize ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(sp.insetSm)
                    ) {
                        FinsibleIconBadge(icon = walletIcon, size = badgeSize, contentDescription = badgeSize.name)
                        FinsibleText(text = badgeSize.name, textStyle = FinsibleTheme.typography.bodySm, color = s.contentSecondary)
                    }
                }
            }
        }

        FinsiblePreviewSection("Background Alpha") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(sp.gapMd),
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
                horizontalArrangement = Arrangement.spacedBy(sp.gapMd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FinsibleIconBadge(
                    icon = walletIcon,
                    colors = FinsibleIconBadgeDefaults.colors(
                        iconTint = s.contentLink,
                        backgroundTint = s.feedbackInfo
                    ),
                    contentDescription = "Icon badge with custom icon and background colors"
                )
                FinsibleIconBadge(
                    icon = walletIcon,
                    shapeVariant = FinsibleShape.Rounded,
                    colors = FinsibleIconBadgeDefaults.colors(
                        iconTint = s.brandInteractive,
                        backgroundTint = s.brandTint
                    ),
                    backgroundAlpha = 1f,
                    contentDescription = "Icon badge with custom icon and background colors"
                )
                FinsibleIconBadge(
                    icon = walletIcon,
                    showBackground = false,
                    colors = FinsibleIconBadgeDefaults.colors(iconTint = s.feedbackWarning),
                    contentDescription = "Icon badge with custom icon tint and no background"
                )
            }
        }

        FinsiblePreviewSection("Coverage Matrix") {
            SupportedIconBadgeSizes.forEach { badgeSize ->
                FinsibleText(
                    text = badgeSize.name,
                    textStyle = FinsibleTheme.typography.bodyMd.semiBold(),
                    color = s.brandInteractive
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(sp.gapSm),
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

                HorizontalDivider(color = s.borderSubtle)
            }
        }
    }
}




