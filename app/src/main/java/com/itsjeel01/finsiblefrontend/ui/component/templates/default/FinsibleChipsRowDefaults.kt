package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.runtime.Composable
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleChipsRowConfig
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Defaults for chip row spacing. */
object FinsibleChipsRowDefaults {
    @Composable
    fun config(): FinsibleChipsRowConfig {
        val s = FinsibleTheme.spacing
        return FinsibleChipsRowConfig(
            horizontalSpacing = s.gapSm,
            verticalSpacing = s.stackSm,
        )
    }
}
