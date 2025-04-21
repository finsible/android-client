package com.itsjeel01.finsiblefrontend.ui.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.itsjeel01.finsiblefrontend.ui.view.InputCommonProps

@Composable
fun NewCategoryAlertDialog(
    dialogTitle: String,
    availableColors: List<Color>,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    var selectedColor by remember { mutableStateOf(availableColors.firstOrNull() ?: Color.Gray) }
    var categoryName by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    // Auto-focus the input field when dialog appears
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    AlertDialog(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            ),
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = true,
            dismissOnClickOutside = true,
            dismissOnBackPress = false
        ),
        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
        shape = RoundedCornerShape(8.dp),
        onDismissRequest = onDismissRequest,
        confirmButton = {
            ConfirmButton(
                onConfirmation = {
                    val validationError = validateCategoryName(categoryName)
                    if (validationError == null) {
                        onConfirmation()
                    } else {
                        showError = true
                        errorMessage = validationError
                    }
                },
                selectedColor = selectedColor
            )
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(
                    "Cancel",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        },
        title = {
            Text(
                text = dialogTitle,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            CategoryFormContent(
                availableColors = availableColors,
                selectedColor = selectedColor,
                onColorSelected = { selectedColor = it },
                categoryName = categoryName,
                onCategoryNameChange = { newName ->
                    categoryName = newName.take(30)
                    errorMessage = validateCategoryName(newName) ?: ""
                    showError = errorMessage.isNotEmpty()
                },
                showError = showError,
                errorMessage = errorMessage,
                focusRequester = focusRequester,
                onDoneAction = { onDismissRequest() }
            )
        }
    )
}

@Composable
private fun CategoryFormContent(
    availableColors: List<Color>,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    categoryName: String,
    onCategoryNameChange: (String) -> Unit,
    showError: Boolean,
    errorMessage: String,
    focusRequester: FocusRequester,
    onDoneAction: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Color selection row
        ColorSelectionRow(
            availableColors = availableColors,
            selectedColor = selectedColor,
            onColorSelected = onColorSelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Category name input
        CategoryNameInput(
            categoryName = categoryName,
            onCategoryNameChange = onCategoryNameChange,
            showError = showError,
            errorMessage = errorMessage,
            focusRequester = focusRequester,
            onDoneAction = onDoneAction,
            accentColor = selectedColor
        )
    }
}

@Composable
private fun CategoryNameInput(
    categoryName: String,
    onCategoryNameChange: (String) -> Unit,
    showError: Boolean,
    errorMessage: String,
    focusRequester: FocusRequester,
    onDoneAction: () -> Unit,
    accentColor: Color,
) {
    FinsibleTextInput(
        value = categoryName,
        onValueChange = onCategoryNameChange,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            autoCorrectEnabled = true,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { onDoneAction() }),
        commonProps = InputCommonProps(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            label = "Category Name",
            errorText = errorMessage,
            isError = showError,
            accentColor = accentColor,
        )
    )
}

fun validateCategoryName(name: String): String? {
    if (name.isBlank()) return "Please enter a name"
    if (name.length >= 30) return "Please enter a name under 30 characters"
    return null
}

@Composable
private fun ColorSelectionRow(
    availableColors: List<Color>,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        availableColors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(color)
                    .clickable { onColorSelected(color) },
                contentAlignment = Alignment.Center
            ) {
                if (color == selectedColor) {
                    Text(
                        text = "✓",
                        color = MaterialTheme.colorScheme.background,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
private fun ConfirmButton(
    onConfirmation: () -> Unit,
    selectedColor: Color,
    isEnabled: Boolean = true,
) {
    TextButton(
        onClick = onConfirmation,
        colors = ButtonDefaults.textButtonColors(
            containerColor = selectedColor.copy(alpha = 0.5F),
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = selectedColor.copy(alpha = 0.2F),
            disabledContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5F)
        ),
        enabled = isEnabled
    ) {
        Text(
            "Add",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelMedium
        )
    }
}