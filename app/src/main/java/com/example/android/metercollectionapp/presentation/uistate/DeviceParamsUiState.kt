package com.example.android.metercollectionapp.presentation.uistate

data class DeviceParamsUiState (
    val paramsUiState: List<DeviceParamUiState> = listOf(),
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false
)
