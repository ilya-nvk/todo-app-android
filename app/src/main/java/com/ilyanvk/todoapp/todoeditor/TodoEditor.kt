package com.ilyanvk.todoapp.todoeditor

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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.switchmaterial.SwitchMaterial
import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.data.Priority
import com.ilyanvk.todoapp.databinding.FragmentTodoEditorBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.util.Calendar


class TodoEditor : Fragment() {
    private val viewModel: TodoEditorViewModel by viewModels()

    private var _binding: FragmentTodoEditorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoEditorBinding.inflate(inflater, container, false)
        val view = binding.root

        setupDeadlineControl(binding.deadlineText, binding.deadlineSwitch)
        setupPriorityMenu(binding.priorityContainer, binding.priorityText)
        setupDeleteButton(binding.deleteIcon, binding.deleteText, binding.deleteButton)
        setupSaveTaskButton(binding.saveTaskButton, binding.editText)
        setupCloseEditorButton(binding.closeEditorButton)
        binding.editText.setText(viewModel.text.value)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupDeleteButton(deleteIcon: ImageView, deleteText: TextView, deleteButton: View) {
        if (viewModel.todoItem.value != null) {
            deleteIcon.setImageResource(R.drawable.delete_red)
            deleteText.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.color_light_red
                )
            )
        }
        deleteButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.deleteTodoItem()
                withContext(Main) {
                    findNavController().navigate(R.id.action_todoEditor_to_todoList)
                }
            }

        }
    }

    private fun setupSaveTaskButton(
        saveTaskButton: View, editText: EditText
    ) {
        saveTaskButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                var isOperationSuccessful = false
                try {
                    viewModel.saveTodo(editText.text.toString())
                    isOperationSuccessful = true
                } catch (e: Exception) {
                    withContext(Main) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.empty_task_message),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                if (isOperationSuccessful) {
                    withContext(Main) {
                        findNavController().navigate(R.id.action_todoEditor_to_todoList)
                    }
                }
            }
        }
    }


    private fun setupDeadlineControl(deadlineText: TextView, switch: SwitchMaterial) {
        viewModel.deadline.observe(viewLifecycleOwner) {
            if (it != null) {
                deadlineText.visibility = View.VISIBLE
                deadlineText.text = DateFormat.getDateInstance(DateFormat.DEFAULT).format(it)
            } else {
                deadlineText.visibility = View.GONE
            }
        }

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
                    DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.timeInMillis)
                viewModel.deadline.value = calendar.timeInMillis
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setOnCancelListener {
            if (viewModel.deadline.value == null) switch.isChecked = false
        }

        deadlineText.setOnClickListener { datePickerDialog.show() }
        if (viewModel.deadline.value != null) {
            switch.isChecked = true
        }
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) datePickerDialog.show()
            else viewModel.deadline.value = null
        }
    }

    private fun setupPriorityMenu(priorityContainer: View, priorityText: TextView) {
        val popupMenu = PopupMenu(context, priorityContainer)
        popupMenu.inflate(R.menu.priority_menu)

        val highPriorityElement: MenuItem = popupMenu.menu.getItem(2)
        val spannableString = SpannableString(getString(R.string.high))
        spannableString.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    requireContext(), R.color.color_light_red
                )
            ), 0, spannableString.length, 0
        )

        highPriorityElement.title = spannableString
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.no_priority_text -> {
                    viewModel.priority.value = Priority.MEDIUM
                    true
                }

                R.id.low_priority_text -> {
                    viewModel.priority.value = Priority.LOW
                    true
                }

                R.id.high_priority_text -> {
                    viewModel.priority.value = Priority.HIGH
                    true
                }

                else -> false
            }
        }

        viewModel.priority.observe(viewLifecycleOwner) {
            priorityText.text = when (it) {
                Priority.LOW -> getString(R.string.low)
                Priority.HIGH -> getString(R.string.high)
                else -> getString(R.string.no)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true)
        }
        priorityContainer.setOnClickListener {
            popupMenu.show()
        }
    }

    private fun setupCloseEditorButton(closeEditorButton: View) {
        closeEditorButton.setOnClickListener {
            findNavController().navigate(R.id.action_todoEditor_to_todoList)
        }
    }
}