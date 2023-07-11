package com.ilyanvk.todoapp.ui.todoeditor

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilyanvk.todoapp.data.Priority
import com.ilyanvk.todoapp.data.TodoItem
import com.ilyanvk.todoapp.ui.theme.AppTheme
import com.ilyanvk.todoapp.ui.todoeditor.components.TodoEditorDeadlineField
import com.ilyanvk.todoapp.ui.todoeditor.components.TodoEditorDeleteField
import com.ilyanvk.todoapp.ui.todoeditor.components.TodoEditorDivider
import com.ilyanvk.todoapp.ui.todoeditor.components.TodoEditorPriorityField
import com.ilyanvk.todoapp.ui.todoeditor.components.TodoEditorTextField
import com.ilyanvk.todoapp.ui.todoeditor.components.TodoEditorToolbar

@Composable
fun TodoEditorComposable(
    mode: EditorState,
    onAction: (TodoEditorAction) -> Unit
) {
    val text = if (mode is EditorState.Editing) mode.todoItem.text else ""
    val priority = if (mode is EditorState.Editing) mode.todoItem.priority else Priority.MEDIUM
    val deadline = if (mode is EditorState.Editing) mode.todoItem.deadline else null

    Scaffold(
        topBar = { TodoEditorToolbar(text = text, onAction = onAction) },
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
                TodoEditorDeleteField(enabled = mode is EditorState.Editing, onAction = onAction)
            }
        }
    }
}

@Preview
@Composable
fun LightPreviewTodoEditor() {
    AppTheme(darkTheme = false) {
        TodoEditorComposable(
            mode = editorState,
            onAction = {})
    }
}

@Preview
@Composable
fun DarkPreviewTodoEditor() {
    AppTheme(darkTheme = true) {
        TodoEditorComposable(
            mode = editorState,
            onAction = {})
    }
}

val previewTodoItem = TodoItem(
    text = "Парампампап",
    priority = Priority.HIGH,
    deadline = System.currentTimeMillis()
)

val editorState = EditorState.Editing(previewTodoItem)
