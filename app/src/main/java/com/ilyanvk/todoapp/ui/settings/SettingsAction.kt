package com.ilyanvk.todoapp.ui.settings

sealed class SettingsAction {
    data class updateTheme(val theme: ThemeMode) : SettingsAction()
    object Close : SettingsAction()
}
