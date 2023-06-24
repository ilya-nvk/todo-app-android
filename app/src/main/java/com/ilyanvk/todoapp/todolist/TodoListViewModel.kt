package com.ilyanvk.todoapp.todolist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.recyclerview.TodoItemAdapter
import com.ilyanvk.todoapp.recyclerview.data.TodoItemsRepository

class TodoListViewModel : ViewModel() {
    val todoItemsAdapter = setupTodoItemsAdapter()
    val completedNumber = MutableLiveData<Int>()
    val showCompleted = MutableLiveData<Boolean>()

    init {
        setupRepository(todoItemsAdapter)
        setupCompletedVisibilityIcon()
        completedNumber.value = TodoItemsRepository.countCompletedTodoItems()
    }

    fun onVisibilityIconClick() {
        showCompleted.value = !TodoItemsRepository.showCompleted
        TodoItemsRepository.showCompleted = showCompleted.value == true
    }

    private fun setupRepository(todoItemsAdapter: TodoItemAdapter) {
        TodoItemsRepository.onRepositoryUpdate = {
            todoItemsAdapter.todoItems = TodoItemsRepository.getTodoItems()
            completedNumber.value =
                TodoItemsRepository.countCompletedTodoItems()
        }
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
                val newTodoItem =
                    todoItem.copy(isCompleted = !todoItem.isCompleted)
                TodoItemsRepository.updateTodoItem(newTodoItem)
            })
    }
}