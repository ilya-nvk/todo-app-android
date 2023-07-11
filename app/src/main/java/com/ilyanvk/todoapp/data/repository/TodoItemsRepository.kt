package com.ilyanvk.todoapp.data.repository

import com.ilyanvk.todoapp.data.TodoItem
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface representing the repository for managing [TodoItem] objects.
 *
 * The [TodoItemsRepository] interface defines methods for accessing and manipulating [TodoItem] objects,
 * as well as synchronization operations with the data sources.
 */
interface TodoItemsRepository {
    val todoItemListFlow: StateFlow<List<TodoItem>>
    val todoItemList: List<TodoItem>
    suspend fun addTodoItem(todoItem: TodoItem)
    suspend fun updateTodoItem(todoItem: TodoItem)
    suspend fun deleteTodoItemById(id: String)
    suspend fun getTodoItemById(id: String): TodoItem

    suspend fun syncFlowList()
    suspend fun syncDataSources()
}
