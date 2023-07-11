package com.ilyanvk.todoapp.ui.todoeditor.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.ui.theme.AppTheme
import com.ilyanvk.todoapp.ui.todoeditor.TodoEditorAction

@Composable
fun TodoEditorDeleteField(
    enabled: Boolean,
    onAction: (TodoEditorAction) -> Unit
) {
    TextButton(
        onClick = { onAction(TodoEditorAction.Delete) },
        enabled = enabled,
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
        colors = ButtonDefaults.textButtonColors(
            contentColor = AppTheme.colors.colorRed,
            disabledContentColor = AppTheme.colors.labelDisable
        )
    ) {
        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        Spacer(modifier = Modifier.size(12.dp))
        Text(text = stringResource(id = R.string.delete), style = AppTheme.typography.body)
    }
}

@Preview
@Composable
fun PreviewTodoEditorDeleteField() {
    AppTheme {
        TodoEditorDeleteField(enabled = true, onAction = {})
    }
}
