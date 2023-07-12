package com.ilyanvk.todoapp.ui.todoeditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ilyanvk.todoapp.Application
import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.data.TodoItem
import com.ilyanvk.todoapp.ui.BundleConstants.TO_EDIT_ID
import com.ilyanvk.todoapp.ui.theme.AppTheme

/**
 * [Fragment] for editing or creating a [TodoItem].
 *
 * The [TodoEditorFragment] allows the user to edit an existing [TodoItem] or create a new one.
 * It provides controls for entering the task text, setting the deadline, and selecting the priority.
 * The user can save the changes or delete the [TodoItem].
 */
class TodoEditorFragment : Fragment() {
    private val component by lazy {
        (requireActivity().application as Application).appComponent.addTodoEditorFragmentComponent()
    }
    private val viewModel: TodoEditorViewModel by activityViewModels {
        TodoEditorViewModel.Factory(component.provideTodoEditorViewModelFactory())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel.addTodoItemToEdit(
            arguments?.getString(TO_EDIT_ID)
        )

        val view = ComposeView(requireContext())
        view.apply {
            setContent {
                AppTheme {
                    TodoEditorScreen(
                        todoItem = viewModel.toEdit,
                        isEditing = viewModel.isEditing,
                        onAction = ::onTodoEditorAction
                    )
                }
            }
        }

        return view
    }

    private fun onTodoEditorAction(action: TodoEditorAction) {
        when (action) {
            TodoEditorAction.Close -> {
                findNavController().navigate(R.id.action_todoEditor_to_todoList)
            }

            TodoEditorAction.Delete -> {
                viewModel.deleteTodoItem()
                findNavController().navigate(R.id.action_todoEditor_to_todoList)
            }

            TodoEditorAction.Save -> {
                viewModel.saveTodoItem()
                findNavController().navigate(R.id.action_todoEditor_to_todoList)
            }

            is TodoEditorAction.UpdateDeadline -> {
                viewModel.updateDeadline(action.deadline)
            }

            is TodoEditorAction.UpdatePriority -> {
                viewModel.updatePriority(action.priority)
            }

            is TodoEditorAction.UpdateText -> {
                viewModel.updateText(action.text)
            }
        }
    }
}
