package com.ilyanvk.todoapp.di.components

import com.ilyanvk.todoapp.Application
import com.ilyanvk.todoapp.di.modules.ApiModule
import com.ilyanvk.todoapp.di.modules.AppModule
import com.ilyanvk.todoapp.di.modules.ClientModule
import com.ilyanvk.todoapp.di.modules.DataSourceModule
import com.ilyanvk.todoapp.di.modules.DatabaseModule
import com.ilyanvk.todoapp.di.modules.RepositoryModule
import com.ilyanvk.todoapp.di.scopes.AppScope
import com.ilyanvk.todoapp.ui.MainActivity
import dagger.Component

@AppScope
@Component(
    modules = [ApiModule::class, AppModule::class, ClientModule::class,
        DataSourceModule::class, DatabaseModule::class, RepositoryModule::class]
)
interface AppComponent {
    fun injectApplication(application: Application)
    fun injectMainActivity(activity: MainActivity)
    fun addTodoListFragmentComponent(): TodoListFragmentComponent
    fun addTodoEditorFragmentComponent(): TodoEditorFragmentComponent
    fun addSettingsFragmentComponent(): SettingsFragmentComponent
}
