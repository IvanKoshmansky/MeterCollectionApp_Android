package com.example.android.metercollectionapp.presentation.uistate

data class WriteValuesElementUiState (
    val uid: Long = 0,
    val shortName: String = "",
    val stringValue: String = "",
    val measUnit: String = "",
    val deleteElementLambda: () -> Unit  // срабатыват при нажатии кнопки "удалить" в элементе списка
)
