package com.itsjeel01.finsiblefrontend.ui.component.templates.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle



/** Immutable holder for resolved text styling. */
@Immutable
data class FinsibleTextStyleSpec(
    val textStyle: TextStyle,
    val color: Color,
    val uppercase: Boolean,
)


