package com.example.android.metercollectionapp.presentation.uistate

data class SyncValuesUiState (
    val values: List<SyncValuesCardUiState> = listOf(),
    val isLoading: Boolean = false,
    val userLoggedIn: Boolean = false,
    val userName: String = "",
)
