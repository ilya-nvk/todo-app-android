package com.ilyanvk.todoapp.todoeditor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ilyanvk.todoapp.recyclerview.data.Priority
import com.ilyanvk.todoapp.recyclerview.data.TodoItem
import com.ilyanvk.todoapp.recyclerview.data.TodoItemsRepository
import java.util.Date

class TodoEditorViewModel : ViewModel() {
    val todoItem = MutableLiveData<TodoItem?>()
    val text = MutableLiveData<String>()
    val priority = MutableLiveData<Priority>()
    val deadline = MutableLiveData<Date?>()

    init {
        todoItem.value = TodoItemsRepository.toEdit
        text.value = todoItem.value?.text ?: ""
        priority.value = todoItem.value?.priority ?: Priority.MEDIUM
        deadline.value = todoItem.value?.deadline
        TodoItemsRepository.toEdit = null
    }

    fun saveTodo(input: String) {
        val newText = input.trim()
        if (newText == "") {
            throw Exception()
        }

        todoItem.value?.let {
            val newTodoItem = it.copy(
                text = newText,
                priority = priority.value ?: Priority.MEDIUM,
                deadline = deadline.value,
                modificationDate = Date()
            )
            TodoItemsRepository.addTodoItem(newTodoItem)
        }
        if (todoItem.value == null) {
            TodoItemsRepository.addTodoItem(
                newText,
                priority.value ?: Priority.MEDIUM,
                deadline.value,
                false,
                Date(),
                null
            )
        }
    }

    fun deleteTodoItem() {
        todoItem.value?.let { TodoItemsRepository.deleteTodoItem(it) }
    }
}