package com.ilyanvk.todoapp.recyclerview.data

import java.util.Date

data class TodoItem(
    val id: String,
    var text: String,
    var priority: Priority,
    var deadline: Date?,
    var isCompleted: Boolean,
    val creationDate: Date,
    var modificationDate: Date?
)
