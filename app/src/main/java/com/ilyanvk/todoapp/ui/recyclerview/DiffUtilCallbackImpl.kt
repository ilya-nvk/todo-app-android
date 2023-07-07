package com.ilyanvk.todoapp.ui.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.ilyanvk.todoapp.data.TodoItem

class DiffUtilCallbackImpl : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
        oldItem == newItem
}
