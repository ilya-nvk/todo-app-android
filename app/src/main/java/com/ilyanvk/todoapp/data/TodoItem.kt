package com.ilyanvk.todoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class TodoItem(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val priority: Priority = Priority.MEDIUM,
    val deadline: Long? = null,
    val isCompleted: Boolean = false,
    val creationDate: Long = System.currentTimeMillis(),
    val modificationDate: Long = System.currentTimeMillis()
)
