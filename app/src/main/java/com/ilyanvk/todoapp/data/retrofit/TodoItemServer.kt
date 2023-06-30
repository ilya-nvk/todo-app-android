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

data class TodoItemApiRequestList(
    @SerializedName("status") val status: String,
    @SerializedName("list") val list: List<TodoItemServer>
)

data class TodoItemApiResponseList(
    @SerializedName("status") val status: String,
    @SerializedName("list") val list: List<TodoItemServer>,
    @SerializedName("revision") val revision: Int
)

data class TodoItemApiRequest(
    @SerializedName("element") val element: TodoItemServer
)

data class TodoItemApiResponse(
    @SerializedName("status") val status: String,
    @SerializedName("revision") val revision: Int,
    @SerializedName("element") val element: TodoItemServer
)
