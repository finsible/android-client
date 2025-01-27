package com.itsjeel01.finsiblefrontend.ui.view.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.itsjeel01.finsiblefrontend.ui.theme.finsibleTextFieldColors
import com.itsjeel01.finsiblefrontend.ui.theme.getCategoryColorsList

@SuppressLint("RememberReturnType")
@Composable
fun NewCategoryAlertDialog(
    dialogTitle: String,
    availableColors: List<Color>,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {

    var selectedColor by remember { mutableStateOf(availableColors.first()) }
    var categoryName by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val dialogProperties = DialogProperties(
        usePlatformDefaultWidth = false,
        decorFitsSystemWindows = true,
        dismissOnClickOutside = true,
        dismissOnBackPress = false
    )

    AlertDialog(
        modifier = Modifier.fillMaxWidth(0.9f),
        properties = dialogProperties,
        containerColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(20.dp),
        onDismissRequest = onDismissRequest,
        confirmButton = {
            ConfirmButton(
                onConfirmation = onConfirmation,
                selectedColor = selectedColor
            )
        },
        dismissButton = { DismissButton(onDismissRequest) },
        title = {
            Text(text = dialogTitle, fontSize = 18.sp)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
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
                                .size(if (color == selectedColor) 24.dp else 16.dp)
                                .clip(CircleShape)
                                .background(color)
                                .clickable { selectedColor = color },
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { Text(text = "Category Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    singleLine = true,
                    colors = finsibleTextFieldColors(accentColor = selectedColor)
                )
            }
        }
    )
}

@Composable
fun ConfirmButton(onConfirmation: () -> Unit, selectedColor: Color) {
    TextButton(
        onClick = onConfirmation,
        colors = ButtonDefaults.textButtonColors().copy(
            containerColor = selectedColor.copy(alpha = 0.5F),
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Text("OK", fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DismissButton(onDismissRequest: () -> Unit) {
    TextButton(onClick = onDismissRequest) {
        Text("Cancel", color = MaterialTheme.colorScheme.onSecondaryContainer)
    }
}

@Composable
@Preview
fun NewCategoryAlertDialogPreview() {
    NewCategoryAlertDialog(
        dialogTitle = "Add New Category",
        availableColors = getCategoryColorsList(),
        onDismissRequest = {},
        onConfirmation = {}
    )
}
