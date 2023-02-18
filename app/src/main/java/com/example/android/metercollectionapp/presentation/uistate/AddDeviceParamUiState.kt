package com.example.android.metercollectionapp.presentation.uistate

data class AddDeviceParamUiState (
    val emptyFields: Boolean = false,
    val success: Boolean = false,
    val error: Boolean = false,
    val duplicatedUid: Boolean = false,
)
