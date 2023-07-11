package com.ilyanvk.todoapp

import android.app.Application
import androidx.work.Configuration
import androidx.work.Configuration.Provider
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.ilyanvk.todoapp.data.workmanager.SyncWorker
import com.ilyanvk.todoapp.data.workmanager.SyncWorkerConstants.REPEAT_INTERVAL
import com.ilyanvk.todoapp.data.workmanager.SyncWorkerConstants.UNIQUE_WORK_NAME
import com.ilyanvk.todoapp.data.workmanager.SyncWorkerFactory
import com.ilyanvk.todoapp.di.components.AppComponent
import com.ilyanvk.todoapp.di.components.DaggerAppComponent
import com.ilyanvk.todoapp.di.modules.AppModule
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class Application : Application(), Provider {
    lateinit var appComponent: AppComponent

    @Inject
    lateinit var workerFactory: SyncWorkerFactory
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        appComponent.injectApplication(this)
        setupPeriodicDataSync()
    }

    private fun setupPeriodicDataSync() {
        val workRequest = PeriodicWorkRequestBuilder<SyncWorker>(REPEAT_INTERVAL, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder().setMinimumLoggingLevel(android.util.Log.INFO)
            .setWorkerFactory(workerFactory).build()
    }
}
