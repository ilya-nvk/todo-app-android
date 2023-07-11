package com.ilyanvk.todoapp.di.components

import com.ilyanvk.todoapp.di.scopes.TodoListFragmentScope
import com.ilyanvk.todoapp.ui.todolist.TodoListViewModel
import dagger.Subcomponent

@Subcomponent
@TodoListFragmentScope
interface TodoListFragmentComponent {
    @TodoListFragmentScope
    fun provideTodoListViewModelFactory(): TodoListViewModel.TodoListViewModelFactory
}
