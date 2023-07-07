package com.ilyanvk.todoapp.data.remotedatasource.retrofit.models

import com.google.gson.annotations.SerializedName

data class TodoItemApiResponseList(
    @SerializedName("status") val status: String,
    @SerializedName("list") val list: List<TodoItemServer>,
    @SerializedName("revision") val revision: Int
)
