package com.example.android.metercollectionapp.presentation.uistate

import com.example.android.metercollectionapp.SyncStatus
import com.example.android.metercollectionapp.domain.model.DeviceParam

data class DeviceParamUiState (
    val uid: Long = 0,
    val paramType: DeviceParam.ParamType = DeviceParam.ParamType.UINT32,
    val measUnit: String = "",
    val name: String = "",
    val shortName: String = "",
    val status: SyncStatus = SyncStatus.UNKNOWN
)
