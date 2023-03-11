package com.example.android.metercollectionapp.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.android.metercollectionapp.MeterCollectionApplication
import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.presentation.uistate.AddObjectUiState
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import javax.inject.Inject

class AddObjectViewModel @Inject constructor (
    private val application: MeterCollectionApplication,
    private val repository: Repository
) : AndroidViewModel(application) {

    // для Databinding с полями ввода
    val nameLiveData = MutableLiveData("")
    val guidLiveData = MutableLiveData("")

    // UiState
    private val _newObjectUiState = MutableLiveData(AddObjectUiState())
    val addObjectUiState: LiveData<AddObjectUiState>
        get() = _newObjectUiState

    // переменные для навигации
    private val _navigateUp = MutableLiveData(false)
    val navigateUp: LiveData<Boolean>
        get() = _navigateUp

    private val _navigateToScanner = MutableLiveData(false)
    val navigateToScanner: LiveData<Boolean>
        get() = _navigateToScanner
    fun navigateToScannerDone() {
        _navigateToScanner.value = false
    }

    // запустить камеру для сканирования QR кодов
    fun onScan() {
        if (application.cameraPermissionGranted) {
            _navigateToScanner.value = true
        } else {
            _newObjectUiState.value = AddObjectUiState(cameraNotGranted = true)
        }
    }

    fun onSave() {
        val newName = nameLiveData.value!!
        if (newName.isNotEmpty()) {
            val newGuidString = guidLiveData.value!!
            if (newGuidString.isNotEmpty()) {
                try {
                    val newGuid = newGuidString.toLong()
                    viewModelScope.launch {
                        repository.addNewDeviceById(newName, newGuid)
                        _newObjectUiState.value = AddObjectUiState(success = true)
                        _navigateUp.value = true
                    }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    _newObjectUiState.value = AddObjectUiState(error = true)
                }
            } else {
                _newObjectUiState.value = AddObjectUiState(emptyGuid = true)
            }
        } else {
            _newObjectUiState.value = AddObjectUiState(emptyName = true)
        }
    }

    fun onCancel() {
        _navigateUp.value = true
    }

}
