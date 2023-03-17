package com.example.android.metercollectionapp.presentation.uistate

import com.example.android.metercollectionapp.SyncStatus

data class ObjectUiState (
    val uid: Long = 0,
    val status: SyncStatus = SyncStatus.UNKNOWN,
    val name: String = ""
)
