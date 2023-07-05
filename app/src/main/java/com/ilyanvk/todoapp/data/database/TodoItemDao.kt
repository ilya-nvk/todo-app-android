package com.ilyanvk.todoapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoItemDao {

    @Insert
    suspend fun insert(todoItem: TodoItemEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(list: List<TodoItemEntity>)

    @Update
    suspend fun update(todoItem: TodoItemEntity)

    @Delete
    suspend fun delete(todoItem: TodoItemEntity)

    @Query("DELETE FROM TodoItemEntity")
    suspend fun clear()

    @Query("SELECT * FROM TodoItemEntity ORDER BY deadline")
    fun getAll(): List<TodoItemEntity>

    @Query("SELECT * FROM TodoItemEntity WHERE isCompleted = 0  ORDER BY deadline")
    fun getUncompleted(): List<TodoItemEntity>

    @Query("SELECT COUNT(*) FROM TodoItemEntity WHERE isCompleted = 1")
    fun countCompleted(): Int
}