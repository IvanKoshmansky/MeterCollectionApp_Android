package com.example.android.metercollectionapp.presentation.uistate

data class DeviceParamsSelectSaveStatusUiState (
    val shortMessage: ShortMessageCode = ShortMessageCode.NOTHING_TO_SHOW
) {
    enum class ShortMessageCode {
        NOTHING_TO_SHOW,
        SUCCESS,
        ERROR
    }
}
