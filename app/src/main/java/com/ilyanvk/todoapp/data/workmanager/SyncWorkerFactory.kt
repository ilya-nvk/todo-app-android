package com.ilyanvk.todoapp.data.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.ilyanvk.todoapp.data.repository.TodoItemsRepository
import com.ilyanvk.todoapp.di.scopes.AppScope
import javax.inject.Inject


@AppScope
class SyncWorkerFactory @Inject constructor(private val repository: TodoItemsRepository) :
    WorkerFactory() {
    override fun createWorker(
        context: Context,
        workerClassName: String,
        params: WorkerParameters
    ): ListenableWorker {
        return SyncWorker(context, params, repository)
    }
}
