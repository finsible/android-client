package com.itsjeel01.finsiblefrontend.ui.component.templates.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleIconBadge
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleIconBadgeDefaults
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold

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
        val type = FinsibleTheme.typography

        Column {
            Text(
                text = "Finsible Icon Badge",
                style = type.t32.bold(),
                color = colors.brandAccent
            )
            Text(
                text = "Visual Component Guide",
                style = type.t16,
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
                        showBackground = false
                    )
                    Text(text = "None", style = type.t12, color = colors.secondaryContent)
                }
                SupportedIconBadgeShapes.forEach { badgeShape ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(d.d6)
                    ) {
                        FinsibleIconBadge(icon = walletIcon, shapeVariant = badgeShape)
                        Text(text = badgeShape.name, style = type.t12, color = colors.secondaryContent)
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
                        FinsibleIconBadge(icon = walletIcon, size = badgeSize)
                        Text(text = badgeSize.name, style = type.t12, color = colors.secondaryContent)
                    }
                }
            }
        }

        FinsiblePreviewSection("Background Alpha") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(d.d12),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FinsibleIconBadge(icon = walletIcon, backgroundAlpha = 0.08f)
                FinsibleIconBadge(icon = walletIcon, backgroundAlpha = FinsibleIconBadgeDefaults.DEFAULT_BACKGROUND_ALPHA)
                FinsibleIconBadge(icon = walletIcon, backgroundAlpha = 0.28f)
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
                    )
                )
                FinsibleIconBadge(
                    icon = walletIcon,
                    shapeVariant = FinsibleShape.Rounded,
                    colors = FinsibleIconBadgeDefaults.colors(
                        iconTint = colors.brandAccent,
                        backgroundTint = colors.brandAccent20
                    ),
                    backgroundAlpha = 1f
                )
                FinsibleIconBadge(
                    icon = walletIcon,
                    showBackground = false,
                    colors = FinsibleIconBadgeDefaults.colors(iconTint = colors.warning)
                )
            }
        }

        FinsiblePreviewSection("Coverage Matrix") {
            SupportedIconBadgeSizes.forEach { badgeSize ->
                Text(
                    text = badgeSize.name,
                    style = type.t14.semiBold(),
                    color = colors.brandAccent
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(d.d10),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FinsibleIconBadge(
                        icon = walletIcon,
                        size = badgeSize,
                        showBackground = false
                    )
                    SupportedIconBadgeShapes.forEach { badgeShape ->
                        FinsibleIconBadge(
                            icon = walletIcon,
                            size = badgeSize,
                            shapeVariant = badgeShape
                        )
                    }
                }

                HorizontalDivider(color = colors.divider)
            }
        }
    }
}




