package com.example.android.metercollectionapp.presentation.uistate

import com.example.android.metercollectionapp.SyncStatus
import com.example.android.metercollectionapp.domain.model.DeviceParamType

data class WriteValuesUiState (
    // состояние выбранного объекта
    val objectUiState: ObjectUiState = ObjectUiState(0, SyncStatus.UNKNOWN, ""),
    // список доступных параметров для выбранного объекта
    val deviceParams: ShortDeviceParamsUiState = ShortDeviceParamsUiState(listOf(), false),
    // сокращенное обозначение выбранного параметра
    val selectedParamShortName: String = "",
    // тип данных для ввода значения (влияет на inputType EditText)
    val selectedParamType: DeviceParamType = DeviceParamType.FLOAT,
    // список введенных значений
    val enteredValues: List<WriteValuesElementUiState> = listOf(),
    // значение данного параметра уже было введено (оповещение)
    val alreadyEntered: Boolean = false,
    // ошибка преобразования текста во float
    val convError: Boolean = false
)
