package com.ilyanvk.todoapp.data.retrofit.models

import com.google.gson.annotations.SerializedName
import com.ilyanvk.todoapp.data.retrofit.TodoItemServer

data class TodoItemApiRequest(
    @SerializedName("element") val element: TodoItemServer
)
