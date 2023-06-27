package com.ilyanvk.todoapp.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ilyanvk.todoapp.databinding.TodoItemBinding
import com.ilyanvk.todoapp.data.TodoItem
import com.ilyanvk.todoapp.recyclerview.domain.CommonCallbackImpl

class TodoItemAdapter(
    private val onTaskClick: (todoItem: TodoItem, itemView: View) -> Unit,
    private val onCheckboxClick: (todoItem: TodoItem) -> Unit
) :
    RecyclerView.Adapter<TodoItemViewHolder>() {
    var todoItems = listOf<TodoItem>()
        set(value) {
            val callback = CommonCallbackImpl(oldItems = field,
                newItems = value,
                areItemsTheSameImpl = { oldItem, newItem -> oldItem.id == newItem.id })
            field = value
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val binding = TodoItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoItemViewHolder(binding)
    }

    override fun getItemCount() = todoItems.size

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        holder.onBind(todoItems[position], onTaskClick, onCheckboxClick)
    }
}