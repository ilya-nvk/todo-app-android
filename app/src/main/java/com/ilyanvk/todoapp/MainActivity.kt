package com.ilyanvk.todoapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import com.ilyanvk.todoapp.data.AppSettings
import com.ilyanvk.todoapp.data.TodoItemsRepository
import com.ilyanvk.todoapp.data.database.TodoItemDatabase
import com.ilyanvk.todoapp.data.retrofit.ApiClient
import com.ilyanvk.todoapp.data.workmanager.SyncWorker
import com.ilyanvk.todoapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val database = Room.databaseBuilder(
            applicationContext, TodoItemDatabase::class.java, "todo_items.db"
        ).build()

        TodoItemsRepository.apply {
            dao = database.dao
            api = ApiClient().api
            sharedPreferences = AppSettings(applicationContext)
            onRemoteUpdate = { message -> Log.d("MainActivity", "onRemoteUpdate: $message") }
            afterSync = ::afterSync
            createSnackbar = { message -> showErrorSnackbar(view, getString(message)) }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            TodoItemsRepository.getTodoItemsFromServer()
        }

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                TodoItemsRepository.connectionAvailable = true
                if (TodoItemsRepository.isDataLoaded) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        TodoItemsRepository.syncTodoItems()
                    }
                }
            }

            override fun onLost(network: Network) {
                TodoItemsRepository.connectionAvailable = false
            }
        }

        SyncWorker.enqueuePeriodicSync(applicationContext)

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fragmentContainer.id) as NavHostFragment
        navController = navHostFragment.navController
    }

    private suspend fun afterSync(isSuccessful: Boolean) {
        withContext(Dispatchers.Main) {
            if (isSuccessful) {
                Toast.makeText(
                    applicationContext, getString(R.string.sync_successful), Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    applicationContext, getString(R.string.sync_failed), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showErrorSnackbar(view: View, message: String) {
        view.let { rootView ->
            Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        registerNetworkCallback()
    }

    override fun onStop() {
        super.onStop()
        unregisterNetworkCallback()
    }

    private fun registerNetworkCallback() {
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    private fun unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}