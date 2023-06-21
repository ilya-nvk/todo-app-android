package com.ilyanvk.todoapp.recyclerview.data

import java.util.Date

data class TodoItem(
    val id: String,
    val text: String,
    val priority: Priority,
    val deadline: Date?,
    val isCompleted: Boolean,
    val creationDate: Date,
    val modificationDate: Date?
)
