package com.example.android.metercollectionapp.presentation.uistate

data class DeviceParamsUiState (
    val paramsUiState: List<DeviceParamUiState> = emptyList(),
    val isLoading: Boolean = false
)
