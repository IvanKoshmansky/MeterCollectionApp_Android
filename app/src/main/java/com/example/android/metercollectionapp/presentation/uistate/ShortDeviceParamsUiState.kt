package com.example.android.metercollectionapp.presentation.uistate

data class ShortDeviceParamsUiState (
    val params: List<DeviceParamUiState> = listOf(),
    val isLoading: Boolean = false
)
