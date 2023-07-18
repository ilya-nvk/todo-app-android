package com.ilyanvk.todoapp.ui.todoeditor

import androidx.compose.runtime.Immutable
import com.ilyanvk.todoapp.data.Priority

@Immutable
sealed interface TodoEditorAction {
    data class UpdateText(val text: String) : TodoEditorAction
    data class UpdatePriority(val priority: Priority) : TodoEditorAction
    data class UpdateDeadline(val deadline: Long?) : TodoEditorAction

    object Close : TodoEditorAction
    object Save : TodoEditorAction
    object Delete : TodoEditorAction
}
