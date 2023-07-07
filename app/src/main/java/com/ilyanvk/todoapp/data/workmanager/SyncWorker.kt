package com.ilyanvk.todoapp.data.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ilyanvk.todoapp.data.TodoSyncFailed
import com.ilyanvk.todoapp.data.repository.TodoItemsRepository

class SyncWorker(
    context: Context,
    params: WorkerParameters,
    private val repository: TodoItemsRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            repository.syncDataSources()
            Result.success()
        } catch (e: TodoSyncFailed) {
            Result.retry()
        }
    }
}
