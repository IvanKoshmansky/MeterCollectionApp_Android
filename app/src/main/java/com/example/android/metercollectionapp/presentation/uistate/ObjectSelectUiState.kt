package com.example.android.metercollectionapp.presentation.uistate

data class ObjectSelectUiState (
    val objects: List<ObjectUiState> = emptyList(),
    val isLoading: Boolean = false
)
