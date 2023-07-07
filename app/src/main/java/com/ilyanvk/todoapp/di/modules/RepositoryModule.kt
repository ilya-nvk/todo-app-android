package com.ilyanvk.todoapp.di.modules

import com.ilyanvk.todoapp.data.repository.TodoItemsRepository
import com.ilyanvk.todoapp.data.repository.TodoItemsRepositoryImpl
import com.ilyanvk.todoapp.di.scopes.AppScope
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @AppScope
    @Binds
    fun bindTodoItemsRepository(todoItemsRepository: TodoItemsRepositoryImpl): TodoItemsRepository
}
