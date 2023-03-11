package com.example.android.metercollectionapp

import android.app.Application
import com.example.android.metercollectionapp.di.DaggerAppComponent

class MeterCollectionApplication : Application() {

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

}
