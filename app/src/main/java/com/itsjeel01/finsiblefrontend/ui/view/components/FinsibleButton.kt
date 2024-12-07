package com.itsjeel01.finsiblefrontend.ui.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.R

@Composable
fun FinsibleButton(
    modifier: Modifier = Modifier,
    label: String,
    labelColor: Color = MaterialTheme.colorScheme.onPrimary,
    labelStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
    iconDrawable: Int? = null,
    onClick: () -> Unit,
    buttonBackground: Color = MaterialTheme.colorScheme.primary,
    cornerSize: Dp = 0.dp,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .background(
                color = buttonBackground,
                shape = RoundedCornerShape(cornerSize)
            )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = labelColor,
                style = labelStyle
            )
            iconDrawable?.let {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_right_icon),
                    tint = labelColor,
                    contentDescription = "Arrow Right Icon",
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
    }
}

@Composable
@Preview
fun ButtonPreview() {
    FinsibleButton(
        label = "Button Preview",
        onClick = { /*TODO*/ },
        modifier = Modifier.fillMaxWidth()
    )
}