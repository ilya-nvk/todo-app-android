package com.ilyanvk.todoapp.todoeditor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ilyanvk.todoapp.data.Priority
import com.ilyanvk.todoapp.data.TodoItem
import com.ilyanvk.todoapp.data.TodoItemsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

class TodoEditorViewModel : ViewModel() {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    val todoItem = MutableLiveData<TodoItem?>()
    val text = MutableLiveData<String>()
    val priority = MutableLiveData<Priority>()
    val deadline = MutableLiveData<Long?>()

    init {
        todoItem.value = TodoItemsRepository.toEdit
        text.value = todoItem.value?.text ?: ""
        priority.value = todoItem.value?.priority ?: Priority.MEDIUM
        deadline.value = todoItem.value?.deadline
        TodoItemsRepository.toEdit = null
    }

    suspend fun saveTodo(input: String) {
        val newText = input.trim()
        if (newText == "") {
            throw Exception()
        }
        todoItem.value?.let {
            val newTodoItem = it.copy(
                text = newText,
                priority = priority.value ?: Priority.MEDIUM,
                deadline = deadline.value,
                modificationDate = System.currentTimeMillis()
            )
            TodoItemsRepository.updateTodoItem(newTodoItem)
        }
        if (todoItem.value == null) {
            TodoItemsRepository.addTodoItem(
                TodoItem(
                    text = newText,
                    priority = priority.value ?: Priority.MEDIUM,
                    deadline = deadline.value,
                )
            )
        }
    }

    suspend fun deleteTodoItem() {
        todoItem.value?.let { TodoItemsRepository.deleteTodoItem(it) }
    }

    override fun onCleared() {
        coroutineScope.cancel()
    }
}