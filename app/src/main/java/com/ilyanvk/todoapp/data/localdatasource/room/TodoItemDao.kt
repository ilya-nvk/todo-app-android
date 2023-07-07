package com.ilyanvk.todoapp.data.localdatasource.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoItemDao {

    @Query("SELECT * FROM TodoItemEntity ORDER BY deadline")
    fun getAll(): List<TodoItemEntity>

    @Query("SELECT * FROM TodoItemEntity WHERE id = :id")
    fun getById(id: String): TodoItemEntity

    @Insert
    suspend fun add(todoItem: TodoItemEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAll(list: List<TodoItemEntity>)

    @Update
    suspend fun update(todoItem: TodoItemEntity)

    @Query("DELETE FROM TodoItemEntity WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM TodoItemEntity")
    suspend fun clear()
}
