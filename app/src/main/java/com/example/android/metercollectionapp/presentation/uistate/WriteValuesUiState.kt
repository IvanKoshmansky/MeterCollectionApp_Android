package com.example.android.metercollectionapp.presentation.uistate

import com.example.android.metercollectionapp.SyncStatus
import com.example.android.metercollectionapp.domain.model.DeviceParam

data class WriteValuesUiState (
    // состояние выбранного объекта
    val objectUiState: ObjectUiState = ObjectUiState(0, SyncStatus.UNKNOWN, ""),
    // список доступных параметров для выбранного объекта
    val deviceParams: ShortDeviceParamsUiState = ShortDeviceParamsUiState(listOf(), false),
    // сокращенное обозначение выбранного параметра
    val selectedParamShortName: String = "",
    // тип данных для ввода значения (влияет на inputType EditText)
    val selectedParamType: DeviceParam.ParamType = DeviceParam.ParamType.FLOAT,
    // список введенных значений
    val enteredValues: List<WriteValuesElementUiState> = listOf(),
    // короткие сообщения
    val shortMessage: ShortMessageCode = ShortMessageCode.NOTHING_TO_SHOW
) {
    enum class ShortMessageCode {
        NOTHING_TO_SHOW,
        ALREADY_ENTERED,
        CONVERSION_ERROR,
        SAVE_SUCCESS,
        ENTERED_VALUES_EMPTY,
    }
}
