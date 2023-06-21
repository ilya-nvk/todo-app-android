package com.ilyanvk.todoapp.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.recyclerview.data.TodoItem
import com.ilyanvk.todoapp.recyclerview.data.TodoItemViewHolder
import com.ilyanvk.todoapp.recyclerview.domain.CommonCallbackImpl

class TodoItemAdapter(private val onCheckboxClick: (todoItem: TodoItem) -> Unit) :
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
        val layoutInflater = LayoutInflater.from(parent.context)
        return TodoItemViewHolder(layoutInflater.inflate(R.layout.todo_item, parent, false))
    }

    override fun getItemCount() = todoItems.size

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        holder.onBind(todoItems[position], onCheckboxClick)
    }
}