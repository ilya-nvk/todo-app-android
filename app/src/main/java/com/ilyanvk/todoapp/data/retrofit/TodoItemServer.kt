package com.ilyanvk.todoapp.data.retrofit

import com.google.gson.annotations.SerializedName
import com.ilyanvk.todoapp.data.Priority
import com.ilyanvk.todoapp.data.TodoItem

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
            "low" -> Priority.LOW
            "important" -> Priority.HIGH
            else -> Priority.MEDIUM
        },
        deadline = deadline,
        isCompleted = isCompleted,
        creationDate = creationDate,
        modificationDate = modificationDate
    )

    companion object {
        fun fromTodoItem(todoItem: TodoItem, lastUpdatedBy: String) = TodoItemServer(
            id = todoItem.id,
            text = todoItem.text,
            priority = when (todoItem.priority) {
                Priority.LOW -> "low"
                Priority.MEDIUM -> "basic"
                Priority.HIGH -> "important"
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
