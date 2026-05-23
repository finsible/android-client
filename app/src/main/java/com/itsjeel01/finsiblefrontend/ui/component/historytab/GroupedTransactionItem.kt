package com.itsjeel01.finsiblefrontend.ui.component.historytab

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.TransactionUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Card for grouped mode — no date line. */
@Composable
fun GroupedTransactionItem(
    transaction: TransactionUIModel,
    isFirst: Boolean,
    isLast: Boolean
) {
    val cornerRadius = FinsibleTheme.spacing.insetLg
    val shape = when {
        isFirst && isLast -> RoundedCornerShape(cornerRadius)
        isFirst -> RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius)
        isLast -> RoundedCornerShape(bottomStart = cornerRadius, bottomEnd = cornerRadius)
        else -> RectangleShape
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(FinsibleTheme.colors.surfaceDefault)
            .border(width = FinsibleTheme.stroke.thin, color = FinsibleTheme.colors.borderSubtle, shape = shape)
    ) {
        Column {
            TransactionListItem(
                transaction = transaction,
                modifier = Modifier.padding(horizontal = FinsibleTheme.spacing.insetLg)
            )
            if (!isLast) {
                HorizontalDivider(
                    color = FinsibleTheme.colors.borderSubtle,
                    thickness = FinsibleTheme.stroke.thin,
                    modifier = Modifier.padding(horizontal = FinsibleTheme.spacing.insetLg)
                )
            }
        }
    }
}