package com.example.android.metercollectionapp.presentation.uistate

data class ShortDeviceParamsUiState (
    val params: List<ShortDeviceParamUiState> = listOf(),
    val isLoading: Boolean = false
)
