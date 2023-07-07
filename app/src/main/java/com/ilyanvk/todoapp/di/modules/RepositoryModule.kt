package com.ilyanvk.todoapp.di.modules

import com.ilyanvk.todoapp.data.repository.TodoItemsRepository
import com.ilyanvk.todoapp.data.repository.TodoItemsRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {
    @Singleton
    @Binds
    fun bindTodoItemsRepository(todoItemsRepository: TodoItemsRepositoryImpl): TodoItemsRepository
}
