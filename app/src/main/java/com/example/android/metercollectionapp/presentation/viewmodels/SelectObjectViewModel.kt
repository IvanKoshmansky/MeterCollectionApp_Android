package com.example.android.metercollectionapp.presentation.viewmodels

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.metercollectionapp.MeterCollectionApplication
import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.presentation.uistate.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectObjectViewModel @Inject constructor (
    private val application: MeterCollectionApplication,
    private val repository: Repository
) : AndroidViewModel(application) {

    private val _uiState = MutableLiveData(SelectObjectUiState(isLoading = true))
    val uiState: LiveData<SelectObjectUiState>
        get() = _uiState

    private val _navigateToScan = MutableLiveData(false)
    val navigateToScan: LiveData<Boolean>
        get() = _navigateToScan

    fun navigateToScanDone() {
        _navigateToScan.value = false
    }

    fun setup() {
        viewModelScope.launch {
            val devices = repository.getAllDevices()
            if (devices.isNotEmpty()) {
                val uiObjects = devices.map {
                    ObjectUiState(uid = it.guid, status = it.status, name = it.name)
                }
                _uiState.value = SelectObjectUiState(objects = uiObjects, isLoading = false)
            } else {
                _uiState.value = SelectObjectUiState(objects = emptyList(), isLoading = false)
            }
        }
    }

    fun onScan() {
        if (application.cameraPermissionGranted) {
            _navigateToScan.value = true
        } else {
            val state = _uiState.value ?: return
            _uiState.value = state.copy(cameraNotGranted = true)
        }
    }

}
