package com.ilyanvk.todoapp.ui.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.di.scopes.AppScope
import javax.inject.Inject

@AppScope
class TodoNotificationManagerImpl @Inject constructor() : TodoNotificationManager {
    override fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val name = context.getString(R.string.notification_channel_name)
        val descriptionText = context.getString(R.string.notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(TodoNotificationManager.CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}