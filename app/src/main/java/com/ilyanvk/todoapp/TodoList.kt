package com.ilyanvk.todoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ilyanvk.todoapp.recyclerview.TodoItemAdapter
import com.ilyanvk.todoapp.recyclerview.data.TodoItemsRepository

class TodoList : Fragment() {

    private lateinit var todoItemsRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_todo_list, container, false)
        todoItemsRecyclerView = rootView.findViewById(R.id.todo_items)
        val todoItemsAdapter = TodoItemAdapter { todoItem ->
            todoItem.isCompleted = !todoItem.isCompleted
            TodoItemsRepository.updateTodoItem(todoItem)
            rootView.findViewById<TextView>(R.id.completed).text = String.format(
                getString(R.string.completed),
                TodoItemsRepository.countCompletedTodoItems()
            )
        }
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        todoItemsRecyclerView.adapter = todoItemsAdapter
        todoItemsRecyclerView.layoutManager = layoutManager
        todoItemsAdapter.todoItems = TodoItemsRepository.getTodoItems()

        rootView.findViewById<TextView>(R.id.completed).text = String.format(
            getString(R.string.completed),
            TodoItemsRepository.countCompletedTodoItems()
        )

        val completedVisibilityIcon = rootView.findViewById<ImageView>(R.id.completed_visibility)
        var isVisible = true
        completedVisibilityIcon.setOnClickListener {
            if (isVisible
            ) {
                todoItemsAdapter.todoItems = todoItemsAdapter.todoItems.filter { !it.isCompleted }
                completedVisibilityIcon.setImageResource(R.drawable.visibility_off)
                isVisible = false
            } else {
                todoItemsAdapter.todoItems = TodoItemsRepository.getTodoItems()
                completedVisibilityIcon.setImageResource(R.drawable.visibility)
                isVisible = true
            }
        }

        rootView.findViewById<FloatingActionButton>(R.id.new_todo_floating_action_button)
            .setOnClickListener {
                findNavController().navigate(R.id.action_todoList_to_todoEditor)
            }

        return rootView
    }
}