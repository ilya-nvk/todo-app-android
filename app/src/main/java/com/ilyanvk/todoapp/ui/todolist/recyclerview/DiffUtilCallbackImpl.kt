package com.ilyanvk.todoapp.ui.todolist.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.ilyanvk.todoapp.data.TodoItem

/**
 * Implementation of the [DiffUtil.ItemCallback] for comparing TodoItem objects in a RecyclerView.
 */
class DiffUtilCallbackImpl : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
        oldItem == newItem
}
