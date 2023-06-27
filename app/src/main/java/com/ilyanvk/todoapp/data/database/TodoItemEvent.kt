package com.ilyanvk.todoapp.data.database

import com.ilyanvk.todoapp.data.Priority
import com.ilyanvk.todoapp.data.TodoItem

sealed interface TodoItemEvent {
    object SaveTodoItem : TodoItemEvent

    data class SetInfo(
        val text: String,
        val isCompleted: Boolean,
        val priority: Priority?,
        val deadline: Long?,
        val creationDate: Long
    ) : TodoItemEvent

    object ShowDialog : TodoItemEvent

    object HideDialog : TodoItemEvent

    data class ShowCompleted(val showCompleted: Boolean) : TodoItemEvent

    data class DeleteTodoItem(val todoItem: TodoItem) : TodoItemEvent
}