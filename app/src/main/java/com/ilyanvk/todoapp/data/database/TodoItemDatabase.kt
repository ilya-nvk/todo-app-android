package com.ilyanvk.todoapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TodoItemEntity::class], version = 1)
abstract class TodoItemDatabase : RoomDatabase() {
    abstract val dao: TodoItemDao
}