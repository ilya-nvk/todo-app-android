package com.ilyanvk.todoapp.data.repository

import com.ilyanvk.todoapp.data.TodoItem
import com.ilyanvk.todoapp.data.TodoSyncFailed
import com.ilyanvk.todoapp.data.localdatasource.LocalDataSource
import com.ilyanvk.todoapp.data.remotedatasource.RemoteDataSource
import com.ilyanvk.todoapp.data.sharedpreferences.SharedPreferencesDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class TodoItemsRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val sharedPreferencesDataSource: SharedPreferencesDataSource
) : TodoItemsRepository {
    private val _todoItemListFlow: MutableStateFlow<List<TodoItem>> = MutableStateFlow(emptyList())
    override val todoItemListFlow: StateFlow<List<TodoItem>>
        get() = _todoItemListFlow
    override val todoItemList: List<TodoItem>
        get() = _todoItemListFlow.value

    override suspend fun addTodoItem(todoItem: TodoItem) {
        try {
            remoteDataSource.addTodoItem(sharedPreferencesDataSource.revision, todoItem)
        } catch (e: Exception) {
            sharedPreferencesDataSource.needSync = true
        } finally {
            localDataSource.addTodoItem(todoItem)
            syncFlowList()
        }
    }

    override suspend fun updateTodoItem(todoItem: TodoItem) {
        try {
            remoteDataSource.updateTodoItem(todoItem)
        } catch (e: Exception) {
            sharedPreferencesDataSource.needSync = true
        } finally {
            localDataSource.updateTodoItem(todoItem)
            syncFlowList()
        }
    }

    override suspend fun deleteTodoItemById(id: String) {
        try {
            remoteDataSource.deleteTodoItemById(id)
        } catch (e: Exception) {
            sharedPreferencesDataSource.needSync = true
        } finally {
            localDataSource.deleteTodoItemById(id)
            syncFlowList()
        }
    }

    override suspend fun getTodoItemById(id: String): TodoItem {
        return localDataSource.getTodoItemById(id)
    }

    override suspend fun syncFlowList() {
        _todoItemListFlow.emit(localDataSource.getTodoItemList())
    }

    override suspend fun syncDataSources() {
        var localTodoItemList = localDataSource.getTodoItemList()
        try {
            val remoteTodoItemList = remoteDataSource.getTodoItemList()
            if (sharedPreferencesDataSource.needSync) {
                localDataSource.addTodoItemList(remoteTodoItemList)
                remoteDataSource.updateTodoItemList(localTodoItemList)
                sharedPreferencesDataSource.needSync = false
            } else {
                localDataSource.forceUpdateTodoItemList(remoteTodoItemList)
                localTodoItemList = remoteTodoItemList
            }
        } catch (e: Exception) {
            sharedPreferencesDataSource.needSync = true
            throw TodoSyncFailed()
        } finally {
            _todoItemListFlow.emit(localTodoItemList)
        }
    }
}
