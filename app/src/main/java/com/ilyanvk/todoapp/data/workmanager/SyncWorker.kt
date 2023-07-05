package com.ilyanvk.todoapp.data.workmanager

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.ilyanvk.todoapp.data.TodoItemsRepository
import com.ilyanvk.todoapp.data.database.TodoItemEntity
import com.ilyanvk.todoapp.data.retrofit.TodoItemServer
import com.ilyanvk.todoapp.data.retrofit.models.TodoItemApiRequestList
import java.util.concurrent.TimeUnit

class SyncWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val localItems = TodoItemsRepository.dao.getAll()
        val revision = TodoItemsRepository.sharedPreferences.revision
        val requestList = TodoItemApiRequestList("ok", localItems.map {
            TodoItemServer.fromTodoItem(
                it.toTodoItem(), TodoItemsRepository.sharedPreferences.deviceId ?: "null"
            )
        })
        val response = TodoItemsRepository.api.updateTodoItemsList(revision, requestList)
        if (response.isSuccessful) {
            val responseData = response.body()
            if (responseData != null) {
                val serverItems =
                    responseData.list.map { TodoItemEntity.fromTodoItem(it.toTodoItem()) }

                TodoItemsRepository.dao.clear()
                TodoItemsRepository.dao.insertAll(serverItems)

                TodoItemsRepository.sharedPreferences.revision = responseData.revision
                TodoItemsRepository.onRepositoryUpdate()
            } else {
                return Result.retry()
            }
        } else {
            return Result.retry()
        }
        return Result.success()
    }


    companion object {
        private const val WORK_NAME = "SyncWorker"

        fun enqueuePeriodicSync(context: Context) {
            val constraints =
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

            val periodicRequest = PeriodicWorkRequestBuilder<SyncWorker>(
                8, TimeUnit.HOURS
            ).setConstraints(constraints).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, periodicRequest
            )
        }
    }
}
