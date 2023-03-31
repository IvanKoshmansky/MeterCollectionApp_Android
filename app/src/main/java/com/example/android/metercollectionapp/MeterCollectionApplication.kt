package com.example.android.metercollectionapp

import android.app.Application
import androidx.work.*
import com.example.android.metercollectionapp.di.DaggerAppComponent
import com.example.android.metercollectionapp.infrastructure.UploadWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MeterCollectionApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerConfiguration: Configuration

    override fun getWorkManagerConfiguration(): Configuration {
        return workerConfiguration
    }

    private var _cameraPermissionGranted = false
    val cameraPermissionGranted: Boolean
        get() = _cameraPermissionGranted
    fun setCameraPermissionGranted() {
        _cameraPermissionGranted = true
    }

    val appComponent = DaggerAppComponent
        .builder()
        .applicationContext(this)
        .build()

    override fun onCreate() {
        // нужно для проведения зависимости на workerConfiguration
        appComponent.inject(this)  // workerConfiguration становится доступной
        super.onCreate()
        scheduleWork()
        //TODO: после базовой реализации имитации передачи, посмотреть еще раз лекцию Google Dev Summit по этой теме
        // показать реализацию через Dagger
    }

    private fun scheduleWork() {

        val constraints = Constraints.Builder()
            //.setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<UploadWorker>(UploadWorker.REPEAT_INTERVAL, TimeUnit.MINUTES)
            .setInitialDelay(UploadWorker.MINIMUM_LATENCY, TimeUnit.SECONDS)
            .addTag(UploadWorker.UPLOAD_WORKER_TAG)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                UploadWorker.UNIQUE_UPLOAD_WORKER,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest
            )
    }
}
