package com.ilyanvk.todoapp.ui.settings

import androidx.compose.runtime.Immutable

@Immutable
sealed interface SettingsAction {
    data class UpdateTheme(val theme: ThemeMode) : SettingsAction
    object Close : SettingsAction
}
