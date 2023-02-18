package com.example.android.metercollectionapp.presentation.uistate

data class AddObjectUiState (
    val emptyName: Boolean = false,
    val emptyGuid: Boolean = false,
    val success: Boolean = false,
    val error: Boolean = false,
    val duplicatedGuid: Boolean = false,
)
