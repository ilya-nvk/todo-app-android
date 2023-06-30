package com.ilyanvk.todoapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.room.Room
import com.ilyanvk.todoapp.data.AppSettings
import com.ilyanvk.todoapp.data.TodoItemsRepository
import com.ilyanvk.todoapp.data.database.TodoItemDatabase
import com.ilyanvk.todoapp.data.retrofit.ApiClient
import com.ilyanvk.todoapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

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
        }

        lifecycleScope.launch(Dispatchers.IO) { TodoItemsRepository.getTodoItemsFromServer() }

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fragmentContainer.id) as NavHostFragment
        navController = navHostFragment.navController
    }
}