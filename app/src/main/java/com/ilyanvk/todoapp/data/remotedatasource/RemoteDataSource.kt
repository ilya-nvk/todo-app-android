package com.ilyanvk.todoapp.data.remotedatasource

import com.ilyanvk.todoapp.data.TodoItem

interface RemoteDataSource {
    suspend fun getTodoItemList(): List<TodoItem>
    suspend fun getCurrentRevision(): Int
    suspend fun updateTodoItem(todoItem: TodoItem)
    suspend fun addTodoItem(revision: Int, todoItem: TodoItem)
    suspend fun deleteTodoItemById(id: String)
    suspend fun updateTodoItemList(todoItemList: List<TodoItem>)
}
