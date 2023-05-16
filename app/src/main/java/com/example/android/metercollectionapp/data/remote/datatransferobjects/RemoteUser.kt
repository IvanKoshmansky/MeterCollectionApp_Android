package com.example.android.metercollectionapp.data.remote.datatransferobjects

import com.example.android.metercollectionapp.SyncStatus

data class RemoteUser (
    val id: Long,
    val login: String,
    val passEncrypted: String,
    val name: String,
    val status: SyncStatus
)
