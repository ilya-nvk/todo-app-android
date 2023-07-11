package com.ilyanvk.todoapp.data.localdatasource.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ilyanvk.todoapp.data.Priority
import com.ilyanvk.todoapp.data.TodoItem
import java.util.UUID

/**
 * Represents a TodoItemEntity, which is an entity class used for storing and
 * retrieving [TodoItem] data from a database.
 */
@Entity
data class TodoItemEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val priority: Priority = Priority.MEDIUM,
    val deadline: Long? = null,
    val isCompleted: Boolean = false,
    val creationDate: Long = System.currentTimeMillis(),
    val modificationDate: Long = System.currentTimeMillis()
) {
    fun toTodoItem() = TodoItem(
        id = id,
        text = text,
        priority = priority,
        deadline = deadline,
        isCompleted = isCompleted,
        creationDate = creationDate,
        modificationDate = modificationDate
    )

    companion object {
        fun fromTodoItem(todoItem: TodoItem) = TodoItemEntity(
            id = todoItem.id,
            text = todoItem.text,
            priority = todoItem.priority,
            deadline = todoItem.deadline,
            isCompleted = todoItem.isCompleted,
            creationDate = todoItem.creationDate,
            modificationDate = todoItem.modificationDate
        )
    }
}
