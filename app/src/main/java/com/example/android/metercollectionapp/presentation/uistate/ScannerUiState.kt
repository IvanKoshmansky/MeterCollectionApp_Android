package com.example.android.metercollectionapp.presentation.uistate

data class ScannerUiState (
    val objectGuid: Long = 0,
    val objectName: String = "",
    val inProcess: Boolean = false,
    val scanningDone: Boolean = false,
    val scanError: Boolean = false
)
