package com.ilyanvk.todoapp.data

import java.util.UUID

data class TodoItem(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val priority: Priority = Priority.MEDIUM,
    val deadline: Long? = null,
    val isCompleted: Boolean = false,
    val creationDate: Long = System.currentTimeMillis(),
    val modificationDate: Long = System.currentTimeMillis()
)
