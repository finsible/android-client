package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun NewTransactionCtaSection(
    isSaving: Boolean,
    onSaveAndAddMore: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.gapMd)
    ) {
        FinsibleButton(
            text = "Save + Add More",
            onClick = onSaveAndAddMore,
            variant = FinsibleButtonVariant.Outlined,
            shapeVariant = FinsibleShape.Rounded,
            fullWidth = true,
            loading = false,
            enabled = !isSaving,
            modifier = Modifier.weight(1f)
        )
        FinsibleButton(
            text = "Save",
            onClick = onSave,
            shapeVariant = FinsibleShape.Rounded,
            fullWidth = true,
            loading = isSaving,
            enabled = !isSaving,
            modifier = Modifier.weight(1f)
        )
    }
}

