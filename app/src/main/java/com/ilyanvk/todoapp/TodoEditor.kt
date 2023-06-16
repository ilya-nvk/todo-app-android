package com.ilyanvk.todoapp

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.switchmaterial.SwitchMaterial
import com.ilyanvk.todoapp.recyclerview.data.Priority
import com.ilyanvk.todoapp.recyclerview.data.TodoItem
import com.ilyanvk.todoapp.recyclerview.data.TodoItemsRepository
import java.text.DateFormat
import java.util.Calendar
import java.util.Date


class TodoEditor : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_todo_editor, container, false)

        val priorityText = rootView.findViewById<TextView>(R.id.priority_text)
        val deadlineText = rootView.findViewById<TextView>(R.id.deadline_text)
        val switch = rootView.findViewById<SwitchMaterial>(R.id.deadline_switch)
        val editText = rootView.findViewById<EditText>(R.id.edit_text)

        val todoItem = TodoItemsRepository.toEdit
        var priority = todoItem?.priority ?: Priority.MEDIUM
        var deadline = todoItem?.deadline
        var text = todoItem?.text
        TodoItemsRepository.toEdit = null

        // text
        editText.setText(text)

        // popup menu
        createPopupMenu(
            priorityText,
            rootView.findViewById(R.id.priority_container),
            priority
        ) { priority = it }

        // delete button
        deleteButtonController(todoItem, rootView)

        // deadline
        deadlineController(deadline, deadlineText, switch) { deadline = it }

        // save
        rootView.findViewById<TextView>(R.id.save_task_button).setOnClickListener {
            text = editText.text.toString().trim()
            if (text == "") {
                Toast.makeText(requireContext(), R.string.empty_task_message, Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (!switch.isChecked) {
                deadline = null
            }
            if (todoItem != null) {
                todoItem.text = text!!
                todoItem.priority = priority
                todoItem.deadline = deadline
                TodoItemsRepository.updateTodoItem(todoItem)
            } else {
                TodoItemsRepository.addTodoItem(text!!, priority, deadline, false, Date(), null)
            }
            findNavController().navigate(R.id.action_todoEditor_to_todoList)
        }

        rootView.findViewById<ImageView>(R.id.close_editor_button).setOnClickListener {
            findNavController().navigate(R.id.action_todoEditor_to_todoList)
        }

        return rootView
    }

    private fun deadlineController(
        deadline: Date?,
        deadlineText: TextView,
        switch: SwitchMaterial,
        setDate: (date: Date) -> Unit
    ) {
        if (deadline != null) {
            deadlineText.visibility = View.VISIBLE
            deadlineText.text = DateFormat.getDateInstance(DateFormat.DEFAULT).format(
                deadline
            )
            switch.isChecked = true
        }
        val datePickerDialog = createDatePickerDialog(deadlineText, switch, setDate)
        switch
            .setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    datePickerDialog.show()
                } else {
                    deadlineText.visibility = View.GONE
                }
            }
        deadlineText.setOnClickListener { datePickerDialog.show() }
    }

    private fun deleteButtonController(todoItem: TodoItem?, rootView: View) {
        if (todoItem != null) {
            rootView.findViewById<ImageView>(R.id.delete_icon)
                .setImageResource(R.drawable.delete_red)
            rootView.findViewById<TextView>(R.id.delete_text).setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_light_red
                )
            )
        }
        rootView.findViewById<ConstraintLayout>(R.id.delete_button).setOnClickListener {
            if (todoItem != null) {
                TodoItemsRepository.deleteTodoItem(todoItem)
            }
            findNavController().navigate(R.id.action_todoEditor_to_todoList)
        }
    }

    private fun createDatePickerDialog(
        deadlineText: TextView,
        switch: SwitchMaterial,
        setDate: (date: Date) -> Unit
    ): DatePickerDialog {
        val currentDate = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.DatePicker,
            { _, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                deadlineText.visibility = View.VISIBLE
                deadlineText.text =
                    DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.time)
                setDate(calendar.time)
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.setOnCancelListener { switch.isChecked = false }

        return datePickerDialog
    }

    private fun createPopupMenu(
        priorityText: TextView,
        priorityContainer: View,
        priority: Priority,
        setPriority: (priority: Priority) -> Unit,
    ) {
        when (priority) {
            Priority.LOW -> priorityText.text = getString(R.string.low)
            Priority.MEDIUM -> priorityText.text = getString(R.string.no)
            Priority.HIGH -> priorityText.text = getString(R.string.high)
        }

        val popupMenu = PopupMenu(context, priorityContainer)
        popupMenu.inflate(R.menu.priority_menu)

        val highPriorityElement: MenuItem = popupMenu.menu.getItem(2)
        val spannableString = SpannableString(getString(R.string.high))
        spannableString.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_light_red
                )
            ),
            0,
            spannableString.length,
            0
        )

        highPriorityElement.title = spannableString
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.no_priority_text -> {
                    priorityText.text = getString(R.string.no)
                    setPriority(Priority.MEDIUM)
                    true
                }

                R.id.low_priority_text -> {
                    priorityText.text = getString(R.string.low)
                    setPriority(Priority.LOW)
                    true
                }

                R.id.high_priority_text -> {
                    priorityText.text = getString(R.string.high)
                    true
                }

                else -> false
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true)
        }
        priorityContainer.setOnClickListener {
            popupMenu.show()
        }
    }

}