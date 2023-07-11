package com.ilyanvk.todoapp.data.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ilyanvk.todoapp.data.TodoSyncFailed
import com.ilyanvk.todoapp.data.repository.TodoItemsRepository

/**
 * Worker class for performing synchronization of data sources in the background.
 *
 * The [SyncWorker] class extends the [CoroutineWorker] class and performs the synchronization
 * of data sources by invoking the syncDataSources() method on the TodoItemsRepository.
 *
 * @param context The application context.
 *
 * @param params The parameters for the worker.
 *
 * @param repository The [TodoItemsRepository] instance.
 */
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
