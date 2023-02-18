package com.example.android.metercollectionapp.presentation.uistate

data class SelectObjectUiState (
    val listOfObjects: List<ObjectUiState> = listOf(),
    val currentPosition: Int = 0,  // пока не используется, зарезервировано
    val listIsLoading: Boolean = false,
    val listIsEmpty: Boolean = false,
    val listChanged: Boolean = false
)
