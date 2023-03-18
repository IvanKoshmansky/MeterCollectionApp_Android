package com.example.android.metercollectionapp.presentation.uistate

data class AddDeviceParamUiState (
    val shortMessage: ShortMessageCode = ShortMessageCode.NOTHING_TO_SHOW
) {
    enum class ShortMessageCode {
        NOTHING_TO_SHOW,
        EMPTY_FIELDS,
        SUCCESS,
        ERROR,
        DUBLICATE_UID
    }
}
