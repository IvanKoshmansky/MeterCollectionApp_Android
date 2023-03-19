package com.example.android.metercollectionapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.presentation.ScannerFeature
import com.example.android.metercollectionapp.presentation.uistate.ScannerUiState
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class ScannerViewModel @Inject constructor (private val repository: Repository) : ViewModel() {

    // цель запуска фрагмента сканирования qr кодов
    // используется для определения определения логики работы кнопки "Далее"
    private var scannerFeature: ScannerFeature = ScannerFeature.SCAN_FOR_NEW
    fun setupScannerFeature(feature: ScannerFeature) {
        scannerFeature = feature
    }

    // UiState
    private val _uiState = MutableLiveData(ScannerUiState(inProcess = true))
    val uiState: LiveData<ScannerUiState>
        get() = _uiState

    // переменные для навигации
    private val _navigationToWriteValuesLiveData = MutableLiveData(false)
    val navigationToWriteValuesLiveData: LiveData<Boolean>
        get() = _navigationToWriteValuesLiveData

    fun navigationToWriteValuesLiveDataDone() {
        _navigationToWriteValuesLiveData.value = false
    }

    private val _navigationBackLiveData = MutableLiveData(false)
    val navigationBackLiveData: LiveData<Boolean>
        get() = _navigationBackLiveData

    fun navigationBackDone() {
        _navigationBackLiveData.value = false
    }

    // callback который вызывается когда библиотека распознавания qr кодов распознала qr код
    fun scanningDone(scannedCode: String) {
        val state = ScannerUiState(inProcess = false)
        val info = scannedCode.split(":")
        var guid = 0L
        var devType = 0
        var name = ""
        var formatError = false
        if (info.size == 3) {
            // отсканировано успешно
            try {
                guid = info[0].toLong()
                devType = info[1].toInt()
                name = info[2]
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                formatError = true
            }
        } else {
            formatError = true
        }
        if (formatError) {
            // ошибка формата qr кода
            _uiState.value = state.copy(scanFormatError = true)
        } else {
            // распозналось успешно
            if (scannerFeature == ScannerFeature.SCAN_FOR_EXISTING) {
                // проверить на наличие в локальной БД
                viewModelScope.launch {
                    if (testExisting(guid)) {
                        _uiState.value = state.copy(scanSuccess = true, objectGuid = guid, objectName = name)
                    } else {
                        _uiState.value = state.copy(scanNotFound = true, objectGuid = guid, objectName = name)
                    }
                }
            } else {
                // новый объект - проверка наличия в локальной БД не требуется
                _uiState.value = state.copy(scanSuccess = true, objectGuid = guid, objectName = name)
            }
        }
    }

    private suspend fun testExisting(guid: Long) =
        try {
            repository.getDeviceById(guid)
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }

    fun onNext() {
        if (scannerFeature == ScannerFeature.SCAN_FOR_NEW) {
            // сохранить новый объект в локальную БД
            _uiState.value?.let {
                viewModelScope.launch {
                    repository.addNewDeviceById(it.objectName, it.objectGuid)
                    // сообщение "новый объект успешно сохранен"
                    _uiState.value = ScannerUiState(newObjectSaved = true)
                    // вернуться к списку устройств (должно добавиться новое устройство)
                    _navigationBackLiveData.value = true
                }
            }
        } else {
            // перейти по навигации на экран ввода значений
            _navigationToWriteValuesLiveData.value = true
        }
    }

    fun onBack() {
        _navigationBackLiveData.value = true
    }

}
