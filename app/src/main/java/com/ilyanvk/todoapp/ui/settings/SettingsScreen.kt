package com.ilyanvk.todoapp.ui.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(currentTheme: ThemeMode, onAction: (SettingsAction) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { onAction(SettingsAction.Close) },
                        enabled = true,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = AppTheme.colors.labelPrimary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.go_back)
                        )
                    }
                },
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colors.backPrimary,
                    navigationIconContentColor = AppTheme.colors.labelPrimary
                )
            )
        },
        containerColor = AppTheme.colors.backPrimary
    ) { paddingValues ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            item {
                val radioButtonColors = RadioButtonDefaults.colors(
                    selectedColor = AppTheme.colors.colorBlue,
                    unselectedColor = AppTheme.colors.labelPrimary
                )

                Text(
                    text = stringResource(id = R.string.current_theme),
                    style = AppTheme.typography.title,
                    color = AppTheme.colors.labelPrimary,
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 14.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = currentTheme == ThemeMode.DEFAULT,
                        onClick = {
                            onAction(SettingsAction.updateTheme(ThemeMode.DEFAULT))
                        },
                        colors = radioButtonColors
                    )
                    Text(
                        text = stringResource(id = R.string.default_theme),
                        style = AppTheme.typography.body,
                        color = AppTheme.colors.labelPrimary
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = currentTheme == ThemeMode.LIGHT,
                        onClick = {
                            onAction(SettingsAction.updateTheme(ThemeMode.LIGHT))
                        },
                        colors = radioButtonColors
                    )
                    Text(
                        text = stringResource(id = R.string.light_theme),
                        style = AppTheme.typography.body,
                        color = AppTheme.colors.labelPrimary
                    )

                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = currentTheme == ThemeMode.DARK,
                        onClick = {
                            onAction(SettingsAction.updateTheme(ThemeMode.DARK))
                        },
                        colors = radioButtonColors
                    )
                    Text(
                        text = stringResource(id = R.string.dark_theme),
                        style = AppTheme.typography.body,
                        color = AppTheme.colors.labelPrimary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewSettingsScreen() {
    AppTheme {
        SettingsScreen(currentTheme = ThemeMode.DEFAULT, {})
    }
}
