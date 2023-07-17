package com.ilyanvk.todoapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.ilyanvk.todoapp.Application
import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.ui.theme.AppTheme

class SettingsFragment : Fragment() {
    private val component by lazy {
        (requireActivity().application as Application).appComponent.addSettingsFragmentComponent()
    }
    private val viewModel: SettingsViewModel by activityViewModels {
        SettingsViewModel.Factory(component.provideSettingsViewModelFactory())
    }

    private val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.fade_in)
        .setExitAnim(R.anim.slide_out)
        .setPopEnterAnim(R.anim.fade_out)
        .setPopExitAnim(R.anim.slide_in)
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext())
        view.apply {
            setContent {
                AppTheme {
                    SettingsScreen(
                        currentTheme = viewModel.currentTheme,
                        onAction = ::onSettingsAction
                    )
                }
            }
        }

        return view
    }

    private fun onSettingsAction(action: SettingsAction) {
        when (action) {
            SettingsAction.Close -> findNavController().navigate(
                R.id.action_settingsFragment_to_todoList,
                Bundle(),
                navOptions
            )

            is SettingsAction.UpdateTheme -> viewModel.updateTheme(action.theme)
        }
    }
}