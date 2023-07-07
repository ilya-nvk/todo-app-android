package com.ilyanvk.todoapp.data.remotedatasource.retrofit.models

import com.google.gson.annotations.SerializedName

data class TodoItemApiRequest(
    @SerializedName("element") val element: TodoItemServer
)
