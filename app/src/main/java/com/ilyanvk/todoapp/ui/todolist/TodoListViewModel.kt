package com.ilyanvk.todoapp.ui.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ilyanvk.todoapp.data.TodoItem
import com.ilyanvk.todoapp.data.TodoSyncFailed
import com.ilyanvk.todoapp.data.repository.TodoItemsRepository
import com.ilyanvk.todoapp.data.sharedpreferences.SharedPreferencesDataSource
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TodoListViewModel @AssistedInject constructor(
    private val repository: TodoItemsRepository,
    private val sharedPreferences: SharedPreferencesDataSource
) :
    ViewModel() {

    @AssistedFactory
    interface TodoListViewModelFactory {
        fun create(): TodoListViewModel
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val factory: TodoListViewModelFactory) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return factory.create() as T
        }
    }

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _todoItemList = MutableLiveData<List<TodoItem>>()
    val todoItemList: LiveData<List<TodoItem>>
        get() = _todoItemList

    val showCompleted
        get() = sharedPreferences.showCompletedTodoItems

    init {
        viewModelScope.launch(Dispatchers.IO) {
            syncData()
        }
    }

    fun onCompletedVisibilityChange() {
        sharedPreferences.showCompletedTodoItems = !sharedPreferences.showCompletedTodoItems
    }

    suspend fun syncData() {
        try {
            repository.syncDataSources()
            _networkState.postValue(NetworkState.Success)
        } catch (_: TodoSyncFailed) {
            _networkState.postValue(NetworkState.Error)
        }
        repository.todoItemListFlow.collectLatest {
            _todoItemList.postValue(it)
        }
    }

    fun changeCompleteStatus(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val newTodoItem = todoItem.copy(
                isCompleted = !todoItem.isCompleted,
                modificationDate = System.currentTimeMillis()
            )
            try {
                repository.updateTodoItem(newTodoItem)
            } catch (_: TodoSyncFailed) {
            }
            repository.todoItemListFlow.collectLatest {
                _todoItemList.postValue(it)
            }
        }
    }
}
