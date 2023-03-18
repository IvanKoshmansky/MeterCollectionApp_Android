package com.example.android.metercollectionapp.presentation.uistate

data class AddObjectUiState (
    val shortMessage: ShortMessageCode = ShortMessageCode.NOTHING_TO_SHOW
) {
    enum class ShortMessageCode {
        NOTHING_TO_SHOW,
        EMPTY_NAME,
        EMPTY_GUID,
        SUCCESS,
        ERROR,
        DUBLICATE_GUID,
        CAMERA_NOT_GRANTED
    }
}
