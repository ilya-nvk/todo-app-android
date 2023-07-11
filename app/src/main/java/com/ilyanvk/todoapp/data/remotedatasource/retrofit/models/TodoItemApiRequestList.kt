package com.ilyanvk.todoapp.data.remotedatasource.retrofit.models

import com.google.gson.annotations.SerializedName
import com.ilyanvk.todoapp.data.TodoItem

/**
 * Represents a [TodoItem] list API request.
 */
data class TodoItemApiRequestList(
    @SerializedName("status") val status: String,
    @SerializedName("list") val list: List<TodoItemServer>
)
