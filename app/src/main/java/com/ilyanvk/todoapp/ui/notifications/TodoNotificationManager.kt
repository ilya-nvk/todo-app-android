package com.ilyanvk.todoapp.ui.notifications

import android.content.Context

interface TodoNotificationManager {
    fun createNotificationChannel(context: Context)

    companion object {
        const val CHANNEL_ID = "TodoAppChannel"
    }
}
