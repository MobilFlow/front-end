// SpecialtyChip.kt
package com.edu.pe.automatch.presentation.components

import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun SpecialtyChip(
    label: String,
    onClick: () -> Unit = {}
) {

    AssistChip(
        onClick = onClick,
        label = {
            Text(text = label)
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = Color(0xFFF2F2F2),
            labelColor = MaterialTheme.colorScheme.onSurface
        )
    )
}