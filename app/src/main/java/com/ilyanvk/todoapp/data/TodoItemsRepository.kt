package com.ilyanvk.todoapp.data

import com.ilyanvk.todoapp.data.database.TodoItemDao

object TodoItemsRepository {
    lateinit var dao: TodoItemDao
    var toEdit: TodoItem? = null
    var onRepositoryUpdate: () -> Unit = {}
    var showCompleted = true
        set(value) {
            field = value
            onRepositoryUpdate()
        }

    fun getTodoItems() = dao.getAll()

    suspend fun addTodoItem(todoItem: TodoItem) {
        dao.insert(
            TodoItem(
                text = todoItem.text,
                isCompleted = todoItem.isCompleted,
                priority = todoItem.priority,
                deadline = todoItem.deadline,
                creationDate = todoItem.creationDate
            )
        )
        onRepositoryUpdate()
    }

    fun countCompletedTodoItems(): Int {
        return dao.countCompleted()
    }

    suspend fun updateTodoItem(todoItem: TodoItem) {
        dao.update(todoItem)
        onRepositoryUpdate()
    }

    suspend fun deleteTodoItem(todoItem: TodoItem) {
        dao.delete(todoItem)
        onRepositoryUpdate()
    }

    suspend fun changeCompletionOfTodoItem(todoItem: TodoItem) {
        dao.update(todoItem.copy(isCompleted = !todoItem.isCompleted))
        onRepositoryUpdate()
    }
}
