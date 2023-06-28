package com.ilyanvk.todoapp.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.databinding.FragmentTodoListBinding
import com.ilyanvk.todoapp.recyclerview.TodoItemAdapter

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
        setupCompletedNumberText(binding.completed)
        setupVisibilityIcon(binding.completedVisibility)
        setupFloatingActionButton(binding.newTodoFloatingActionButton)
        viewModel.updateData()

        return view
    }

    private fun setupCompletedNumberText(textView: TextView) {
        viewModel.completedNumber.observe(viewLifecycleOwner) {
            textView.text = getString(R.string.completed, it)
        }
    }

    private fun setupVisibilityIcon(visibilityIcon: ImageView) {
        viewModel.showCompleted.observe(viewLifecycleOwner) {
            if (it) {
                visibilityIcon.contentDescription = getString(R.string.hide_completed)
                visibilityIcon.setImageResource(R.drawable.visibility)
            } else {
                visibilityIcon.contentDescription = getString(R.string.show_completed)
                visibilityIcon.setImageResource(R.drawable.visibility_off)
            }
        }
        visibilityIcon.setOnClickListener { viewModel.onVisibilityIconClick() }
    }

    private fun setupFloatingActionButton(floatingActionButton: FloatingActionButton) {
        floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_todoList_to_todoEditor)
        }
    }

    private fun setupRecyclerView(todoItemsAdapter: TodoItemAdapter) {
        todoItemsRecyclerView = binding.todoItems
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        todoItemsRecyclerView.adapter = todoItemsAdapter
        todoItemsRecyclerView.layoutManager = layoutManager
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}