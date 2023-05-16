package com.example.android.metercollectionapp.data.remote.datatransferobjects

import com.example.android.metercollectionapp.SyncStatus

data class RemoteDevice (
    val guid: Long,
    val devType: Int,
    val name: String,
    val lat: Double,
    val lon: Double,
    val status: SyncStatus
)
