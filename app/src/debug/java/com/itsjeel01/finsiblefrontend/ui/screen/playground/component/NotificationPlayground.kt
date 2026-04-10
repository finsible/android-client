package com.itsjeel01.finsiblefrontend.ui.screen.playground.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleNotificationPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleNotificationVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.util.FinsibleNotificationStateConfig
import com.itsjeel01.finsiblefrontend.ui.component.templates.util.LocalFinsibleNotification
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.FinsibleLabeledTextField
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.OptionDropdown
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.OptionToggle
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun NotificationPlayground() {
    val notificationManager = LocalFinsibleNotification.current

    var variant by rememberSaveable { mutableStateOf(FinsibleNotificationVariant.Info) }
    var position by rememberSaveable { mutableStateOf(FinsibleNotificationPosition.Top) }

    var title by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("System Update Available"))
    }
    var subtitle by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("Please restart the app to apply the latest changes and improvements."))
    }
    var actionLabel by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("Update Now"))
    }

    var showSubtitle by rememberSaveable { mutableStateOf(true) }
    var hasAction by rememberSaveable { mutableStateOf(false) }
    var autoDismiss by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = FinsibleTheme.dimes.d16, vertical = FinsibleTheme.dimes.d12),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d16)
    ) {
        OptionDropdown(
            label = "Notification Variant",
            selectedLabel = variant.name,
            options = FinsibleNotificationVariant.entries,
            optionLabel = { it.name },
            onSelect = { variant = it }
        )

        OptionDropdown(
            label = "Display Position",
            selectedLabel = position.name,
            options = FinsibleNotificationPosition.entries,
            optionLabel = { it.name },
            onSelect = { position = it }
        )

        FinsibleLabeledTextField(
            label = "Title",
            value = title,
            onValueChange = { title = it }
        )

        OptionToggle(
            label = "Include Subtitle",
            checked = showSubtitle,
            onCheckedChange = { showSubtitle = it }
        )

        if (showSubtitle) {
            FinsibleLabeledTextField(
                label = "Subtitle",
                value = subtitle,
                onValueChange = { subtitle = it }
            )
        }

        OptionToggle(
            label = "Auto Dismiss (5s Timer)",
            checked = autoDismiss,
            onCheckedChange = { autoDismiss = it },
            helperText = "Shows the progress bar and dismisses automatically."
        )

        OptionToggle(
            label = "Include Action Button",
            checked = hasAction,
            onCheckedChange = { hasAction = it }
        )

        if (hasAction) {
            FinsibleLabeledTextField(
                label = "Action Label",
                value = actionLabel,
                onValueChange = { actionLabel = it }
            )
        }

        FinsibleButton(
            text = "Trigger Notification",
            fullWidth = true,
            onClick = {
                // Determine the correct builder method to use
                val sub = if (showSubtitle) subtitle.text.takeIf { it.isNotBlank() } else null
                val actionText = if (hasAction) actionLabel.text.takeIf { it.isNotBlank() } else null

                val actionLambda: (() -> Unit)? = if (hasAction) {
                    { println("Playground: Notification Action Clicked!") }
                } else null

                // Call the unified show method with the config
                notificationManager.show(
                    FinsibleNotificationStateConfig(
                        title = title.text.ifBlank { "Default Title" },
                        subtitle = sub,
                        variant = variant,
                        position = position,
                        autoDismiss = autoDismiss,
                        actionLabel = actionText,
                        onAction = actionLambda
                    )
                )
            }
        )
    }
}