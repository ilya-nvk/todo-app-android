package com.ilyanvk.todoapp.data.localdatasource

import com.ilyanvk.todoapp.data.TodoItem
import com.ilyanvk.todoapp.data.localdatasource.room.TodoItemDatabase
import com.ilyanvk.todoapp.data.localdatasource.room.TodoItemEntity
import javax.inject.Inject

/**
 * Implementation of the [LocalDataSource] interface.
 *
 * @param database the Room database.
 */
class LocalDataSourceImpl @Inject constructor(
    private val database: TodoItemDatabase
) : LocalDataSource {
    override suspend fun getTodoItemList(): List<TodoItem> {
        return database.dao.getAll().map { it.toTodoItem() }
    }

    override suspend fun getTodoItemById(id: String): TodoItem {
        return database.dao.getById(id).toTodoItem()
    }

    override suspend fun addTodoItem(todoItem: TodoItem) {
        database.dao.add(TodoItemEntity.fromTodoItem(todoItem))
    }

    override suspend fun addTodoItemList(todoItemList: List<TodoItem>) {
        database.dao.addAll(todoItemList.map { TodoItemEntity.fromTodoItem(it) })
    }

    override suspend fun updateTodoItem(todoItem: TodoItem) {
        database.dao.update(TodoItemEntity.fromTodoItem(todoItem))
    }

    override suspend fun deleteTodoItemById(id: String) {
        return database.dao.deleteById(id)
    }

    override suspend fun forceUpdateTodoItemList(todoItem: List<TodoItem>) {
        database.dao.clear()
        database.dao.addAll(todoItem.map { TodoItemEntity.fromTodoItem(it) })
    }

}
