package com.ilyanvk.todoapp.data.sharedpreferences

interface SharedPreferencesDataSource {
    var revision: Int
    var needSync: Boolean
    var showCompletedTodoItems: Boolean
    fun getDeviceId(): String

    companion object {
        const val NAME = "preferences"
    }
}
