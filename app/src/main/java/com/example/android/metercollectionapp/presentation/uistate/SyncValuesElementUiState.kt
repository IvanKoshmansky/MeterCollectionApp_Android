package com.example.android.metercollectionapp.presentation.uistate

import com.example.android.metercollectionapp.SyncStatus
import com.example.android.metercollectionapp.domain.model.DeviceParamType

data class SyncValuesElementUiState (
    val uid: Long = 0,
    val paramType: DeviceParamType = DeviceParamType.UINT32,
    val syncStatus: SyncStatus = SyncStatus.UNKNOWN,
    val shortName: String = "",
    val stringValue: String = "",
    val measUnit: String = ""
)
