package com.ilyanvk.todoapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.room.Room
import com.ilyanvk.todoapp.data.TodoItemsRepository
import com.ilyanvk.todoapp.data.database.TodoItemDatabase
import com.ilyanvk.todoapp.data.database.TodoItemViewModel
import com.ilyanvk.todoapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val database = Room.databaseBuilder(
            applicationContext,
            TodoItemDatabase::class.java,
            "todo_items.db"
        ).build()

        @Suppress("UNCHECKED_CAST")
        val todoItemViewModel by viewModels<TodoItemViewModel>(
            factoryProducer = {
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return TodoItemViewModel(database.dao) as T
                    }
                }
            }
        )

        TodoItemsRepository.state = todoItemViewModel.state.value
        TodoItemsRepository.onEvent = todoItemViewModel::onEvent

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fragmentContainer.id) as NavHostFragment
        navController = navHostFragment.navController
    }
}