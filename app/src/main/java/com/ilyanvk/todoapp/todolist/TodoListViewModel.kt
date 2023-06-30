package com.ilyanvk.todoapp.todolist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.data.TodoItemsRepository
import com.ilyanvk.todoapp.data.retrofit.TodoItemApi
import com.ilyanvk.todoapp.recyclerview.TodoItemAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoListViewModel : ViewModel() {
    val todoItemsAdapter = setupTodoItemsAdapter()
    val completedNumber = MutableLiveData<Int>()
    val showCompleted = MutableLiveData<Boolean>()

    init {
        setupRepository()
        setupCompletedVisibilityIcon()
    }

    fun onVisibilityIconClick() {
        TodoItemsRepository.showCompleted = !TodoItemsRepository.showCompleted
        showCompleted.value = TodoItemsRepository.showCompleted
    }

    fun onSwipeRefresh() {
        viewModelScope.launch(Dispatchers.IO) {
            TodoItemsRepository.syncTodoItems()
        }
    }

    private fun setupRepository() {
        TodoItemsRepository.onRepositoryUpdate = ::updateData
    }

    private fun setupCompletedVisibilityIcon() {
        showCompleted.value = TodoItemsRepository.showCompleted
    }

    private fun setupTodoItemsAdapter(): TodoItemAdapter {
        return TodoItemAdapter(onTaskClick = { todoItem, itemView ->
            TodoItemsRepository.toEdit = todoItem
            Navigation.findNavController(itemView).navigate(R.id.action_todoList_to_todoEditor)
        },
            onCheckboxClick = { todoItem ->
                viewModelScope.launch(Dispatchers.IO) {
                    TodoItemsRepository.changeCompletionOfTodoItem(todoItem)
                }
            })
    }

    fun updateData() {
        viewModelScope.launch(Dispatchers.IO) {
            TodoItemsRepository.getAllTodoItems().collect {
                when (TodoItemsRepository.showCompleted) {
                    false -> todoItemsAdapter.submitList(it.filter { todoItem -> !todoItem.isCompleted })
                    else -> todoItemsAdapter.submitList(it)
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            completedNumber.postValue(TodoItemsRepository.countCompletedTodoItems())
        }
    }
}