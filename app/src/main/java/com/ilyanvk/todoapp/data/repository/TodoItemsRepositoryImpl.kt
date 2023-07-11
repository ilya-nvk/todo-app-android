package com.ilyanvk.todoapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ilyanvk.todoapp.data.TodoItem
import com.ilyanvk.todoapp.data.TodoSyncFailed
import com.ilyanvk.todoapp.data.localdatasource.LocalDataSource
import com.ilyanvk.todoapp.data.remotedatasource.RemoteDataSource
import com.ilyanvk.todoapp.data.sharedpreferences.SharedPreferencesDataSource
import javax.inject.Inject

/**
 * Implementation of the [TodoItemsRepository] interface that manages [TodoItem] objects.
 *
 * @param localDataSource The local data source for accessing [TodoItem] objects
 * @param remoteDataSource The remote data source for accessing [TodoItem] objects
 * @param sharedPreferencesDataSource The shared preferences data source
 * for storing revision and synchronization information.
 */
class TodoItemsRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val sharedPreferencesDataSource: SharedPreferencesDataSource
) : TodoItemsRepository {

    private val _localTodoItemList: MutableLiveData<List<TodoItem>> =
        MutableLiveData<List<TodoItem>>(
            emptyList()
        )
    override val todoItemList: LiveData<List<TodoItem>>
        get() = _localTodoItemList

    override suspend fun addTodoItem(todoItem: TodoItem) {
        localDataSource.addTodoItem(todoItem)
        _localTodoItemList.postValue(localDataSource.getTodoItemList())
        try {
            remoteDataSource.addTodoItem(sharedPreferencesDataSource.revision, todoItem)
        } catch (e: Exception) {
            sharedPreferencesDataSource.needSync = true
        }
    }

    override suspend fun updateTodoItem(todoItem: TodoItem) {
        localDataSource.updateTodoItem(todoItem)
        _localTodoItemList.postValue(localDataSource.getTodoItemList())
        try {
            remoteDataSource.updateTodoItem(todoItem)
        } catch (e: Exception) {
            sharedPreferencesDataSource.needSync = true
        }
    }

    override suspend fun deleteTodoItemById(id: String) {
        localDataSource.deleteTodoItemById(id)
        _localTodoItemList.postValue(localDataSource.getTodoItemList())
        try {
            remoteDataSource.deleteTodoItemById(id)
        } catch (e: Exception) {
            sharedPreferencesDataSource.needSync = true
        }
    }

    override suspend fun getTodoItemById(id: String): TodoItem {
        return localDataSource.getTodoItemById(id)
    }

    override suspend fun syncList() {
        _localTodoItemList.postValue(localDataSource.getTodoItemList())
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
            _localTodoItemList.postValue(localTodoItemList)
        }
    }
}
