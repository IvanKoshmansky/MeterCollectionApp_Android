package com.example.android.metercollectionapp.presentation.uistate

data class ScannerUiState (
    val objectGuid: Long = 0,
    val objectName: String = "",
    val inProcess: Boolean = false,
    val scanSuccess: Boolean = false,      // сканирование завершено, qr код распознан, устройство найдено или новое
    val scanFormatError: Boolean = false,  // сканирование завершено, qr код распознан, ошибка формата
    val scanNotFound: Boolean = false,     // сканирование завершено, qr код распознан, устройство не найдено в лок. БД
    val newObjectSaved: Boolean = false
)
