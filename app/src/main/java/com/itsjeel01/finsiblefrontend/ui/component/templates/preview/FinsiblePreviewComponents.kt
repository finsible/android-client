package com.itsjeel01.finsiblefrontend.ui.component.templates.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold

/** Shared scaffold for long, section-based component previews. */
@Composable
fun FinsibleComponentPreviewScaffold(content: @Composable ColumnScope.() -> Unit) {
    FinsibleTheme {
        val dimes = FinsibleTheme.dimes

        Surface(color = FinsibleTheme.colors.primaryBackground) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = dimes.d8, horizontal = dimes.d16),
                verticalArrangement = Arrangement.spacedBy(dimes.d12),
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
    val dimes = FinsibleTheme.dimes

    Column(verticalArrangement = Arrangement.spacedBy(dimes.d12)) {
        Text(
            text = title,
            style = FinsibleTheme.typography.t20.semiBold(),
            color = FinsibleTheme.colors.brandAccent
        )
        content()
    }
}
