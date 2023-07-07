package com.ilyanvk.todoapp.data.sharedpreferences

import android.content.Context
import com.ilyanvk.todoapp.data.sharedpreferences.SharedPreferencesDataSource.Companion.NAME
import java.util.UUID
import javax.inject.Inject

class SharedPreferencesDataSourceImpl @Inject constructor(context: Context) :
    SharedPreferencesDataSource {
    private val preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    private val editor = preferences.edit()

    init {
        generateDeviceId()
    }

    override var revision: Int
        set(value) {
            editor.putInt(REVISION_TAG, value)
            editor.apply()
        }
        get() = preferences.getInt(REVISION_TAG, 0)

    override var needSync: Boolean
        get() = preferences.getBoolean(SYNC_FLAG_TAG, true)
        set(value) {
            editor.putBoolean(SYNC_FLAG_TAG, value)
        }

    override var showCompletedTodoItems: Boolean
        get() = preferences.getBoolean(SHOW_COMPLETED_TAG, true)
        set(value) {
            editor.putBoolean(SHOW_COMPLETED_TAG, value)
            editor.apply()
        }

    private fun generateDeviceId() {
        if (deviceId == null) {
            val storedDeviceId = preferences.getString(DEVICE_TAG, null)
            if (storedDeviceId != null) {
                deviceId = storedDeviceId
            } else {
                deviceId = UUID.randomUUID().toString()
                editor.putString(DEVICE_TAG, deviceId)
                editor.apply()
            }
        }
    }

    private var deviceId = preferences.getString(DEVICE_TAG, null)


    override fun getDeviceId(): String {
        if (deviceId == null) {
            generateDeviceId()
        }
        return deviceId!!
    }


    companion object {
        private const val REVISION_TAG = "currentRevision"
        private const val DEVICE_TAG = "currentDevice"
        private const val SYNC_FLAG_TAG = "syncFlag"
        private const val SHOW_COMPLETED_TAG = "showCompleted"
    }
}