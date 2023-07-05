package com.ilyanvk.todoapp.data

import com.ilyanvk.todoapp.R
import com.ilyanvk.todoapp.data.database.TodoItemDao
import com.ilyanvk.todoapp.data.database.TodoItemEntity
import com.ilyanvk.todoapp.data.retrofit.Error
import com.ilyanvk.todoapp.data.retrofit.TodoItemApi
import com.ilyanvk.todoapp.data.retrofit.TodoItemServer
import com.ilyanvk.todoapp.data.retrofit.models.TodoItemApiRequest
import com.ilyanvk.todoapp.data.retrofit.models.TodoItemApiRequestList
import kotlinx.coroutines.delay

object TodoItemsRepository {
    lateinit var dao: TodoItemDao
    lateinit var api: TodoItemApi
    lateinit var sharedPreferences: AppSettings

    var onRepositoryUpdate: () -> Unit = {}
    var onRemoteUpdate: (message: String) -> Unit = {}
    var afterSync: suspend (Boolean) -> Unit = {}
    var createSnackbar: (Int) -> Unit = {}

    var toEdit: TodoItem? = null
    var isDataLoaded = false
    var connectionAvailable = true
    var showCompleted = true
        set(value) {
            field = value
            onRepositoryUpdate()
        }

    fun getAllTodoItems(): List<TodoItem> {
        return when (showCompleted) {
            false -> dao.getUncompleted().map { it.toTodoItem() }
            else -> dao.getAll().map { it.toTodoItem() }
        }
    }

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
        dao.insert(TodoItemEntity.fromTodoItem(newTodoItem))
        addTodoItemOnServer(newTodoItem)
        onRepositoryUpdate()
    }

    suspend fun updateTodoItem(todoItem: TodoItem) {
        dao.update(TodoItemEntity.fromTodoItem(todoItem))
        updateTodoItemOnServer(todoItem)
        onRepositoryUpdate()
    }

    suspend fun deleteTodoItem(todoItem: TodoItem) {
        dao.delete(TodoItemEntity.fromTodoItem(todoItem))
        deleteTodoItemFromServer(todoItem)
        onRepositoryUpdate()
    }

    suspend fun changeCompletionOfTodoItem(todoItem: TodoItem) {
        updateTodoItem(
            todoItem.copy(
                isCompleted = !todoItem.isCompleted, modificationDate = System.currentTimeMillis()
            )
        )
    }

    private suspend fun addTodoItemOnServer(todoItem: TodoItem) {
        try {
            retryWithDelay(3) {
                val response = api.addTodoItem(
                    sharedPreferences.revision, TodoItemApiRequest(
                        TodoItemServer.fromTodoItem(
                            todoItem, sharedPreferences.deviceId ?: "null"
                        )
                    )
                )
                if (response.isSuccessful) {
                    response.body()?.let { sharedPreferences.revision = it.revision }
                } else {
                    handleServerError(response.code())
                    throw Exception()
                }
            }
        } catch (e: Exception) {
            createSnackbar(R.string.add_failed)
        }
    }

    private suspend fun updateTodoItemOnServer(todoItem: TodoItem) {
        try {
            retryWithDelay(3) {
                val response = api.updateTodoItem(
                    sharedPreferences.revision, todoItem.id, TodoItemApiRequest(
                        TodoItemServer.fromTodoItem(
                            todoItem, sharedPreferences.deviceId ?: "null"
                        )
                    )
                )
                if (response.isSuccessful) {
                    response.body()?.let { sharedPreferences.revision = it.revision }
                } else {
                    handleServerError(response.code())
                    throw Exception()
                }
            }
        } catch (e: Exception) {
            createSnackbar(R.string.update_failed)
        }
    }


    private suspend fun deleteTodoItemFromServer(todoItem: TodoItem) {
        try {
            retryWithDelay(3) {
                val response = api.deleteTodoItem(sharedPreferences.revision, todoItem.id)
                if (response.isSuccessful) {
                    response.body()?.let { sharedPreferences.revision = it.revision }
                } else {
                    handleServerError(response.code())
                    throw Exception()
                }
            }
        } catch (e: Exception) {
            createSnackbar(R.string.delete_failed)
        }
    }

    suspend fun getTodoItemsFromServer() {
        try {
            retryWithDelay(3) {
                val response = api.getList()
                if (response.isSuccessful) {
                    val todoItems =
                        response.body()?.list?.map { it.toTodoItem() } ?: listOf<TodoItem>()
                    dao.insertAll(todoItems.map { TodoItemEntity.fromTodoItem(it) })
                    response.body()?.let { sharedPreferences.revision = it.revision }
                } else {
                    handleServerError(response.code())
                    throw Exception()
                }
            }
        } catch (e: Exception) {
            handleServerError(Error.UNKNOWN.code)
        }
        isDataLoaded = true
        onRepositoryUpdate()
    }

    suspend fun syncTodoItems(notify: Boolean = false) {
        try {
            retryWithDelay(3) {
                val localItems = dao.getAll()
                val revision = sharedPreferences.revision
                val requestList = TodoItemApiRequestList("ok", localItems.map {
                    TodoItemServer.fromTodoItem(
                        it.toTodoItem(), sharedPreferences.deviceId ?: "null"
                    )
                })
                val response = api.updateTodoItemsList(revision, requestList)
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null) {
                        val serverItems = responseData.list.map { it.toTodoItem() }

                        dao.clear()
                        dao.insertAll(serverItems.map { TodoItemEntity.fromTodoItem(it) })

                        sharedPreferences.revision = responseData.revision
                        onRepositoryUpdate()
                    } else {
                        if (notify) afterSync(false)
                        handleServerError(response.code())
                    }
                } else {
                    handleServerError(response.code())
                    throw Exception()
                }
            }

        } catch (e: Exception) {
            if (notify) afterSync(false)
        }
        if (notify) afterSync(true)
    }

    private fun handleServerError(errorCode: Int) {
        when (errorCode) {
            Error.REVISION.code -> {
                onRemoteUpdate("400")
            }

            Error.AUTHORISATION.code -> {
                onRemoteUpdate("401")
            }

            Error.NOT_FOUND.code -> {
                onRemoteUpdate("404")
            }

            else -> {
                onRemoteUpdate("Unknown error: $errorCode")
            }
        }
    }

    suspend fun retryWithDelay(
        retries: Int, block: suspend () -> Unit
    ) {
        var lastException: Exception? = null
        repeat(retries) {
            try {
                block()
                return
            } catch (e: Exception) {
                lastException = e
            }
            delay(1000)
        }
        throw lastException ?: RuntimeException("Request failed after $retries attempts")
    }
}
