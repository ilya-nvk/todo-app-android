package com.ilyanvk.todoapp.ui.todolist.recyclerview

import android.graphics.Paint
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.data.Priority
import com.ilyanvk.todoapp.data.TodoItem
import com.ilyanvk.todoapp.databinding.TodoItemBinding
import com.ilyanvk.todoapp.ui.StringConverter.toDateTimeString

/**
 * ViewHolder class for displaying a single [TodoItem] in a [RecyclerView].
 *
 * The TodoItemViewHolder class extends [RecyclerView.ViewHolder] and holds references to the views
 * for a single TodoItem in the item layout.
 *
 * @param binding The [TodoItemBinding] object that represents the item layout.
 */
class TodoItemViewHolder(private val binding: TodoItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val text: TextView = binding.textPreview
    private val priority: ImageView = binding.priority
    private val deadline: TextView = binding.deadline
    private val checkbox: ImageView = binding.checkbox

    fun onBind(
        todoItem: TodoItem,
        onTaskClick: (todoItem: TodoItem, itemView: View) -> Unit,
        onCheckboxClick: (todoItem: TodoItem) -> Unit
    ) {
        setUpView(todoItem)
        binding.root.setOnClickListener {
            onTaskClick(todoItem, binding.root)
        }
        binding.checkbox.setOnClickListener {
            onCheckboxClick(todoItem)
        }
    }

    private fun setUpView(todoItem: TodoItem) {
        setUpText(todoItem)
        setUpPriorityIcon(todoItem)
        setUpDeadlineText(todoItem)
        setUpCheckbox(todoItem)
    }

    private fun setUpCheckbox(todoItem: TodoItem) {
        if (!todoItem.isCompleted && todoItem.priority == Priority.HIGH) {
            checkbox.setImageResource(R.drawable.checkbox_unchecked_high)
        } else if (todoItem.isCompleted) {
            checkbox.setImageResource(R.drawable.checkbox_checked)
        } else {
            checkbox.setImageResource(R.drawable.checkbox_unchecked_normal)
        }

        if (todoItem.isCompleted) {
            checkbox.contentDescription = R.string.mark_not_completed.toString()
        } else {
            checkbox.contentDescription = R.string.mark_completed.toString()
        }
    }

    private fun setUpDeadlineText(todoItem: TodoItem) {
        if (todoItem.deadline != null) {
            deadline.visibility = View.VISIBLE
            deadline.text = todoItem.deadline.toDateTimeString()
        } else {
            deadline.visibility = View.GONE
        }
    }

    private fun setUpPriorityIcon(todoItem: TodoItem) {
        when (todoItem.priority) {
            Priority.LOW -> {
                priority.visibility = View.VISIBLE
                priority.setImageResource(R.drawable.priority_low)
                priority.contentDescription = R.string.low_priority.toString()
            }

            Priority.HIGH -> {
                priority.visibility = View.VISIBLE
                priority.setImageResource(R.drawable.priority_high)
                priority.contentDescription = R.string.high_priority.toString()
            }

            else -> {
                priority.setImageResource(0)
                priority.contentDescription = R.string.no_priority.toString()
            }
        }
    }

    private fun setUpText(todoItem: TodoItem) {
        text.text = todoItem.text
        val typedValue = TypedValue()
        val theme = text.context.theme
        if (todoItem.isCompleted) {
            theme.resolveAttribute(R.attr.label_tertiary, typedValue, true)
            text.paintFlags = text.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            theme.resolveAttribute(R.attr.label_primary, typedValue, true)
            text.paintFlags = text.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
        text.setTextColor(typedValue.data)
    }
}
