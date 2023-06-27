package com.ilyanvk.todoapp.data.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilyanvk.todoapp.data.Priority
import com.ilyanvk.todoapp.data.TodoItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TodoItemViewModel(private val dao: TodoItemDao) : ViewModel() {
    private val _showCompleted = MutableStateFlow(true)

    private val _todoItems = _showCompleted.flatMapLatest {
        when (it) {
            true -> dao.getAll()
            false -> dao.getUncompleted()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(TodoItemState())

    val state = combine(_state, _showCompleted, _todoItems) { state, showCompleted, todoItems ->
        state.copy(
            todoItems = todoItems, showCompleted = showCompleted
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TodoItemState())

    fun onEvent(event: TodoItemEvent) {
        when (event) {
            is TodoItemEvent.DeleteTodoItem -> {
                viewModelScope.launch {
                    dao.delete(event.todoItem)
                }
            }

            TodoItemEvent.HideDialog -> {
                _state.update { it.copy(isAddingTodoItem = false) }
            }

            TodoItemEvent.SaveTodoItem -> {
                val text = _state.value.text
                val priority = _state.value.priority
                val isCompleted = _state.value.isCompleted
                val deadline = _state.value.deadline
                val creationDate = _state.value.creationTime

                if (text.isBlank()) {
                    return
                }

                val todoItem = TodoItem(
                    text = text,
                    priority = priority,
                    isCompleted = isCompleted,
                    deadline = deadline,
                    creationDate = creationDate
                )

                viewModelScope.launch { dao.upsert(todoItem) }

                _state.update { it.copy(isAddingTodoItem = false) }
            }

            is TodoItemEvent.SetInfo -> {
                _state.update {
                    it.copy(
                        text = event.text,
                        isCompleted = event.isCompleted,
                        priority = event.priority ?: Priority.MEDIUM,
                        deadline = event.deadline,
                        creationTime = event.creationDate
                    )
                }
            }

            is TodoItemEvent.ShowCompleted -> {
                _showCompleted.value = event.showCompleted
            }

            TodoItemEvent.ShowDialog -> {
                _state.update { it.copy(isAddingTodoItem = true) }
            }
        }
    }
}