package com.ilyanvk.todoapp.di.modules

import com.ilyanvk.todoapp.di.scopes.AppScope
import com.ilyanvk.todoapp.ui.notifications.TodoNotificationManager
import com.ilyanvk.todoapp.ui.notifications.TodoNotificationManagerImpl
import dagger.Binds
import dagger.Module

@Module
interface NotificationsModule {
    @AppScope
    @Binds
    fun bindTodoNotificationManager(
        todoNotificationManagerImpl: TodoNotificationManagerImpl
    ): TodoNotificationManager
}