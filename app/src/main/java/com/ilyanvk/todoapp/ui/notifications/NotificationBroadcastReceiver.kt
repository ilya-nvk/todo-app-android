package com.ilyanvk.todoapp.ui.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.ilyanvk.todoapp.data.TodoItem
import com.ilyanvk.todoapp.data.sharedpreferences.SharedPreferencesDataSource
import java.util.TimeZone

class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationTitle = intent.getStringExtra("notificationTitle") ?: return
        val notificationId = intent.getIntExtra("notificationId", 0)
        TodoNotificationManager.showNotification(
            context,
            notificationTitle,
            notificationId
        )
        Log.d("NotificationBroadcastReceiver", "Notification received")
    }

    fun setNotifications(
        context: Context,
        todoItemList: List<TodoItem>,
        sharedPreferencesDataSource: SharedPreferencesDataSource
    ) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent().also { intent ->
                    intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                    context.startActivity(intent)
                }
            }
        }

        cancelAllNotifications(
            context,
            alarmManager,
            sharedPreferencesDataSource
        )

        var counter = 0
        todoItemList.forEach {
            if (it.isCompleted ||
                it.deadline == null ||
                it.deadline - TimeZone.getDefault().rawOffset <= System.currentTimeMillis()
            ) {
                return@forEach
            }

            val notificationIntent =
                Intent(context, NotificationBroadcastReceiver::class.java).apply {
                    putExtra("notificationTitle", it.text)
                    putExtra("notificationId", counter++)
                }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                it.id.hashCode(),
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val alarmAt =
                it.deadline - TimeZone.getDefault().rawOffset
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmAt,
                pendingIntent
            )

            Log.d("NotificationBroadcastReceiver", "Notification set for ${it.text}")
        }

        sharedPreferencesDataSource.notificationIds = todoItemList.map { it.id }.toSet()
    }

    private fun cancelAllNotifications(
        context: Context,
        alarmManager: AlarmManager,
        sharedPreferencesDataSource: SharedPreferencesDataSource
    ) {
        sharedPreferencesDataSource.notificationIds.forEach {
            val notificationIntent = Intent(context, NotificationBroadcastReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                it.hashCode(),
                notificationIntent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
            }
        }
    }
}
