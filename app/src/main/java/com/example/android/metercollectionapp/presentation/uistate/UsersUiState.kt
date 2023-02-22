package com.example.android.metercollectionapp.presentation.uistate

data class UsersUiState (
    val users: List<UserUiState> = emptyList(),
    val isLoading: Boolean = false
)
