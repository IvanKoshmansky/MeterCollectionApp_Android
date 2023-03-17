package com.example.android.metercollectionapp.presentation.uistate

data class SelectObjectUiState(
    val objects: List<ObjectUiState> = emptyList(),
    val isLoading: Boolean = false,
    val cameraNotGranted: Boolean = false
)
