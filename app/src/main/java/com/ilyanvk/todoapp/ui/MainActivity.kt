package com.ilyanvk.todoapp.ui

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.ilyanvk.todoapp.Application
import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.data.TodoSyncFailed
import com.ilyanvk.todoapp.data.repository.TodoItemsRepository
import com.ilyanvk.todoapp.data.sharedpreferences.SharedPreferencesDataSource
import com.ilyanvk.todoapp.ui.notifications.NotificationBroadcastReceiver
import com.ilyanvk.todoapp.ui.settings.ThemeMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var repository: TodoItemsRepository

    @Inject
    lateinit var sharedPreferencesDataSource: SharedPreferencesDataSource
    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as Application).appComponent.injectMainActivity(this)

        networkCallback = createNetworkCallback()

        requestNotificationPermission()
        val notificationBroadcastReceiver = NotificationBroadcastReceiver()
        repository.todoItemList.observe(this) {
            notificationBroadcastReceiver.setNotifications(this, it, sharedPreferencesDataSource)
        }

        setTheme()
    }

    override fun onStart() {
        super.onStart()
        registerNetworkCallback()
    }

    override fun onStop() {
        super.onStop()
        unregisterNetworkCallback()
    }

    private fun setTheme() {
        when (sharedPreferencesDataSource.theme) {
            ThemeMode.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            ThemeMode.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            ThemeMode.DEFAULT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }
    }

    private fun createNetworkCallback(): ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {
            private var isSyncInProgress = false
            private var syncJob: Job? = null

            override fun onAvailable(network: Network) {
                if (!isSyncInProgress) {
                    isSyncInProgress = true
                    syncJob?.cancel()
                    syncJob = lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            repository.syncDataSources()
                        } catch (_: TodoSyncFailed) {
                        } finally {
                            delay(SYNC_DELAY)
                            isSyncInProgress = false
                        }
                    }
                }
            }
        }
    }

    private fun registerNetworkCallback() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder().build()
        networkCallback?.let { connectivityManager.registerNetworkCallback(networkRequest, it) }
    }

    private fun unregisterNetworkCallback() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback?.let { connectivityManager.unregisterNetworkCallback(it) }
    }

    companion object Constants {
        const val SYNC_DELAY = 5000L
    }
}
