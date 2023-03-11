package com.example.android.metercollectionapp.presentation.viewmodels

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.metercollectionapp.MeterCollectionApplication
import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.presentation.uistate.SelectObjectUiState
import javax.inject.Inject

class SelectObjectViewModel @Inject constructor (
    private val application: MeterCollectionApplication,
    private val repository: Repository
) : AndroidViewModel(application) {

    private val _uiState = MutableLiveData(SelectObjectUiState())
    val uiState: LiveData<SelectObjectUiState>
        get() = _uiState

    private val _navigateToScan = MutableLiveData(false)
    val navigateToScan: LiveData<Boolean>
        get() = _navigateToScan

    fun navigateToScanDone() {
        _navigateToScan.value = false
    }

    fun onScan() {
        if (application.cameraPermissionGranted) {
            _navigateToScan.value = true
        } else {
            _uiState.value = SelectObjectUiState(cameraNotGranted = true)
        }
    }

}
