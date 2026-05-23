package com.itsjeel01.finsiblefrontend.ui.component.templates.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleCheckbox
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleCheckboxVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold
import com.itsjeel01.finsiblefrontend.ui.theme.bold

private val SupportedCheckboxSizes = listOf(FinsibleSize.Small, FinsibleSize.Medium, FinsibleSize.Large)

@Preview(name = "Checkbox - Light", showBackground = true, widthDp = 400, heightDp = 900)
@Preview(name = "Checkbox - Dark", showBackground = true, widthDp = 400, heightDp = 900, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FinsibleCheckboxPreview() {
    FinsibleComponentPreviewScaffold {
        val sp = FinsibleTheme.spacing
        val s = FinsibleTheme.colors
        Column {
            FinsibleText(
                text = "Finsible Checkbox",
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

        FinsiblePreviewSection("Variants") {
            FinsibleCheckboxVariant.entries.forEach { checkboxVariant ->
                val checked = remember(checkboxVariant) { mutableStateOf(true) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(sp.gapSm)
                ) {
                    FinsibleCheckbox(
                        checked = checked.value,
                        onCheckedChange = { checked.value = it },
                        variant = checkboxVariant,
                        animateChecking = false,
                        size = FinsibleSize.Medium,
                        checkboxContentDescription = "${checkboxVariant.name} checkbox preview"
                    )
                    FinsibleText(
                        text = checkboxVariant.name,
                        textStyle = FinsibleTheme.typography.bodyLg.semiBold(),
                        color = s.contentPrimary
                    )
                }
            }
        }

        FinsiblePreviewSection("Sizes") {
            Row(horizontalArrangement = Arrangement.spacedBy(sp.insetXl), verticalAlignment = Alignment.CenterVertically) {
                SizeSample(size = FinsibleSize.Small, label = "Small")
                SizeSample(size = FinsibleSize.Medium, label = "Regular")
                SizeSample(size = FinsibleSize.Large, label = "Large")
            }
        }

        FinsiblePreviewSection("Label Variations") {
            val withLabelChecked = remember { mutableStateOf(true) }
            val withoutLabelChecked = remember { mutableStateOf(false) }

            Column(verticalArrangement = Arrangement.spacedBy(sp.gapMd)) {
                FinsibleCheckbox(
                    checked = withLabelChecked.value,
                    onCheckedChange = { withLabelChecked.value = it },
                    label = "Include recurring reminder",
                    checkboxContentDescription = "Checkbox with label"
                )
                FinsibleCheckbox(
                    checked = withoutLabelChecked.value,
                    onCheckedChange = { withoutLabelChecked.value = it },
                    checkboxContentDescription = "Checkbox without label"
                )
            }
        }

        FinsiblePreviewSection("Shapes") {
            Row(horizontalArrangement = Arrangement.spacedBy(sp.insetXl), verticalAlignment = Alignment.CenterVertically) {
                ShapeSample(label = "Rounded", shapeVariant = FinsibleShape.Rounded)
                ShapeSample(label = "Sharp", shapeVariant = FinsibleShape.Sharp)
            }
        }

        FinsiblePreviewSection("States") {
            Row(horizontalArrangement = Arrangement.spacedBy(sp.insetXl), verticalAlignment = Alignment.CenterVertically) {
                StateSample(label = "Checked", checked = true, enabled = true)
                StateSample(label = "Unchecked", checked = false, enabled = true)
                StateSample(label = "Disabled Checked", checked = true, enabled = false)
                StateSample(label = "Disabled Unchecked", checked = false, enabled = false)
            }
        }

        FinsiblePreviewSection("Coverage Matrix") {
            FinsibleCheckboxVariant.entries.forEach { checkboxVariant ->
                FinsibleText(
                    text = checkboxVariant.name,
                    textStyle = FinsibleTheme.typography.bodyMd.semiBold(),
                    color = s.brandInteractive
                )

                Row(horizontalArrangement = Arrangement.spacedBy(sp.gapSm), verticalAlignment = Alignment.CenterVertically) {
                    SupportedCheckboxSizes.forEach { checkboxSize ->
                            FinsibleCheckbox(
                                checked = true,
                                onCheckedChange = {},
                                variant = checkboxVariant,
                                size = checkboxSize,
                                checkboxContentDescription = "${checkboxVariant.name} ${checkboxSize.name} checked preview"
                            )
                            FinsibleCheckbox(
                                checked = false,
                                onCheckedChange = {},
                                variant = checkboxVariant,
                                size = checkboxSize,
                                checkboxContentDescription = "${checkboxVariant.name} ${checkboxSize.name} unchecked preview"
                            )
                            FinsibleCheckbox(
                                checked = true,
                                onCheckedChange = {},
                                variant = checkboxVariant,
                                size = checkboxSize,
                                enabled = false,
                                checkboxContentDescription = "${checkboxVariant.name} ${checkboxSize.name} disabled preview"
                            )
                    }
                }

                HorizontalDivider(color = s.borderSubtle)
            }
        }
    }
}

@Composable
private fun SizeSample(size: FinsibleSize, label: String) {
    val checked = remember(size) { mutableStateOf(true) }

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.inlineMd)) {
        FinsibleCheckbox(
            checked = checked.value,
            onCheckedChange = { checked.value = it },
            size = size,
            checkboxContentDescription = "$label size preview"
        )
        FinsibleText(text = label, textStyle = FinsibleTheme.typography.bodyMd, color = FinsibleTheme.colors.contentSecondary)
    }
}

@Composable
private fun StateSample(label: String, checked: Boolean, enabled: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.inlineMd)) {
        FinsibleCheckbox(
            checked = checked,
            onCheckedChange = {},
            enabled = enabled,
            checkboxContentDescription = "$label state preview"
        )
        FinsibleText(text = label, textStyle = FinsibleTheme.typography.bodySm, color = FinsibleTheme.colors.contentSecondary)
    }
}

@Composable
private fun ShapeSample(label: String, shapeVariant: FinsibleShape) {
    val checked = remember(shapeVariant) { mutableStateOf(true) }

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.inlineMd)) {
        FinsibleCheckbox(
            checked = checked.value,
            onCheckedChange = { checked.value = it },
            shapeVariant = shapeVariant,
            checkboxContentDescription = "$label shape preview"
        )
        FinsibleText(text = label, textStyle = FinsibleTheme.typography.bodySm, color = FinsibleTheme.colors.contentSecondary)
    }
}

