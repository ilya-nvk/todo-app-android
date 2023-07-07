package com.ilyanvk.todoapp.data.localdatasource.room

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Room Database class for the [TodoItemEntity].
 */
@Database(entities = [TodoItemEntity::class], version = 1)
abstract class TodoItemDatabase : RoomDatabase() {
    abstract val dao: TodoItemDao
}
