package com.example.android.metercollectionapp.data.remote.datatransferobjects

import com.example.android.metercollectionapp.SyncStatus

data class RemoteDeviceParam (
    val uid: Long,
    val paramType: Int,
    val measUnit: String,
    val name: String,
    val shortName: String,
    val status: SyncStatus = SyncStatus.UNKNOWN
)
