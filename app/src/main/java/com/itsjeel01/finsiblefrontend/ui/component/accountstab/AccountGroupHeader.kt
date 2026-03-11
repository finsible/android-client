package com.itsjeel01.finsiblefrontend.ui.component.accountstab

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold

@Composable
fun AccountGroupHeader(groupName: String) {
    Text(
        text = groupName.uppercase(),
        style = FinsibleTheme.typography.t12.semiBold(),
        color = FinsibleTheme.colors.secondaryContent,
        modifier = Modifier.padding(
            start = FinsibleTheme.dimes.d8,
            top = FinsibleTheme.dimes.d8,
            bottom = FinsibleTheme.dimes.d4
        )
    )
}
