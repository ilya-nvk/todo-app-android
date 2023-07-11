package com.ilyanvk.todoapp.data.remotedatasource.retrofit.models

import com.google.gson.annotations.SerializedName
import com.ilyanvk.todoapp.data.TodoItem

/**
 * Represents a [TodoItem] API response.
 */
data class TodoItemApiResponse(
    @SerializedName("status") val status: String,
    @SerializedName("revision") val revision: Int,
    @SerializedName("element") val element: TodoItemServer
)
