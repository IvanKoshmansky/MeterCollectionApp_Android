package com.example.android.metercollectionapp.presentation.uistate

import com.example.android.metercollectionapp.SyncStatus

data class SyncValuesRowUiState (
    val uid: Long = 0,
    val valueSyncStatus: SyncStatus = SyncStatus.UNKNOWN,
    val shortName: String = "",
    val stringValue: String = "",
    val measUnit: String = ""
)
