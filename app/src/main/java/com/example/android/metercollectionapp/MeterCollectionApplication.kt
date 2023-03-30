package com.example.android.metercollectionapp

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.android.metercollectionapp.di.DaggerAppComponent
import com.example.android.metercollectionapp.infrastructure.UploadWorker
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
        appComponent.inject(this)
        super.onCreate()

    }
}
