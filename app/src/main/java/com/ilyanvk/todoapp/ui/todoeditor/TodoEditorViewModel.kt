package com.ilyanvk.todoapp.ui.todoeditor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
) : ViewModel() {

    var toEdit by mutableStateOf(TodoItem())
        private set

    var isEditing = false
        private set

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

    fun addTodoItemToEdit(id: String?) {
        val found = repository.todoItemList.find { it.id == id }
        if (found != null) {
            toEdit = found
            isEditing = true
        } else {
            toEdit = TodoItem()
            isEditing = false
        }
    }

    fun saveTodoItem() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!isEditing) {
                repository.addTodoItem(
                    toEdit.copy(
                        creationDate = System.currentTimeMillis(),
                        modificationDate = System.currentTimeMillis()
                    )
                )
            } else {
                repository.updateTodoItem(
                    toEdit.copy(
                        modificationDate = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    fun deleteTodoItem() {
        viewModelScope.launch(Dispatchers.IO) {
            if (isEditing) {
                repository.deleteTodoItemById(toEdit.id)
            }
        }
    }

    fun updateText(text: String) {
        toEdit = toEdit.copy(text = text)
    }

    fun updatePriority(priority: Priority) {
        toEdit = toEdit.copy(priority = priority)
    }

    fun updateDeadline(deadline: Long?) {
        toEdit = toEdit.copy(deadline = deadline)
    }
}
