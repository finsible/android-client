package com.itsjeel01.finsiblefrontend.ui.component.templates.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant

/** Shared scaffold for long, section-based component previews. */
@Composable
fun FinsibleComponentPreviewScaffold(content: @Composable ColumnScope.() -> Unit) {
    FinsibleTheme {
        val sp = FinsibleTheme.spacing

        Surface(color = FinsibleTheme.colors.surfaceBase) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = sp.inlineMd, horizontal = sp.insetLg),
                verticalArrangement = Arrangement.spacedBy(sp.gapMd),
                content = content
            )
        }
    }
}

/** Shared titled section used across component preview docs. */
@Composable
fun FinsiblePreviewSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    val sp = FinsibleTheme.spacing

    Column(verticalArrangement = Arrangement.spacedBy(sp.gapMd)) {
        FinsibleText(
            text = title,
            textStyle = FinsibleTheme.typography.headingSm,
            colorVariant = FinsibleTextColorVariant.Accent,
        )
        content()
    }
}
