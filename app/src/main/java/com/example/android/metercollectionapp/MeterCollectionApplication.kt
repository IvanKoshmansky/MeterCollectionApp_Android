package com.example.android.metercollectionapp

import android.app.Application
import androidx.work.*
import com.example.android.metercollectionapp.di.DaggerAppComponent
import com.example.android.metercollectionapp.infrastructure.UploadWorker
import javax.inject.Inject

private const val MINIMUM_LATENCY = 5L
private const val UNIQUE_UPLOAD_WORKER = "UNIQUEUPLOADWORKER"
private const val UPLOAD_WORKER_TAG = "UPLOADWORKERTAG"

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
        //TODO: после базовой реализации посмотреть еще раз лекцию Google Dev Summit по этой теме
        // в том числе оповещение о статусе работы менеджера через LiveData (см. codelab)
    }

    private fun scheduleWork() {
        //val constraints = Constraints.Builder()
        //    .setRequiredNetworkType(NetworkType.UNMETERED)
        //    .build()

        val uploadWorkerRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .addTag(UPLOAD_WORKER_TAG)
            //    .setInitialDelay(MINIMUM_LATENCY, TimeUnit.SECONDS)
            //    .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniqueWork(
                UNIQUE_UPLOAD_WORKER,
                ExistingWorkPolicy.KEEP,
                uploadWorkerRequest
            )
    }
}
