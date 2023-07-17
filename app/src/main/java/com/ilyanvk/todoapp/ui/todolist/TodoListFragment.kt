package com.ilyanvk.todoapp.ui.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilyanvk.todoapp.Application
import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.data.TodoItem
import com.ilyanvk.todoapp.databinding.FragmentTodoListBinding
import com.ilyanvk.todoapp.ui.todoeditor.TodoEditorFragment.Companion.TO_EDIT_ID
import com.ilyanvk.todoapp.ui.todolist.recyclerview.TodoItemAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * [Fragment] class for displaying and managing the list of [TodoItem] objects.
 *
 * The [TodoListFragment] class is responsible for the following functionality:
 *
 * Displaying the list of [TodoItem] objects in a RecyclerView.
 *
 * Handling user interactions, such as clicking on a [TodoItem] or
 * toggling the visibility of completed [TodoItem] objects.
 *
 * Initiating data synchronization with the repository.
 *
 * Notifying the user of the network state and displaying appropriate messages.
 *
 * @property viewModel The [TodoListViewModel] instance for managing the state and business logic of the fragment.
 * @property adapter The [TodoItemAdapter] instance for displaying the list of TodoItems in the RecyclerView.
 * @property notifyErrorStateChange
 * Flag indicating whether to notify the error state change when the network state changes.
 */
class TodoListFragment : Fragment() {
    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!

    private val component by lazy {
        (requireActivity().application as Application).appComponent.addTodoListFragmentComponent()
    }
    private val viewModel: TodoListViewModel by activityViewModels {
        TodoListViewModel.Factory(component.provideTodoListViewModelFactory())
    }

    private val adapter = setUpTodoItemsAdapter()
    private var notifyErrorStateChange = false

    private val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in)
        .setExitAnim(R.anim.fade_out)
        .setPopEnterAnim(R.anim.fade_in)
        .setPopExitAnim(R.anim.slide_out)
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.todoItemList.observe(viewLifecycleOwner) { updateList(it) }
        viewModel.networkState.observe(viewLifecycleOwner) { onNetworkStateChange(it) }

        setUpRecyclerView()
        setUpSwipeRefresh()
        setUpVisibilityIcon()
        setUpFloatingActionButton()
        binding.settingsIcon.setOnClickListener {
            findNavController().navigate(R.id.action_todoList_to_settingsFragment, Bundle(), navOptions)
        }

        return view
    }

    private fun setUpSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            notifyErrorStateChange = true
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.syncData()
            }
        }
    }

    private fun onNetworkStateChange(networkState: NetworkState) {
        if (!notifyErrorStateChange) {
            return
        }
        when (networkState) {
            is NetworkState.Success -> Toast.makeText(
                context,
                getString(R.string.sync_successful),
                LENGTH_SHORT
            ).show()

            else -> Toast.makeText(
                context,
                getString(R.string.sync_failed),
                LENGTH_SHORT
            ).show()
        }
        notifyErrorStateChange = false
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun setUpVisibilityIcon() {
        updateVisibilityIcon()
        binding.completedVisibility.setOnClickListener {
            viewModel.onCompletedVisibilityChange()
            updateVisibilityIcon()
        }
    }

    private fun updateList(todoItemList: List<TodoItem>) {
        adapter.submitList(
            if (viewModel.showCompleted) todoItemList
            else todoItemList.filter { !it.isCompleted }
        )
        binding.completed.text =
            getString(R.string.completed, todoItemList.count { it.isCompleted })
    }

    private fun updateVisibilityIcon() {
        if (viewModel.showCompleted) {
            binding.completedVisibility.contentDescription = getString(R.string.hide_completed)
            binding.completedVisibility.setImageResource(R.drawable.visibility)
        } else {
            binding.completedVisibility.contentDescription = getString(R.string.show_completed)
            binding.completedVisibility.setImageResource(R.drawable.visibility_off)
        }
        viewModel.todoItemList.value?.let { updateList(it) }
    }

    private fun setUpFloatingActionButton() {

        binding.newTodoFloatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_todoList_to_todoEditor, Bundle(), navOptions)
        }
    }

    private fun setUpRecyclerView() {
        val todoItemsRecyclerView = binding.todoItems
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        todoItemsRecyclerView.adapter = adapter
        todoItemsRecyclerView.layoutManager = layoutManager
    }

    private fun setUpTodoItemsAdapter(): TodoItemAdapter {
        return TodoItemAdapter(
            onTaskClick = { todoItem, itemView ->
                val bundle = Bundle()
                bundle.putString(TO_EDIT_ID, todoItem.id)
                Navigation.findNavController(itemView)
                    .navigate(R.id.action_todoList_to_todoEditor, bundle, navOptions)
            },
            onCheckboxClick = {
                viewModel.changeCompleteStatus(it)
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
