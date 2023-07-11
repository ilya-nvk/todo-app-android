package com.ilyanvk.todoapp.data.localdatasource

import com.ilyanvk.todoapp.data.TodoItem

/**
 * Interface representing the local [TodoItem] data source.
 *
 * The [LocalDataSource] interface defines methods for interacting with the database
 *
 * to perform CRUD (Create, Read, Update, Delete) operations on the objects of [TodoItem].
 */
interface LocalDataSource {
    suspend fun getTodoItemList(): List<TodoItem>
    suspend fun getTodoItemById(id: String): TodoItem
    suspend fun addTodoItem(todoItem: TodoItem)
    suspend fun addTodoItemList(todoItemList: List<TodoItem>)
    suspend fun updateTodoItem(todoItem: TodoItem)
    suspend fun deleteTodoItemById(id: String)
    suspend fun forceUpdateTodoItemList(todoItem: List<TodoItem>)
}
