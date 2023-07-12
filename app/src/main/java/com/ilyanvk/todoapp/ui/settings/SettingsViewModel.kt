package com.ilyanvk.todoapp.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ilyanvk.todoapp.data.sharedpreferences.SharedPreferencesDataSource
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SettingsViewModel @AssistedInject constructor(
    private val sharedPreferencesDataSource: SharedPreferencesDataSource
) : ViewModel() {

    var currentTheme by mutableStateOf(sharedPreferencesDataSource.theme)
        private set

    @AssistedFactory
    interface SettingsViewModelFactory {
        fun create(): SettingsViewModel
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val factory: SettingsViewModelFactory) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return factory.create() as T
        }
    }

    fun updateTheme(theme: ThemeMode) {
        currentTheme = theme
        sharedPreferencesDataSource.theme = theme
    }
}