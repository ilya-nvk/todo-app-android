package com.ilyanvk.todoapp.ui.todoeditor.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ilyanvk.todoapp.ui.theme.AppTheme

@Composable
fun TodoEditorDivider(padding: PaddingValues) {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding),
        thickness = 0.5.dp,
        color = AppTheme.colors.supportSeparator
    )
}

