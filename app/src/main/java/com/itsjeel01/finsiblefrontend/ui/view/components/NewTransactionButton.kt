package com.itsjeel01.finsiblefrontend.ui.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.data.client.BottomNavigationItems

@Composable
fun NewTransactionButton(navigationItem: BottomNavigationItems) {
    Box(
        modifier = Modifier
            .background(
                shape = CircleShape,
                brush = Brush.horizontalGradient(
                    endX = 200F,
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            )
            .shadow(
                elevation = 16.dp,
                shape = CircleShape,
                ambientColor = MaterialTheme.colorScheme.primary,
                spotColor = MaterialTheme.colorScheme.primary,
            )
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(id = navigationItem.icon),
            tint = MaterialTheme.colorScheme.background,
            modifier = Modifier.size(24.dp),
            contentDescription = navigationItem.label
        )
    }
}