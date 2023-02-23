package com.example.android.metercollectionapp.presentation.uistate

data class DeviceParamsSelectUiState (
    val objects: List<ObjectUiState> = emptyList(),
    val objectsLoading: Boolean = false,
    val availableParams: List<DeviceParamSelectUiState> = emptyList(),
    val availableParamsLoading: Boolean = false,
    val selectedParams: List<DeviceParamSelectUiState> = emptyList(),
    val selectedParamsLoading: Boolean = false
)
