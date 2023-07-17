package com.ilyanvk.todoapp.ui.settings

sealed class SettingsAction {
    data class UpdateTheme(val theme: ThemeMode) : SettingsAction()
    object Close : SettingsAction()
}
