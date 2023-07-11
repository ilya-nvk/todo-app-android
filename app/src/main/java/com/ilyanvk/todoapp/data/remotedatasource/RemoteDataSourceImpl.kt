package com.ilyanvk.todoapp.data.remotedatasource

import com.ilyanvk.todoapp.data.TodoItem
import com.ilyanvk.todoapp.data.remotedatasource.retrofit.TodoItemApi
import com.ilyanvk.todoapp.data.remotedatasource.retrofit.models.TodoItemApiRequest
import com.ilyanvk.todoapp.data.remotedatasource.retrofit.models.TodoItemApiRequestList
import com.ilyanvk.todoapp.data.remotedatasource.retrofit.models.TodoItemServer
import com.ilyanvk.todoapp.data.sharedpreferences.SharedPreferencesDataSource
import javax.inject.Inject

/**
 * Implementation of the [RemoteDataSource] interface that interacts with the [TodoItemApi].
 */
class RemoteDataSourceImpl @Inject constructor(
    private val api: TodoItemApi,
    private val sharedPreferencesDataSource: SharedPreferencesDataSource
) : RemoteDataSource {
    override suspend fun getTodoItemList(): List<TodoItem> {
        val response = api.getAll()
        sharedPreferencesDataSource.revision = response.body()?.revision ?: 0
        return response.body()?.list?.map { it.toTodoItem() } ?: emptyList()
    }

    override suspend fun getCurrentRevision(): Int {
        return api.getAll().body()?.revision ?: 0
    }

    override suspend fun updateTodoItem(todoItem: TodoItem) {
        val itemToSend =
            TodoItemServer.fromTodoItem(todoItem, sharedPreferencesDataSource.getDeviceId()).copy(
                modificationDate = System.currentTimeMillis()
            )
        val response = api.update(
            sharedPreferencesDataSource.revision,
            todoItem.id,
            TodoItemApiRequest(itemToSend)
        )
        val currentRevision = response.body()?.revision ?: 0
        sharedPreferencesDataSource.revision = currentRevision
    }

    override suspend fun addTodoItem(revision: Int, todoItem: TodoItem) {
        val itemToSend =
            TodoItemServer.fromTodoItem(todoItem, sharedPreferencesDataSource.getDeviceId())
        val response = api.add(revision, TodoItemApiRequest(itemToSend))
        val currentRevision = response.body()?.revision ?: 0
        sharedPreferencesDataSource.revision = currentRevision
    }

    override suspend fun deleteTodoItemById(id: String) {
        val response = api.delete(sharedPreferencesDataSource.revision, id)
        val currentRevision = response.body()?.revision ?: 0
        sharedPreferencesDataSource.revision = currentRevision
    }

    override suspend fun updateTodoItemList(todoItemList: List<TodoItem>) {
        val requestList = TodoItemApiRequestList(
            "ok",
            todoItemList.map {
                TodoItemServer.fromTodoItem(
                    it,
                    sharedPreferencesDataSource.getDeviceId()
                )
            }
        )
        val response = api.updateList(sharedPreferencesDataSource.revision, requestList)
        val currentRevision = response.body()?.revision ?: 0
        sharedPreferencesDataSource.revision = currentRevision
    }
}
