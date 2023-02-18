package com.example.android.metercollectionapp.presentation.uistate

import com.example.android.metercollectionapp.SyncStatus

data class ObjectUiState (
    val uid: Long,
    val status: SyncStatus = SyncStatus.UNKNOWN,
    val name: String = ""
)
