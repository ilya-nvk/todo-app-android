package com.ilyanvk.todoapp.di.modules

import android.content.Context
import androidx.room.Room
import com.ilyanvk.todoapp.data.localdatasource.room.TodoItemDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(context: Context): TodoItemDatabase {
        return Room.databaseBuilder(
            context, TodoItemDatabase::class.java, "todo_items.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideTodoItemDao(database: TodoItemDatabase) = database.dao
}
