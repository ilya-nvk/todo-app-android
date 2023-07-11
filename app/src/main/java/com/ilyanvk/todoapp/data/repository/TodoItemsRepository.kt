package com.ilyanvk.todoapp.data.repository

import androidx.lifecycle.LiveData
import com.ilyanvk.todoapp.data.TodoItem

/**
 * Interface representing the repository for managing [TodoItem] objects.
 *
 * The [TodoItemsRepository] interface defines methods for accessing and manipulating [TodoItem] objects,
 * as well as synchronization operations with the data sources.
 */
interface TodoItemsRepository {
    val todoItemList: LiveData<List<TodoItem>>
    suspend fun addTodoItem(todoItem: TodoItem)
    suspend fun updateTodoItem(todoItem: TodoItem)
    suspend fun deleteTodoItemById(id: String)
    suspend fun getTodoItemById(id: String): TodoItem

    suspend fun syncList()
    suspend fun syncDataSources()
}
