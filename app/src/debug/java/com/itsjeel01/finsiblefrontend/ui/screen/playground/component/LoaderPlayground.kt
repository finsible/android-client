package com.itsjeel01.finsiblefrontend.ui.screen.playground.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.TextFieldValue
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleLoader
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleLoaderSpeed
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleOverlayOpacity
import com.itsjeel01.finsiblefrontend.ui.component.templates.util.LocalFinsibleLoader
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.FinsibleLabeledTextField
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.OptionDropdown
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.sizeLabel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoaderPlayground() {
    // Inline Loader State
    var size by rememberSaveable { mutableStateOf(FinsibleSize.Large) }
    var speed by rememberSaveable { mutableStateOf(FinsibleLoaderSpeed.Normal) }

    // Full Screen Overlay State
    var overlayOpacity by rememberSaveable { mutableStateOf(FinsibleOverlayOpacity.Regular) }
    var overlayMessage by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("Fetching your data..."))
    }

    val loaderManager = LocalFinsibleLoader.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = FinsibleTheme.spacing.insetLg, vertical = FinsibleTheme.spacing.gapMd),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackLg)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(FinsibleTheme.spacing.gapMd))
                .background(FinsibleTheme.colors.surfaceBase)
                .padding(vertical = FinsibleTheme.sizes.touch.md),
            contentAlignment = Alignment.Center
        ) {
            FinsibleLoader(
                size = size,
                speed = speed
            )
        }

        OptionDropdown(
            label = "Loader Size",
            selectedLabel = sizeLabel(size),
            options = FinsibleSize.entries,
            optionLabel = { sizeLabel(it) },
            onSelect = { size = it }
        )

        OptionDropdown(
            label = "Animation Speed",
            selectedLabel = speed.name,
            options = FinsibleLoaderSpeed.entries,
            optionLabel = { it.name },
            onSelect = { speed = it }
        )

        FinsibleLabeledTextField(
            label = "Overlay Message",
            value = overlayMessage,
            onValueChange = { overlayMessage = it }
        )

        OptionDropdown(
            label = "Overlay Opacity",
            selectedLabel = overlayOpacity.name,
            options = FinsibleOverlayOpacity.entries,
            optionLabel = { it.name },
            onSelect = { overlayOpacity = it },
            helperText = "Semantic token transparency for the full-screen scrim"
        )

        FinsibleButton(
            text = "Show Full Screen Overlay (3s)",
            fullWidth = true,
            onClick = {
                // Launch overlay and hide automatically after 3 seconds
                coroutineScope.launch {
                    loaderManager.show(
                        message = overlayMessage.text.takeIf { it.isNotBlank() },
                        opacity = overlayOpacity
                    )
                    delay(3000)
                    loaderManager.hide()
                }
            }
        )
    }
}