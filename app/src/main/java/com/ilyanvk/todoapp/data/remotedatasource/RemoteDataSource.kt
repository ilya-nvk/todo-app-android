package com.ilyanvk.todoapp.data.remotedatasource

import com.ilyanvk.todoapp.data.TodoItem


/**
 * Interface representing the remote data source for the [TodoItem] objects.
 *
 * The [RemoteDataSource] interface defines methods
 * for interacting with the remote API to perform CRUD operations.
 */
interface RemoteDataSource {
    suspend fun getTodoItemList(): List<TodoItem>
    suspend fun getCurrentRevision(): Int
    suspend fun updateTodoItem(todoItem: TodoItem)
    suspend fun addTodoItem(revision: Int, todoItem: TodoItem)
    suspend fun deleteTodoItemById(id: String)
    suspend fun updateTodoItemList(todoItemList: List<TodoItem>)
}
