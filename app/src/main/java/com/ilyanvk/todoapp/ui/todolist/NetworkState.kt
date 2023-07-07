package com.ilyanvk.todoapp.ui.todolist

sealed class NetworkState {
    object Success : NetworkState()
    object Error : NetworkState()
}
