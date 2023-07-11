package com.ilyanvk.todoapp.ui.todoeditor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var menuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .clickable { menuExpanded = true }
    ) {
        PriorityDropdownMenu(
            expanded = menuExpanded,
            closeMenu = { menuExpanded = false },
            onAction = onAction
        )

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
    }
}

@Composable
fun PriorityDropdownMenu(
    expanded: Boolean,
    closeMenu: () -> Unit,
    onAction: (TodoEditorAction) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = closeMenu,
        modifier = Modifier.background(
            color = AppTheme.colors.backElevated
        )
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.no),
                    style = AppTheme.typography.body,
                    color = AppTheme.colors.labelPrimary
                )
            },
            onClick = {
                onAction(TodoEditorAction.UpdatePriority(Priority.MEDIUM))
                closeMenu()
            })

        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.low),
                    style = AppTheme.typography.body
                )
            },
            onClick = {
                onAction(TodoEditorAction.UpdatePriority(Priority.LOW))
                closeMenu()
            },
            colors = MenuDefaults.itemColors(
                textColor = AppTheme.colors.labelPrimary
            )
        )

        DropdownMenuItem(
            text = {
                Text(
                    text = "!! " + stringResource(R.string.high),
                    style = AppTheme.typography.body,
                )
            },
            onClick = {
                onAction(TodoEditorAction.UpdatePriority(Priority.HIGH))
                closeMenu()
            },
            colors = MenuDefaults.itemColors(
                textColor = AppTheme.colors.colorRed
            )
        )
    }
}


@Preview
@Composable
fun PreviewTodoEditorPriorityField() {
    AppTheme {
        TodoEditorPriorityField(priority = Priority.MEDIUM) {}
    }
}
