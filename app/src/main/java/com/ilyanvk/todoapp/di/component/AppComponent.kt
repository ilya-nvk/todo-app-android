package com.ilyanvk.todoapp.di.component

import com.ilyanvk.todoapp.Application
import com.ilyanvk.todoapp.di.modules.ApiModule
import com.ilyanvk.todoapp.di.modules.AppModule
import com.ilyanvk.todoapp.di.modules.ClientModule
import com.ilyanvk.todoapp.di.modules.DataSourceModule
import com.ilyanvk.todoapp.di.modules.DatabaseModule
import com.ilyanvk.todoapp.di.modules.RepositoryModule
import com.ilyanvk.todoapp.ui.MainActivity
import com.ilyanvk.todoapp.ui.todoeditor.TodoEditorViewModel
import com.ilyanvk.todoapp.ui.todolist.TodoListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ApiModule::class, AppModule::class, ClientModule::class,
        DataSourceModule::class, DatabaseModule::class, RepositoryModule::class]
)
interface AppComponent {
    fun injectApplication(application: Application)
    fun injectMainActivity(activity: MainActivity)
    fun injectTodoListViewModel(): TodoListViewModel.TodoListViewModelFactory
    fun injectTodoEditorViewModel(): TodoEditorViewModel.TodoEditorViewModelFactory
}
