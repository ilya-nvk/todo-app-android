package com.ilyanvk.todoapp.ui.todoeditor


sealed class TodoEditorAction {
    object CloseEditor : TodoEditorAction()
    object Save : TodoEditorAction()
    object ChangeText : TodoEditorAction()
    object UpdateDeadlinePresence : TodoEditorAction()
    object Delete : TodoEditorAction()
}
