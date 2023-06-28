package com.ilyanvk.todoapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ilyanvk.todoapp.data.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemDao {

    @Insert
    suspend fun insert(todoItem: TodoItem)

    @Update
    suspend fun update(todoItem: TodoItem)

    @Delete
    suspend fun delete(todoItem: TodoItem)

    @Query("SELECT * FROM TodoItem")
    fun getAll(): Flow<List<TodoItem>>

    @Query("SELECT * FROM TodoItem WHERE isCompleted = 0")
    fun getUncompleted(): Flow<List<TodoItem>>

    @Query("SELECT COUNT(*) FROM TodoItem WHERE isCompleted = 1")
    fun countCompleted(): Int
}