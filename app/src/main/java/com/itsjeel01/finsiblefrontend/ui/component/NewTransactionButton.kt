package com.itsjeel01.finsiblefrontend.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import com.itsjeel01.finsiblefrontend.ui.data.NavBarItem
import com.itsjeel01.finsiblefrontend.ui.theme.dime.Size
import com.itsjeel01.finsiblefrontend.ui.theme.dime.appDimensions
import com.itsjeel01.finsiblefrontend.ui.theme.dime.paddingAll
import com.itsjeel01.finsiblefrontend.ui.theme.dime.size

@Composable
fun NewTransactionButton(navBarItem: NavBarItem) {
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
                elevation = appDimensions().size(Size.S16),
                shape = CircleShape,
                ambientColor = MaterialTheme.colorScheme.primary,
                spotColor = MaterialTheme.colorScheme.primary,
            )
            .paddingAll(Size.S16)
    ) {
        Icon(
            painter = painterResource(id = navBarItem.icon),
            tint = MaterialTheme.colorScheme.background,
            modifier = Modifier.size(Size.S24),
            contentDescription = navBarItem.label
        )
    }
}
