package com.example.android.metercollectionapp.presentation.uistate

import androidx.lifecycle.MutableLiveData

// презентация параметра устройств в сокращенном виде для показа в списках для выбора
data class DeviceParamSelectUiState (
    val uid: Long = 0,
    val name: String = "",
    // двусторонний датабайндинг через стандартный адаптер работает корректно только с MutableLiveData или Observable
    val checked: MutableLiveData<Boolean> = MutableLiveData(false)
)
