package com.ilyanvk.todoapp.data.remotedatasource.retrofit.models

import com.google.gson.annotations.SerializedName
import com.ilyanvk.todoapp.data.TodoItem


/**
 * Represents a [TodoItem] API request.
 */
data class TodoItemApiRequest(
    @SerializedName("element") val element: TodoItemServer
)
