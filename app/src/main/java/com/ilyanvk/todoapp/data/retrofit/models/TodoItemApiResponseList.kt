package com.ilyanvk.todoapp.data.retrofit.models

import com.google.gson.annotations.SerializedName
import com.ilyanvk.todoapp.data.retrofit.TodoItemServer

data class TodoItemApiResponseList(
    @SerializedName("status") val status: String,
    @SerializedName("list") val list: List<TodoItemServer>,
    @SerializedName("revision") val revision: Int
)
