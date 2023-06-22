package com.ilyanvk.todoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ilyanvk.todoapp.databinding.FragmentTodoListBinding
import com.ilyanvk.todoapp.recyclerview.TodoItemAdapter
import com.ilyanvk.todoapp.recyclerview.data.TodoItemsRepository

class TodoList : Fragment() {

    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!
    private lateinit var todoItemsRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
        val view = binding.root

        // adapter
        val todoItemsAdapter = TodoItemAdapter(onTaskClick = { todoItem, itemView ->
            TodoItemsRepository.toEdit = todoItem
            Navigation.findNavController(itemView).navigate(R.id.action_todoList_to_todoEditor)
        },
            onCheckboxClick = { todoItem ->
                val newTodoItem =
                    todoItem.copy(isCompleted = !todoItem.isCompleted)
                TodoItemsRepository.updateTodoItem(newTodoItem)
            })

        // repository
        TodoItemsRepository.onRepositoryUpdate = {
            todoItemsAdapter.todoItems = TodoItemsRepository.getTodoItems()
            _binding?.completed?.text =
                getString(R.string.completed, TodoItemsRepository.countCompletedTodoItems())
        }

        // recycler view
        todoItemsRecyclerView = binding.todoItems
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        todoItemsRecyclerView.adapter = todoItemsAdapter
        todoItemsRecyclerView.layoutManager = layoutManager
        todoItemsAdapter.todoItems = TodoItemsRepository.getTodoItems()

        binding.completed.text =
            getString(R.string.completed, TodoItemsRepository.countCompletedTodoItems())

        val completedVisibilityIcon = binding.completedVisibility
        if (!TodoItemsRepository.showCompleted) {
            completedVisibilityIcon.setImageResource(R.drawable.visibility_off)
        }
        completedVisibilityIcon.setOnClickListener {
            if (TodoItemsRepository.showCompleted
            ) {
                TodoItemsRepository.showCompleted = false
                completedVisibilityIcon.setImageResource(R.drawable.visibility_off)
            } else {
                TodoItemsRepository.showCompleted = true
                todoItemsAdapter.todoItems = TodoItemsRepository.getTodoItems()
                completedVisibilityIcon.setImageResource(R.drawable.visibility)
            }
        }

        binding.newTodoFloatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_todoList_to_todoEditor)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}