package com.ilyanvk.todoapp.di.components

import com.ilyanvk.todoapp.di.scopes.TodoEditorFragmentScope
import com.ilyanvk.todoapp.ui.todoeditor.TodoEditorViewModel
import dagger.Subcomponent

@Subcomponent
@TodoEditorFragmentScope
interface TodoEditorFragmentComponent {
    @TodoEditorFragmentScope
    fun provideTodoEditorViewModelFactory(): TodoEditorViewModel.TodoEditorViewModelFactory
}
