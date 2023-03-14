package com.example.android.metercollectionapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.presentation.uistate.ScannerUiState
import javax.inject.Inject

class ScannerViewModel @Inject constructor (private val repository: Repository) : ViewModel() {

    enum class ScannerFeature {
        SCAN_FOR_NEW,
        SCAN_FOR_EXISTING
    }

    // используется для определения назначения навигации по кнопке "Далее"
    private var _scannerFeature: ScannerFeature = ScannerFeature.SCAN_FOR_NEW
    val scannerFeauture: ScannerFeature
        get() = _scannerFeature
    fun setupScannerFeature(scannerFeature: ScannerFeature) {
        _scannerFeature = scannerFeature
    }

    private val _uiState = MutableLiveData(ScannerUiState(inProcess = true))
    val uiState: LiveData<ScannerUiState>
        get() = _uiState

    fun scanningDone(scannedCode: String) {
        val info = scannedCode.split(":")
        if (info.size == 3) {
            // отсканировано успешно
            try {
                val guid = info[0].toLong()
                val devType = info[1].toInt()
                val name = info[2]
                _uiState.value = ScannerUiState(objectGuid = guid, objectName = name, inProcess = false,
                    scanningDone = true, scanError = false)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                _uiState.value = ScannerUiState(inProcess = false, scanningDone = true, scanError = true)
            }
        }
    }

}
