package com.ilyanvk.todoapp.di.components

import com.ilyanvk.todoapp.di.scopes.SettingsFragmentScope
import com.ilyanvk.todoapp.ui.settings.SettingsViewModel
import dagger.Subcomponent

@Subcomponent
@SettingsFragmentScope
interface SettingsFragmentComponent {
    @SettingsFragmentScope
    fun provideSettingsViewModelFactory(): SettingsViewModel.SettingsViewModelFactory
}
