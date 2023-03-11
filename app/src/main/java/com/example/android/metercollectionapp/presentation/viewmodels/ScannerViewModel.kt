package com.example.android.metercollectionapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.presentation.uistate.ScannerUiState
import javax.inject.Inject

class ScannerViewModel @Inject constructor (private val repository: Repository) : ViewModel() {

    private enum class ScannerFeature {
        SCAN_FOR_NEW,
        SCAN_FOR_EXISTING
    }

    private val _uiState = MutableLiveData(ScannerUiState(inProcess = true))
    val uiState: LiveData<ScannerUiState>
        get() = _uiState

}
