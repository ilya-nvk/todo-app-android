package com.ilyanvk.todoapp.ui.todoeditor

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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ilyanvk.todoapp.Application
import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.data.Priority
import com.ilyanvk.todoapp.data.TodoItem
import com.ilyanvk.todoapp.databinding.FragmentTodoEditorBinding
import com.ilyanvk.todoapp.ui.BundleConstants.TO_EDIT_ID
import java.text.DateFormat
import java.util.Calendar

/**
 * [Fragment] for editing or creating a [TodoItem].
 *
 * The [TodoEditorFragment] allows the user to edit an existing [TodoItem] or create a new one.
 * It provides controls for entering the task text, setting the deadline, and selecting the priority.
 * The user can save the changes or delete the [TodoItem].
 */
class TodoEditorFragment : Fragment() {
    private var _binding: FragmentTodoEditorBinding? = null
    private val binding get() = _binding!!

    private val component by lazy {
        (requireActivity().application as Application).appComponent.addTodoEditorFragmentComponent()
    }
    private val viewModel: TodoEditorViewModel by activityViewModels {
        TodoEditorViewModel.Factory(component.provideTodoEditorViewModelFactory())
    }

    private var text: String = ""
    private var deadline: Long? = null
    private var priority: Priority = Priority.MEDIUM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoEditorBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.addTodoItemToEdit(
            arguments?.getString(TO_EDIT_ID)
        )
        if (viewModel.state is EditorState.Editing) {
            val toEdit = (viewModel.state as EditorState.Editing).todoItem
            text = toEdit.text
            deadline = toEdit.deadline
            priority = toEdit.priority
        }

        setUpDeadlineControl()
        setUpPriorityMenu()
        setUpDeleteButton()
        setUpSaveButton()
        binding.closeEditorButton.setOnClickListener {
            findNavController().navigate(R.id.action_todoEditor_to_todoList)
        }

        binding.editText.setText(text)
        return view
    }

    private fun setUpDeleteButton() {
        if (viewModel.state is EditorState.Editing) {
            binding.deleteIcon.setImageResource(R.drawable.delete_red)
            binding.deleteText.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.color_light_red
                )
            )
        }
        binding.deleteButton.setOnClickListener {
            findNavController().navigate(R.id.action_todoEditor_to_todoList)
            viewModel.deleteTodoItem()
        }
    }

    private fun setUpSaveButton() {
        binding.saveTaskButton.setOnClickListener {
            val newText = binding.editText.text.toString().trim()
            if (newText.isBlank()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.empty_task_message),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            findNavController().navigate(R.id.action_todoEditor_to_todoList)
            viewModel.saveTodo(newText, deadline, priority)
        }
    }


    private fun setUpDeadlineControl() {
        val currentDate = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.DatePicker,
            { _, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                deadline = calendar.timeInMillis
                updateDeadline()
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setOnCancelListener {
            if (deadline == null) binding.deadlineSwitch.isChecked = false
        }

        binding.deadlineText.setOnClickListener { datePickerDialog.show() }
        if (deadline != null) {
            binding.deadlineSwitch.isChecked = true
        }
        binding.deadlineSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                datePickerDialog.show()
            } else {
                deadline = null
                updateDeadline()
            }
        }
        updateDeadline()
    }

    private fun updateDeadline() {
        if (deadline != null) {
            binding.deadlineText.visibility = View.VISIBLE
            binding.deadlineText.text =
                DateFormat.getDateInstance(DateFormat.DEFAULT).format(deadline)
        } else {
            binding.deadlineText.visibility = View.GONE
        }
    }

    private fun setUpPriorityMenu() {
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
            onMenuItemClick(it.itemId)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true)
        }
        binding.priorityContainer.setOnClickListener {
            popupMenu.show()
        }
    }

    private fun onMenuItemClick(id: Int): Boolean {
        return when (id) {
            R.id.no_priority_text -> {
                priority = Priority.MEDIUM
                updatePriority()
                true
            }

            R.id.low_priority_text -> {
                priority = Priority.LOW
                updatePriority()
                true
            }

            R.id.high_priority_text -> {
                priority = Priority.HIGH
                updatePriority()
                true
            }

            else -> false
        }
    }

    private fun updatePriority() {
        binding.priorityText.text = when (priority) {
            Priority.LOW -> getString(R.string.low)
            Priority.HIGH -> getString(R.string.high)
            else -> getString(R.string.no)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
