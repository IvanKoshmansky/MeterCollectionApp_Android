package com.example.android.metercollectionapp.presentation.uistate

data class DeviceParamsSelectUiState (
    val paramsUiState: List<DeviceParamSelectUiState> = listOf(),
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false
)
