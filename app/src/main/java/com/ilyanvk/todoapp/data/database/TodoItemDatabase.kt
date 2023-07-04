package com.ilyanvk.todoapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ilyanvk.todoapp.data.TodoItem

@Database(entities = [TodoItem::class], version = 1)
abstract class TodoItemDatabase : RoomDatabase() {
    abstract val dao: TodoItemDao
}