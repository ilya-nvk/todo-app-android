package com.ilyanvk.todoapp.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ilyanvk.todoapp.Application
import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.data.TodoSyncFailed
import com.ilyanvk.todoapp.data.repository.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var repository: TodoItemsRepository
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as Application).appComponent.injectMainActivity(this)

        networkCallback = createNetworkCallback()
    }


    override fun onStart() {
        super.onStart()
        registerNetworkCallback()
    }

    override fun onStop() {
        super.onStop()
        unregisterNetworkCallback()
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
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    private fun unregisterNetworkCallback() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    companion object Constants {
        const val SYNC_DELAY = 5000L
    }
}
