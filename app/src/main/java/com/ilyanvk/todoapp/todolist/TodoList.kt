package com.ilyanvk.todoapp.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.databinding.FragmentTodoListBinding
import com.ilyanvk.todoapp.recyclerview.TodoItemAdapter
import com.ilyanvk.todoapp.recyclerview.data.TodoItemsRepository

class TodoList : Fragment() {
    private val viewModel: TodoListViewModel by viewModels()

    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!
    private lateinit var todoItemsRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
        val view = binding.root

        setupRecyclerView(viewModel.todoItemsAdapter)

        viewModel.completedNumber.observe(viewLifecycleOwner) {
            binding.completed.text = getString(R.string.completed, it)
        }

        val completedVisibilityIcon = binding.completedVisibility
        viewModel.showCompleted.observe(viewLifecycleOwner) {
            completedVisibilityIcon.setImageResource(
                if (it) R.drawable.visibility
                else R.drawable.visibility_off
            )
        }
        completedVisibilityIcon.setOnClickListener { viewModel.onVisibilityIconClick() }

        binding.newTodoFloatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_todoList_to_todoEditor)
        }

        return view
    }

    private fun setupRecyclerView(todoItemsAdapter: TodoItemAdapter) {
        todoItemsRecyclerView = binding.todoItems
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        todoItemsRecyclerView.adapter = todoItemsAdapter
        todoItemsRecyclerView.layoutManager = layoutManager
        todoItemsAdapter.todoItems = TodoItemsRepository.getTodoItems()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}