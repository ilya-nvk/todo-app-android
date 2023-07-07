package com.ilyanvk.todoapp.data.localdatasource

import com.ilyanvk.todoapp.data.TodoItem

interface LocalDataSource {
    suspend fun getTodoItemList(): List<TodoItem>
    suspend fun getTodoItemById(id: String): TodoItem
    suspend fun addTodoItem(todoItem: TodoItem)
    suspend fun addTodoItemList(todoItemList: List<TodoItem>)
    suspend fun updateTodoItem(todoItem: TodoItem)
    suspend fun deleteTodoItemById(id: String)
    suspend fun forceUpdateTodoItemList(todoItem: List<TodoItem>)
}
