package com.ilyanvk.todoapp.data.remotedatasource.retrofit.models

import com.google.gson.annotations.SerializedName

data class TodoItemApiRequestList(
    @SerializedName("status") val status: String,
    @SerializedName("list") val list: List<TodoItemServer>
)
