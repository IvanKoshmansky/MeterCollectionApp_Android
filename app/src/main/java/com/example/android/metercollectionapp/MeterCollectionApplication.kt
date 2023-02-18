package com.example.android.metercollectionapp

import android.app.Application
import com.example.android.metercollectionapp.di.DaggerAppComponent

class MeterCollectionApplication : Application() {

    val appComponent = DaggerAppComponent
        .builder()
        .applicationContext(this)
        .build()

}
