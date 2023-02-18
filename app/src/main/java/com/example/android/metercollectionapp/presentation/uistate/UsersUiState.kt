package com.example.android.metercollectionapp.presentation.uistate

data class UsersUiState (
    val usersUiState: List<UserUiState> = listOf(),
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false
)
