package com.ilyanvk.todoapp.data

import android.content.Context
import java.util.UUID

class AppSettings(context: Context) {
    private val preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    private val editor = preferences.edit()

    init {
        generateDeviceId()
    }

    var revision: Int
        set(value) {
            editor.putInt(REVISION_TAG, value)
            editor.apply()
        }
        get() = preferences.getInt(REVISION_TAG, 0)

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


    var deviceId: String? = preferences.getString(DEVICE_TAG, null)


    companion object {
        private const val REVISION_TAG = "currentRevision"
        private const val DEVICE_TAG = "currentDevice"
    }
}