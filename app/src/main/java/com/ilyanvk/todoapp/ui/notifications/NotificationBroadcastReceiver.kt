package com.ilyanvk.todoapp.ui.notifications

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.data.TodoItem
import com.ilyanvk.todoapp.data.sharedpreferences.SharedPreferencesDataSource
import com.ilyanvk.todoapp.di.scopes.AppScope
import com.ilyanvk.todoapp.ui.MainActivity
import com.ilyanvk.todoapp.ui.notifications.TodoNotificationManager.Companion.CHANNEL_ID
import java.util.TimeZone
import javax.inject.Inject

@AppScope
class NotificationBroadcastReceiver @Inject constructor() : BroadcastReceiver() {
    @Inject
    lateinit var context: Context

    @Inject
    lateinit var sharedPreferencesDataSource: SharedPreferencesDataSource

    override fun onReceive(context: Context, intent: Intent) {
        val notificationTitle = intent.getStringExtra("notificationTitle") ?: return
        val notificationId = intent.getIntExtra("notificationId", 0)
        showNotification(
            context,
            notificationTitle,
            notificationId
        )
        Log.d("NotificationBroadcastReceiver", "Notification received")
    }

    fun setNotifications(todoItemList: List<TodoItem>) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent().also { intent ->
                    intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                    context.startActivity(intent)
                }
            }
        }

        cancelAllNotifications(alarmManager)

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

    private fun cancelAllNotifications(alarmManager: AlarmManager) {
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

    private fun showNotification(context: Context, title: String, notificationId: Int) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    context as MainActivity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
            return
        }
        notificationManager.notify(notificationId, builder.build())
    }

}
