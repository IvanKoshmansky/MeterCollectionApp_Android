package com.example.android.metercollectionapp.presentation.uistate

data class ObjectSelectUiState (
    val objects: List<ObjectUiState> = listOf(),
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
)

