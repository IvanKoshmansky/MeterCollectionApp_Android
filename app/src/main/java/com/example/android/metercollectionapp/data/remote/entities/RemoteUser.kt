package com.example.android.metercollectionapp.data.remote.entities

import com.example.android.metercollectionapp.SyncStatus

data class RemoteUser (
    val id: Long = 0,
    val login: String = "",
    val passEncrypted: String = "",
    val name: String = "",
    val status: SyncStatus = SyncStatus.UNKNOWN
)
