package com.ilyanvk.todoapp.recyclerview.data

import android.graphics.Paint
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ilyanvk.todoapp.R
import java.text.DateFormat


class TodoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val text: TextView = itemView.findViewById(R.id.text_preview)
    private val priority: ImageView = itemView.findViewById(R.id.priority)
    private val deadline: TextView = itemView.findViewById(R.id.deadline)
    private val checkbox: ImageView = itemView.findViewById(R.id.checkbox)

    fun onBind(
        todoItem: TodoItem,
        onTaskClick: (todoItem: TodoItem, itemView: View) -> Unit,
        onCheckboxClick: (todoItem: TodoItem) -> Unit
    ) {
        createView(todoItem)
        itemView.setOnClickListener {
            onTaskClick(todoItem, itemView)
        }
        itemView.findViewById<ImageView>(R.id.checkbox).setOnClickListener {
            onCheckboxClick(todoItem)
        }
    }

    private fun createView(todoItem: TodoItem) {
        // text
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

        // priority icon
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
                //priority.visibility = View.GONE
                priority.setImageResource(0)
                priority.contentDescription = R.string.no_priority.toString()
            }
        }

        // deadline text
        if (todoItem.deadline != null) {
            deadline.visibility = View.VISIBLE
            deadline.text =
                DateFormat.getDateInstance(DateFormat.DEFAULT).format(todoItem.deadline)
        } else {
            deadline.visibility = View.GONE
        }

        // checkbox
        if (!todoItem.isCompleted && todoItem.priority == Priority.HIGH) {
            checkbox.setImageResource(R.drawable.checkbox_unchecked_high)
        } else if (todoItem.isCompleted) {
            checkbox.setImageResource(R.drawable.checkbox_checked)
        } else {
            checkbox.setImageResource(R.drawable.checkbox_unchecked_normal)
        }
    }
}