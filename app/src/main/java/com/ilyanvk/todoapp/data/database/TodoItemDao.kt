package com.ilyanvk.todoapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ilyanvk.todoapp.data.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemDao {

    @Upsert
    suspend fun upsert(todoItemEntity: TodoItem)

    @Delete
    suspend fun delete(todoItemEntity: TodoItem)

    @Query("SELECT * FROM TodoItem")
    fun getAll(): Flow<List<TodoItem>>

    @Query("SELECT * FROM TodoItem WHERE isCompleted = 0")
    fun getUncompleted(): Flow<List<TodoItem>>
}