package com.ilyanvk.todoapp.data

import com.ilyanvk.todoapp.data.database.TodoItemDao
import com.ilyanvk.todoapp.data.retrofit.ErrorResponse
import com.ilyanvk.todoapp.data.retrofit.TodoItemApi
import com.ilyanvk.todoapp.data.retrofit.TodoItemApiRequest
import com.ilyanvk.todoapp.data.retrofit.TodoItemApiRequestList
import com.ilyanvk.todoapp.data.retrofit.TodoItemServer

object TodoItemsRepository {
    lateinit var dao: TodoItemDao
    lateinit var api: TodoItemApi
    lateinit var sharedPreferences: AppSettings

    var toEdit: TodoItem? = null
    var onRepositoryUpdate: () -> Unit = {}
    var onRemoteUpdate: (message: String) -> Unit = {}
    var showCompleted = true
        set(value) {
            field = value
            onRepositoryUpdate()
        }

    fun getAllTodoItems() = dao.getAll()

    fun countCompletedTodoItems(): Int {
        return dao.countCompleted()
    }

    suspend fun addTodoItem(todoItem: TodoItem) {
        val newTodoItem = TodoItem(
            text = todoItem.text,
            isCompleted = todoItem.isCompleted,
            priority = todoItem.priority,
            deadline = todoItem.deadline,
            creationDate = todoItem.creationDate
        )
        dao.insert(newTodoItem)
        addTodoItemOnServer(newTodoItem)
        onRepositoryUpdate()
    }

    suspend fun updateTodoItem(todoItem: TodoItem) {
        dao.update(todoItem)
        updateTodoItemOnServer(todoItem)
        onRepositoryUpdate()
    }

    suspend fun deleteTodoItem(todoItem: TodoItem) {
        dao.delete(todoItem)
        deleteTodoItem(todoItem)
        onRepositoryUpdate()
    }

    suspend fun changeCompletionOfTodoItem(todoItem: TodoItem) {
        updateTodoItem(
            todoItem.copy(
                isCompleted = !todoItem.isCompleted,
                modificationDate = System.currentTimeMillis()
            )
        )
    }

    private suspend fun addTodoItemOnServer(todoItem: TodoItem) {
        try {
            val response = api.addTodoItem(
                sharedPreferences.revisionId, TodoItemApiRequest(
                    TodoItemServer.fromTodoItem(
                        todoItem, sharedPreferences.deviceId ?: "null"
                    )
                )
            )
            if (response.isSuccessful) {
                response.body()?.let { sharedPreferences.revisionId = it.revision }
            } else {
                handleServerError(response.code())
            }
        } catch (e: Exception) {
            handleServerError(ErrorResponse.UNKNOWN.code)
        }
    }

    private suspend fun updateTodoItemOnServer(todoItem: TodoItem) {
        try {
            val response = api.updateTodoItem(
                sharedPreferences.revisionId, todoItem.id, TodoItemApiRequest(
                    TodoItemServer.fromTodoItem(
                        todoItem, sharedPreferences.deviceId ?: "null"
                    )
                )
            )
            if (response.isSuccessful) {
                response.body()?.let { sharedPreferences.revisionId = it.revision }
            } else {
                handleServerError(response.code())
            }
        } catch (e: Exception) {
            handleServerError(ErrorResponse.UNKNOWN.code)
        }
    }


    private suspend fun deleteTodoItemFromServer(todoItemId: String) {
        try {
            val response = api.deleteTodoItem(sharedPreferences.revisionId, todoItemId)
            if (response.isSuccessful) {
                response.body()?.let { sharedPreferences.revisionId = it.revision }
            } else {
                handleServerError(response.code())
            }
        } catch (e: Exception) {
            handleServerError(ErrorResponse.UNKNOWN.code)
        }
    }

    suspend fun getTodoItemsFromServer() {
        try {
            val response = api.getList()
            if (response.isSuccessful) {
                val todoItems = response.body()?.list?.map { it.toTodoItem() } ?: listOf()
                dao.insertAll(todoItems)
                response.body()?.let { sharedPreferences.revisionId = it.revision }
            } else {
                handleServerError(response.code())
            }
        } catch (e: Exception) {
            handleServerError(ErrorResponse.UNKNOWN.code)
        }
        onRepositoryUpdate()
    }

    suspend fun syncTodoItems() {
        try {
            val localItems = dao.getAllAsList()
            val revision = sharedPreferences.revisionId
            val requestList = TodoItemApiRequestList("ok", localItems.map {
                TodoItemServer.fromTodoItem(
                    it, sharedPreferences.deviceId ?: "null"
                )
            })
            val response = api.updateTodoItemsList(revision, requestList)

            if (response.isSuccessful) {
                val responseData = response.body()
                if (responseData != null) {
                    val serverItems = responseData.list.map { it.toTodoItem() }

                    dao.clear()
                    dao.insertAll(serverItems)

                    sharedPreferences.revisionId = responseData.revision
                    onRepositoryUpdate()
                } else {
                    handleServerError(response.code())
                }
            } else {
                handleServerError(response.code())
            }
        } catch (e: Exception) {
            handleServerError(ErrorResponse.UNKNOWN.code)
        }
    }

    private fun handleServerError(errorCode: Int) {
        when (errorCode) {
            ErrorResponse.REVISION.code -> {
                onRemoteUpdate("400")
            }

            ErrorResponse.AUTHORISATION.code -> {
                onRemoteUpdate("401")
            }

            ErrorResponse.NOT_FOUND.code -> {
                onRemoteUpdate("404")
            }

            else -> {
                onRemoteUpdate("Uknown error")
            }
        }
    }
}
