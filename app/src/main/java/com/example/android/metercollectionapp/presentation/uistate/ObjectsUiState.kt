package com.example.android.metercollectionapp.presentation.uistate

data class ObjectsUiState (
    val objectsUiState: List<ObjectUiState> = listOf(),
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false
)
