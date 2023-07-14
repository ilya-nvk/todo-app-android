package com.ilyanvk.todoapp.ui.todoeditor.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.ui.StringConverter.DAY
import com.ilyanvk.todoapp.ui.StringConverter.HOUR
import com.ilyanvk.todoapp.ui.StringConverter.MINUTE
import com.ilyanvk.todoapp.ui.StringConverter.toDateTimeString
import com.ilyanvk.todoapp.ui.theme.AppTheme
import com.ilyanvk.todoapp.ui.todoeditor.TodoEditorAction
import java.util.Calendar
import java.util.TimeZone

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
        var currentDeadline by remember { mutableStateOf(deadline) }
        var showDatePickerDialog by remember { mutableStateOf(false) }
        var showTimePickerDialog by remember { mutableStateOf(false) }

        DeadlineDatePicker(
            date = deadline,
            show = showDatePickerDialog,
            showTimePicker = {
                currentDeadline = it
                showTimePickerDialog = true
            },
            onDialogClose = { showDatePickerDialog = false }
        )

        DeadlineTimePicker(
            selectedDate = currentDeadline ?: 0,
            deadline = deadline,
            open = showTimePickerDialog,
            onDialogClose = { showTimePickerDialog = false },
            onAction = onAction
        )

        Column {
            Text(
                text = stringResource(id = R.string.deadline),
                style = AppTheme.typography.body,
                color = AppTheme.colors.labelPrimary
            )
            AnimatedVisibility(deadline != null) {
                val dateText =
                    remember(deadline) {
                        deadline?.toDateTimeString()
                    }
                if (dateText != null) {
                    Text(
                        modifier = Modifier.clickable { showDatePickerDialog = true },
                        text = dateText,
                        style = AppTheme.typography.subhead,
                        color = AppTheme.colors.colorBlue
                    )
                }
            }
        }
        Switch(
            checked = deadline != null,
            onCheckedChange = {
                if (it) showDatePickerDialog = true
                else onAction(TodoEditorAction.UpdateDeadline(null))
            },
            colors = androidx.compose.material.SwitchDefaults.colors(
                checkedThumbColor = AppTheme.colors.colorBlue,
                checkedTrackColor = AppTheme.colors.colorBlue,
                uncheckedThumbColor = AppTheme.colors.backElevated,
                uncheckedTrackColor = AppTheme.colors.supportOverlay
            )
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeadlineDatePicker(
    date: Long?,
    show: Boolean,
    onDialogClose: () -> Unit,
    showTimePicker: (Long) -> Unit
) {
    if (!show) return

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = (date?.minus(TimeZone.getDefault().rawOffset))
            ?: System.currentTimeMillis()
    )
    val saveButtonEnabled by remember(datePickerState.selectedDateMillis) {
        derivedStateOf { datePickerState.selectedDateMillis != null }
    }

    DatePickerDialog(
        onDismissRequest = onDialogClose,
        confirmButton = {
            TextButton(
                onClick = {
                    onDialogClose()
                    showTimePicker(datePickerState.selectedDateMillis ?: 0)
                },
                enabled = saveButtonEnabled
            ) {
                Text(
                    stringResource(R.string.select_time),
                    style = AppTheme.typography.button
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDialogClose()
                }
            ) {
                Text(
                    stringResource(R.string.cancel),
                    style = AppTheme.typography.button
                )
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            dateValidator = { it > System.currentTimeMillis() - DAY },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeadlineTimePicker(
    selectedDate: Long,
    deadline: Long?,
    open: Boolean,
    onDialogClose: () -> Unit,
    onAction: (TodoEditorAction) -> Unit
) {
    if (!open) return
    val calendar = Calendar.getInstance()
    calendar.timeInMillis =
        (deadline?.minus(TimeZone.getDefault().rawOffset)) ?: System.currentTimeMillis()
    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE)
    )
    TimePickerDialog(
        onCancel = { onDialogClose() },
        onConfirm = {
            val newDeadline =
                selectedDate + timePickerState.hour * HOUR + timePickerState.minute * MINUTE
            onAction(TodoEditorAction.UpdateDeadline(newDeadline))
            onDialogClose()
        }
    ) {
        TimePicker(state = timePickerState)
    }
}


@Preview
@Composable
fun PreviewDeadlineFieldOn() {
    AppTheme {
        TodoEditorDeadlineField(1689195600000) {}
    }
}

@Preview
@Composable
fun PreviewDeadlineFieldOff() {
    AppTheme {
        TodoEditorDeadlineField(null) {}
    }
}

@Preview
@Composable
fun PreviewDatePicker() {
    AppTheme {
        DeadlineDatePicker(
            date = 74343278,
            show = true,
            onDialogClose = {},
            showTimePicker = {}
        )
    }
}

@Preview
@Composable
fun PreviewTimePicker() {
    AppTheme {
        DeadlineTimePicker(
            selectedDate = 74343278,
            open = true,
            onDialogClose = {},
            deadline = 74343278,
            onAction = {}
        )
    }
}
