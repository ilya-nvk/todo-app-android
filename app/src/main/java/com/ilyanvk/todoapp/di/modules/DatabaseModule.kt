package com.ilyanvk.todoapp.di.modules

import android.content.Context
import androidx.room.Room
import com.ilyanvk.todoapp.data.localdatasource.room.TodoItemDatabase
import com.ilyanvk.todoapp.di.scopes.AppScope
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {
    @AppScope
    @Provides
    fun provideDatabase(context: Context): TodoItemDatabase {
        return Room.databaseBuilder(
            context, TodoItemDatabase::class.java, "todo_items.db"
        ).build()
    }

    @AppScope
    @Provides
    fun provideTodoItemDao(database: TodoItemDatabase) = database.dao
}
