package com.example.android.metercollectionapp.presentation.uistate

import com.example.android.metercollectionapp.SyncStatus

data class WriteValuesUiState (
    // состояние выбранного объекта
    val objectUiState: ObjectUiState = ObjectUiState(0, SyncStatus.UNKNOWN, ""),
    // список доступных параметров для выбранного объекта
    val deviceParams: ShortDeviceParamsUiState = ShortDeviceParamsUiState(listOf(), false),
    // сокращенное обозначение выбранного параметра
    val selectedParamShortName: String = "",
    // список введенных значений
    val enteredValues: List<WriteValuesElementUiState> = listOf()
)
