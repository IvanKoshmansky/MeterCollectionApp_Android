package com.example.android.metercollectionapp.presentation.uistate

data class SyncValuesCardUiState (
    val guid: Long = 0,
    val objectName: String = "",
    val rows: List<SyncValuesRowUiState> = listOf()
)
