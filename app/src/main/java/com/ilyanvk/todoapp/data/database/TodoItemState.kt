package com.ilyanvk.todoapp.data.database

import com.ilyanvk.todoapp.data.Priority
import com.ilyanvk.todoapp.data.TodoItem

data class TodoItemState(
    val todoItems: List<TodoItem> = emptyList(),
    val text: String = "",
    val priority: Priority = Priority.MEDIUM,
    val deadline: Long? = null,
    val isCompleted: Boolean = false,
    val creationTime: Long = System.currentTimeMillis(),
    val isAddingTodoItem: Boolean = false,
    val showCompleted: Boolean = true
)