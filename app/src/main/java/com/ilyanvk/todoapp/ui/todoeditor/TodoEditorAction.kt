package com.ilyanvk.todoapp.ui.todoeditor


sealed class TodoEditorAction {
    object CloseEditor : TodoEditorAction()
    object SaveTodoItem : TodoEditorAction()
}

