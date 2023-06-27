package com.ilyanvk.todoapp.data

import com.ilyanvk.todoapp.data.database.TodoItemEvent
import com.ilyanvk.todoapp.data.database.TodoItemState

object TodoItemsRepository {
    lateinit var state: TodoItemState
    lateinit var onEvent: (TodoItemEvent) -> Unit

    var toEdit: TodoItem? = null
    var onRepositoryUpdate: () -> Unit = {}
    var showCompleted = true
        set(value) {
            field = value
            onEvent(TodoItemEvent.ShowCompleted(value))
            onRepositoryUpdate()
        }

    fun getTodoItems(): List<TodoItem> {
        return state.todoItems
    }

    fun countCompletedTodoItems(): Int {
        return getTodoItems().count { it.isCompleted }
    }

    fun addTodoItem(todoItem: TodoItem) {
        onEvent(
            TodoItemEvent.SetInfo(
                todoItem.text,
                todoItem.isCompleted,
                todoItem.priority,
                todoItem.deadline,
                todoItem.creationDate
            )
        )
        onEvent(TodoItemEvent.SaveTodoItem)
        onRepositoryUpdate()
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        onEvent(TodoItemEvent.DeleteTodoItem(todoItem))
        onRepositoryUpdate()
    }
}
