package com.ilyanvk.todoapp.data.remotedatasource.retrofit.models

import com.google.gson.annotations.SerializedName
import com.ilyanvk.todoapp.data.Priority
import com.ilyanvk.todoapp.data.TodoItem

/**
 * This class is used to map the [TodoItem] object to the server format.
 * It is also used to map the server response to the [TodoItem] object.
 */
data class TodoItemServer(
    @SerializedName("id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("importance") val priority: String,
    @SerializedName("deadline") val deadline: Long?,
    @SerializedName("done") val isCompleted: Boolean,
    @SerializedName("color") val color: String?,
    @SerializedName("created_at") val creationDate: Long,
    @SerializedName("changed_at") val modificationDate: Long,
    @SerializedName("last_updated_by") val lastUpdatedBy: String
) {
    fun toTodoItem() = TodoItem(
        id = id,
        text = text,
        priority = when (priority) {
            PRIORITY_LOW -> Priority.LOW
            PRIORITY_HIGH -> Priority.HIGH
            else -> Priority.MEDIUM
        },
        deadline = deadline,
        isCompleted = isCompleted,
        creationDate = creationDate,
        modificationDate = modificationDate
    )

    companion object {
        private const val PRIORITY_LOW = "low"
        private const val PRIORITY_MEDIUM = "basic"
        private const val PRIORITY_HIGH = "important"

        fun fromTodoItem(todoItem: TodoItem, lastUpdatedBy: String) = TodoItemServer(
            id = todoItem.id,
            text = todoItem.text,
            priority = when (todoItem.priority) {
                Priority.LOW -> PRIORITY_LOW
                Priority.MEDIUM -> PRIORITY_MEDIUM
                Priority.HIGH -> PRIORITY_HIGH
            },
            deadline = todoItem.deadline,
            isCompleted = todoItem.isCompleted,
            color = null,
            creationDate = todoItem.creationDate,
            modificationDate = todoItem.modificationDate,
            lastUpdatedBy = lastUpdatedBy
        )
    }
}
