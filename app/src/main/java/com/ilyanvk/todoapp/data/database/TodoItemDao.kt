package com.ilyanvk.todoapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ilyanvk.todoapp.data.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemDao {

    @Insert
    suspend fun insert(todoItem: TodoItem)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(list: List<TodoItem>)

    @Update
    suspend fun update(todoItem: TodoItem)

    @Delete
    suspend fun delete(todoItem: TodoItem)

    @Query("DELETE FROM TodoItem")
    suspend fun clear()

    @Query("SELECT * FROM TodoItem ORDER BY deadline")
    fun getAll(): Flow<List<TodoItem>>

    @Query("SELECT * FROM TodoItem ORDER BY deadline")
    fun getAllAsList(): List<TodoItem>

    @Query("SELECT * FROM TodoItem WHERE isCompleted = 0  ORDER BY deadline")
    fun getUncompleted(): Flow<List<TodoItem>>

    @Query("SELECT COUNT(*) FROM TodoItem WHERE isCompleted = 1")
    fun countCompleted(): Int
}