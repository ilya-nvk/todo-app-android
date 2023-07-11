package com.ilyanvk.todoapp.ui.todoeditor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ilyanvk.todoapp.data.Priority
import com.ilyanvk.todoapp.data.TodoItem
import com.ilyanvk.todoapp.data.repository.TodoItemsRepository
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * [ViewModel] class for the [TodoEditorFragment].
 *
 * The [TodoEditorViewModel] class is responsible for managing the state and logic related to
 * editing or creating a [TodoItem].
 *
 * @param repository The [TodoItemsRepository] instance for accessing and modifying [TodoItem] data.
 */
class TodoEditorViewModel @AssistedInject constructor(
    private val repository: TodoItemsRepository
) :
    ViewModel() {

    @AssistedFactory
    interface TodoEditorViewModelFactory {
        fun create(): TodoEditorViewModel
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val factory: TodoEditorViewModelFactory) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return factory.create() as T
        }
    }

    var state: EditorState = EditorState.Creating
        private set

    fun addTodoItemToEdit(id: String?) {
        state =
            if (id != null && repository.todoItemList.find { it.id == id } != null) EditorState.Editing(
                repository.todoItemList.find { it.id == id }!!
            )
            else EditorState.Creating
    }

    fun saveTodo(text: String, deadline: Long?, priority: Priority) {
        viewModelScope.launch(Dispatchers.IO) {
            if (state is EditorState.Creating) {
                val newTodoItem = TodoItem(text = text, priority = priority, deadline = deadline)
                repository.addTodoItem(newTodoItem)
            } else {
                val newTodoItem = (state as EditorState.Editing).todoItem.copy(
                    text = text,
                    priority = priority,
                    deadline = deadline,
                    modificationDate = System.currentTimeMillis()
                )
                repository.updateTodoItem(newTodoItem)
            }
        }
    }

    fun deleteTodoItem() {
        viewModelScope.launch(Dispatchers.IO) {
            if (state is EditorState.Editing) {
                repository.deleteTodoItemById((state as EditorState.Editing).todoItem.id)
            }
        }
    }
}
