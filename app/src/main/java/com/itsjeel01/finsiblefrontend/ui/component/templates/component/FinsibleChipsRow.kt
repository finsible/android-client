package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleChipsRowDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleChipsRowConfig

/** A container that lays out chip content either in a wrapping FlowRow or a horizontal LazyRow.
 *
 * @param chips The list of chips to display.
 * @param modifier Optional [Modifier] for this container.
 * @param wrap Decides whether to render chips in a horizontal scrollable row or a FlowRow wrapped at screen width.
 * @param chipKeys The list of keys associated with each chip.
 * @param config The configuration for this container.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FinsibleChipsRow(
    chips: List<@Composable () -> Unit>,
    modifier: Modifier = Modifier,
    wrap: Boolean = true,
    chipKeys: List<Any>? = null,
    config: FinsibleChipsRowConfig = FinsibleChipsRowDefaults.config()
) {
    if (chipKeys != null) {
        require(chipKeys.size == chips.size) {
            "chipKeys size must match chips size when provided."
        }
    }

    if (wrap) {
        FlowRow(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(config.horizontalSpacing),
            verticalArrangement = Arrangement.spacedBy(config.verticalSpacing),
        ) {
            chips.forEachIndexed { index, chip ->
                val chipKey = chipKeys?.get(index) ?: index
                key(chipKey) {
                    chip()
                }
            }
        }
    } else {
        LazyRow(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(config.horizontalSpacing),
        ) {
            itemsIndexed(
                items = chips,
                key = { index, _ -> chipKeys?.get(index) ?: index }
            ) { _, chip ->
                chip()
            }
        }
    }
}



