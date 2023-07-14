package com.ilyanvk.todoapp.ui.todoeditor.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilyanvk.todoapp.data.Priority
import com.ilyanvk.todoapp.data.TodoItem
import com.ilyanvk.todoapp.ui.theme.AppTheme
import com.ilyanvk.todoapp.ui.todoeditor.TodoEditorAction
import com.ilyanvk.todoapp.ui.todoeditor.compose.components.TodoEditorDeadlineField
import com.ilyanvk.todoapp.ui.todoeditor.compose.components.TodoEditorDeleteField
import com.ilyanvk.todoapp.ui.todoeditor.compose.components.TodoEditorDivider
import com.ilyanvk.todoapp.ui.todoeditor.compose.components.TodoEditorPriorityField
import com.ilyanvk.todoapp.ui.todoeditor.compose.components.TodoEditorTextField
import com.ilyanvk.todoapp.ui.todoeditor.compose.components.TodoEditorTopBar

@Composable
fun TodoEditorScreen(
    todoItem: TodoItem,
    isEditing: Boolean,
    onAction: (TodoEditorAction) -> Unit
) {
    val text = todoItem.text
    val priority = todoItem.priority
    val deadline = todoItem.deadline

    Scaffold(
        topBar = { TodoEditorTopBar(text = text, onAction = onAction) },
        containerColor = AppTheme.colors.backPrimary
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                TodoEditorTextField(text = text, onAction = onAction)
                TodoEditorPriorityField(priority = priority, onAction = onAction)
                TodoEditorDivider(PaddingValues(horizontal = 16.dp))
                TodoEditorDeadlineField(deadline = deadline, onAction = onAction)
                TodoEditorDivider(PaddingValues(top = 24.dp, bottom = 8.dp))
                TodoEditorDeleteField(
                    enabled = isEditing || todoItem.text.isNotEmpty(),
                    onAction = onAction
                )
                Spacer(Modifier.size(33.dp))
            }
        }
    }
}

@Preview
@Composable
fun LightPreviewTodoEditor() {
    AppTheme(darkTheme = false) {
        TodoEditorScreen(
            previewTodoItem,
            isEditing = true,
            onAction = {})
    }
}

@Preview
@Composable
fun DarkPreviewTodoEditor() {
    AppTheme(darkTheme = true) {
        TodoEditorScreen(
            previewTodoItem,
            isEditing = true,
            onAction = {})
    }
}

val previewTodoItem = TodoItem(
    text = "Парампампап",
    priority = Priority.HIGH,
    deadline = System.currentTimeMillis()
)
