package com.itsjeel01.finsiblefrontend.ui.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun HelpRow(
    text: String,
    icon: @Composable (() -> Unit)?,
    textColor: Color,
    textStyle: TextStyle,
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
    ) {
        icon?.let {
            Box(
                modifier = Modifier
                    .padding(end = 4.dp, top = 2.dp)
                    .size(12.dp)
            ) {
                it()
            }
        }
        Text(
            text = text,
            style = textStyle,
            color = textColor
        )
    }
}