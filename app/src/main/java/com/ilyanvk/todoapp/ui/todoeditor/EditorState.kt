package com.ilyanvk.todoapp.ui.todoeditor

import com.ilyanvk.todoapp.data.TodoItem

/**
 * Sealed class representing the state of the [TodoItem] editor.
 *
 * The [EditorState] class defines two possible states:
 *
 * [Editing]: Represents the state of editing an existing [TodoItem].
 * It contains the reference to the [TodoItem] being edited.
 *
 * [Creating]: Represents the state of creating a new [TodoItem].
 */
sealed class EditorState {
    /**
     * State representing editing an existing [TodoItem].
     * @param todoItem The TodoItem being edited.
     */
    class Editing(val todoItem: TodoItem) : EditorState()

    /**
     * State representing creating a new [TodoItem].
     */
    object Creating : EditorState()
}
