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
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.databinding.FragmentTodoEditorBinding
import com.ilyanvk.todoapp.recyclerview.data.Priority
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

        setupDeadlineControl()
        setupPriorityMenu()
        setupDeleteButton()
        setupSaveTaskButton()
        setupCloseEditorButton()
        binding.editText.setText(viewModel.text.value)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupDeleteButton() {
        if (viewModel.todoItem.value != null) {
            binding.deleteIcon.setImageResource(R.drawable.delete_red)
            binding.deleteText.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.color_light_red
                )
            )
        }
        binding.deleteButton.setOnClickListener {
            viewModel.deleteTodoItem()
            findNavController().navigate(R.id.action_todoEditor_to_todoList)
        }
    }

    private fun setupSaveTaskButton() {
        binding.saveTaskButton.setOnClickListener {
            try {
                viewModel.saveTodo(binding.editText.text.toString())
                findNavController().navigate(R.id.action_todoEditor_to_todoList)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), R.string.empty_task_message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setupDeadlineControl() {
        viewModel.deadline.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.deadlineText.visibility = View.VISIBLE
                binding.deadlineText.text =
                    DateFormat.getDateInstance(DateFormat.DEFAULT).format(it)
            } else {
                binding.deadlineText.visibility = View.GONE
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
                binding.deadlineText.visibility = View.VISIBLE
                binding.deadlineText.text =
                    DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.time)
                viewModel.deadline.value = calendar.time
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setOnCancelListener {
            if (viewModel.deadline.value == null) binding.deadlineSwitch.isChecked = false
        }

        binding.deadlineText.setOnClickListener { datePickerDialog.show() }
        if (viewModel.deadline.value != null) {
            binding.deadlineSwitch.isChecked = true
        }
        binding.deadlineSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) datePickerDialog.show()
            else viewModel.deadline.value = null
        }
    }

    private fun setupPriorityMenu(
    ) {
        val popupMenu = PopupMenu(context, binding.priorityContainer)
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
                    binding.priorityText.text = getString(R.string.no)
                    viewModel.priority.value = Priority.MEDIUM
                    true
                }

                R.id.low_priority_text -> {
                    binding.priorityText.text = getString(R.string.low)
                    viewModel.priority.value = Priority.LOW
                    true
                }

                R.id.high_priority_text -> {
                    binding.priorityText.text = getString(R.string.high)
                    viewModel.priority.value = Priority.HIGH
                    true
                }

                else -> false
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true)
        }
        binding.priorityContainer.setOnClickListener {
            popupMenu.show()
        }
    }

    private fun setupCloseEditorButton() {
        binding.closeEditorButton.setOnClickListener {
            findNavController().navigate(R.id.action_todoEditor_to_todoList)
        }
    }
}