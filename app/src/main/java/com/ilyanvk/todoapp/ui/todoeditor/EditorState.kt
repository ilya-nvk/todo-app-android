package com.ilyanvk.todoapp.ui.todoeditor

import com.ilyanvk.todoapp.data.TodoItem

sealed class EditorState {
    class Editing(val todoItem: TodoItem) : EditorState()
    object Creating : EditorState()
}
