package com.example.android.metercollectionapp.presentation.uistate

data class ObjectsUiState (
    val objects: List<ObjectUiState> = emptyList(),
    val isLoading: Boolean = false
)
