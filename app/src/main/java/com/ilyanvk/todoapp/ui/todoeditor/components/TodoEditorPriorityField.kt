package com.ilyanvk.todoapp.ui.todoeditor.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.data.Priority
import com.ilyanvk.todoapp.ui.theme.AppTheme
import com.ilyanvk.todoapp.ui.todoeditor.TodoEditorAction

@Composable
fun TodoEditorPriorityField(
    priority: Priority,
    onAction: (TodoEditorAction) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.priority),
            style = AppTheme.typography.body,
            color = AppTheme.colors.labelPrimary
        )
        Text(
            text = stringResource(
                id = when (priority) {
                    Priority.HIGH -> R.string.high
                    Priority.LOW -> R.string.low
                    else -> R.string.no
                }
            ),
            style = AppTheme.typography.subhead,
            color = AppTheme.colors.labelSecondary
        )

        //todo dropdown menu
    }
}

@Preview
@Composable
fun PreviewTodoEditorPriorityField() {
    AppTheme {
        TodoEditorPriorityField(priority = Priority.MEDIUM) {}
    }
}
