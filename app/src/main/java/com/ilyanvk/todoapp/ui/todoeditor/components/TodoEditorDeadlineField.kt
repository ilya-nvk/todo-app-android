package com.ilyanvk.todoapp.ui.todoeditor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.ui.theme.AppTheme
import com.ilyanvk.todoapp.ui.todoeditor.TodoEditorAction
import java.text.DateFormat

@Composable
fun TodoEditorDeadlineField(
    deadline: Long?,
    onAction: (TodoEditorAction) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(72.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.deadline),
                style = AppTheme.typography.body,
                color = AppTheme.colors.labelPrimary
            )
            if (deadline != null) {
                val dateText = DateFormat.getDateInstance(DateFormat.DEFAULT).format(deadline)
                Text(
                    text = dateText,
                    style = AppTheme.typography.subhead,
                    color = AppTheme.colors.colorBlue
                )
            }
        }
        Switch(
            checked = deadline != null,
            onCheckedChange = { onAction(TodoEditorAction.UpdateDeadlinePresence) },
            colors = androidx.compose.material.SwitchDefaults.colors(
                checkedThumbColor = AppTheme.colors.colorBlue,
                checkedTrackColor = AppTheme.colors.colorBlue,
                uncheckedThumbColor = AppTheme.colors.backElevated,
                uncheckedTrackColor = AppTheme.colors.supportOverlay
            )
        )
    }

}

@Preview
@Composable
fun PreviewTodoEditorDeadlineFieldOn() {
    AppTheme {
        TodoEditorDeadlineField(74343278) {}
    }
}

@Preview
@Composable
fun PreviewTodoEditorDeadlineFieldOff() {
    AppTheme {
        TodoEditorDeadlineField(null) {}
    }
}
