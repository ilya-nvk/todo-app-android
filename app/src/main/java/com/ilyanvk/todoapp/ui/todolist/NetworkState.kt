package com.ilyanvk.todoapp.ui.todolist

/**
 * Sealed class representing the network state of a data operation.
 *
 * The [NetworkState] class has two subclasses:
 *
 * [Success]: Indicates that the data operation was successful.
 *
 * [Error]: Indicates that the data operation encountered an error.
 */
sealed class NetworkState {
    /**
     * Represents a successful network state.
     */
    object Success : NetworkState()

    /**
     * Represents an error network state.
     */
    object Error : NetworkState()
}
