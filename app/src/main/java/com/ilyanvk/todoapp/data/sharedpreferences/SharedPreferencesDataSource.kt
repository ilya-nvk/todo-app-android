package com.ilyanvk.todoapp.data.sharedpreferences

import com.ilyanvk.todoapp.ui.settings.ThemeMode

/**
 * Interface representing the shared preferences data source for storing and retrieving data.
 *
 * The [SharedPreferencesDataSource] interface defines properties and methods for managing data in shared preferences.
 */
interface SharedPreferencesDataSource {
    var revision: Int
    var needSync: Boolean
    var showCompletedTodoItems: Boolean
    var theme: ThemeMode
    fun getDeviceId(): String

    companion object {
        const val NAME = "preferences"
    }
}
