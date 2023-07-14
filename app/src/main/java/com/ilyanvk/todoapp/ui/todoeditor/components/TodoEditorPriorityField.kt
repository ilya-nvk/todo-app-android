@file:OptIn(ExperimentalMaterial3Api::class)

package com.ilyanvk.todoapp.ui.todoeditor.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.data.Priority
import com.ilyanvk.todoapp.ui.theme.AppTheme
import com.ilyanvk.todoapp.ui.todoeditor.TodoEditorAction
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoEditorPriorityField(
    priority: Priority,
    onAction: (TodoEditorAction) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (showBottomSheet) {
        PriorityBottomSheet(
            hide = {
                scope.launch {
                    bottomSheetState.hide()
                    showBottomSheet = false
                }
            },
            onAction = onAction
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                scope.launch {
                    showBottomSheet = true
                    bottomSheetState.show()
                }
            }
            .padding(16.dp)
    ) {
        val color by animateColorAsState(
            targetValue = when (priority) {
                Priority.HIGH -> AppTheme.colors.colorRed
                else -> AppTheme.colors.labelSecondary
            },
            animationSpec = tween(200, easing = FastOutSlowInEasing),
            label = "color"
        )
        Text(
            text = stringResource(id = R.string.priority),
            style = AppTheme.typography.body,
            color = AppTheme.colors.labelPrimary
        )
        Text(
            text = stringResource(
                id = when (priority) {
                    Priority.HIGH -> R.string.high
                    Priority.LOW -> R.string.low
                    else -> R.string.no
                }
            ),
            style = AppTheme.typography.subhead,
            color = color
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityBottomSheet(
    hide: () -> Unit,
    onAction: (TodoEditorAction) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { hide() },
        containerColor = AppTheme.colors.backSecondary
    ) {
        Column(Modifier.padding(bottom = 48.dp)) {
            PriorityItem(
                text = stringResource(R.string.no),
                color = AppTheme.colors.labelPrimary
            ) {
                onAction(TodoEditorAction.UpdatePriority(Priority.MEDIUM))
                hide()
            }

            PriorityItem(
                text = stringResource(R.string.low),
                color = AppTheme.colors.labelPrimary
            ) {
                onAction(TodoEditorAction.UpdatePriority(Priority.LOW))
                hide()
            }

            PriorityItem(
                text = stringResource(R.string.high),
                color = AppTheme.colors.colorRed
            ) {
                onAction(TodoEditorAction.UpdatePriority(Priority.HIGH))
                hide()
            }
        }
    }
}

@Composable
fun PriorityItem(text: String, color: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(text = text, style = AppTheme.typography.body, color = color)
    }
}


@Preview
@Composable
fun PreviewTodoEditorPriorityField() {
    AppTheme {
        TodoEditorPriorityField(priority = Priority.MEDIUM) {}
    }
}
