package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import com.itsjeel01.finsiblefrontend.data.model.Currency
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleTextField
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold

@Composable
fun CurrencySheetContent(
    selectedCurrencyCode: String?,
    currencies: List<Currency>,
    accentColor: Color,
    surfaceColor: Color,
    onCurrencySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val sp = FinsibleTheme.spacing

    val focusManager = LocalFocusManager.current

    // OPTIMIZATION 1: Pre-compute the lowercase corpus ONCE when the currencies list changes.
    // This entirely removes string allocations and case-folding logic from the user's typing path.
    val searchCorpus = remember(currencies) {
        currencies.map { currency ->
            currency to "${currency.code.lowercase()} ${currency.name.lowercase()}"
        }
    }

    // OPTIMIZATION 2: Fast-path filtering using the pre-computed corpus.
    val filteredCurrencies = remember(searchQuery, searchCorpus) {
        if (searchQuery.isBlank()) {
            currencies
        } else {
            val query = searchQuery.trim().lowercase()
            searchCorpus
                .filter { (_, corpusString) -> corpusString.contains(query) }
                .map { (currency, _) -> currency }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // --- Search Bar ---
        FinsibleTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            size = FinsibleSize.Medium,
            shapeVariant = FinsibleShape.Circle,
            placeholder = "Search Currency",
            leadingIcon = {
                Icon(
                    painter = painterResource(id = com.composables.icons.tabler.outline.R.drawable.tabler_ic_search_outline),
                    contentDescription = "Search",
                    tint = FinsibleTheme.colors.contentSecondary
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    FinsibleButton(
                        onClick = { searchQuery = "" },
                        icon = {
                            Icon(
                                painter = painterResource(id = com.composables.icons.tabler.outline.R.drawable.tabler_ic_x_outline),
                                contentDescription = "Clear search"
                            )
                        },
                        iconOnly = true,
                        size = FinsibleSize.ExtraSmall,
                        variant = FinsibleButtonVariant.Text
                    )
                }
            },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )

        Spacer(modifier = Modifier.height(sp.insetLg))

        // --- Currency List ---
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(sp.insetXs) // Small gap between list items
        ) {
            // Using `key` optimizes LazyColumn recompositions when the list filters
            items(items = filteredCurrencies, key = { it.code }) { currency ->
                CurrencyListItem(
                    currency = currency,
                    isSelected = currency.code == selectedCurrencyCode,
                    accentColor = accentColor,
                    surfaceColor = surfaceColor,
                    onClick = { onCurrencySelected(currency.code) }
                )
            }
        }
    }
}

@Composable
private fun CurrencyListItem(
    currency: Currency,
    isSelected: Boolean,
    accentColor: Color,
    surfaceColor: Color,
    onClick: () -> Unit
) {
    val sp = FinsibleTheme.spacing
    val type = FinsibleTheme.typography

    // The translucent green background shown in the screenshot for the selected item
    val backgroundColor = if (isSelected) surfaceColor else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(sp.gapMd)) // Clip FIRST to constrain the ripple
            .background(backgroundColor)
            .clickable(onClick = onClick) // Clickable AFTER clip
            .padding(horizontal = sp.insetLg, vertical = sp.gapMd), // Inner padding
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(sp.insetLg),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Flag
            FinsibleText(
                text = currency.flagEmoji ?: "🏳️",
                textStyle = type.bodyLg // Larger text size for the emoji
            )

            // Code & Name
            Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackMicro)) {
                FinsibleText(
                    text = currency.code,
                    textStyle = type.bodyLg.semiBold(),
                    colorVariant = FinsibleTextColorVariant.Primary
                )
                FinsibleText(
                    text = currency.name,
                    textStyle = type.bodyMd,
                    colorVariant = FinsibleTextColorVariant.Secondary // Grayish text for the name
                )
            }
        }
    }
}