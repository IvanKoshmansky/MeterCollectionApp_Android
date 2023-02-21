package com.example.android.metercollectionapp.presentation.uistate

// презентация параметра устройств в сокращенном виде для показа в списках для выбора
data class DeviceParamSelectUiState (
    val uid: Long = 0,
    val name: String = "",
    val checked: Boolean = false,
    val checkingLambda: (newState: Boolean) -> Unit
)

// вот здесь в UiState не нужно использовать платформенно зависимые элементы, такие как LiveData
// здесь checked + lamda
// для случая не списков, двусторонний датабайндинг работает с обозреваемыми типами
// вообще UiState используется только для передачи данных от ViewModel в контроллер,
// наоборот в обратную сторону можно использовать либо UiAction либо просто методы, которые дергать из ViewModel
