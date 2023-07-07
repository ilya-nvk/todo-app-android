package com.ilyanvk.todoapp.ui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ilyanvk.todoapp.data.TodoItem
import com.ilyanvk.todoapp.databinding.TodoItemBinding

class TodoItemAdapter(
    private val onTaskClick: (todoItem: TodoItem, itemView: View) -> Unit,
    private val onCheckboxClick: (todoItem: TodoItem) -> Unit
) : ListAdapter<TodoItem, TodoItemViewHolder>(DiffUtilCallbackImpl()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val binding = TodoItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        holder.onBind(getItem(position), onTaskClick, onCheckboxClick)
    }
}
