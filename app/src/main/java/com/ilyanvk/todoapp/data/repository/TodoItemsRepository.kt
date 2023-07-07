package com.ilyanvk.todoapp.data.repository

import com.ilyanvk.todoapp.data.TodoItem
import kotlinx.coroutines.flow.StateFlow

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
