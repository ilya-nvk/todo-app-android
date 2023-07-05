package com.ilyanvk.todoapp.data.retrofit.models

import com.google.gson.annotations.SerializedName
import com.ilyanvk.todoapp.data.retrofit.TodoItemServer

data class TodoItemApiResponse(
    @SerializedName("status") val status: String,
    @SerializedName("revision") val revision: Int,
    @SerializedName("element") val element: TodoItemServer
)
