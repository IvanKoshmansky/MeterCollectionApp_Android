package com.example.android.metercollectionapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.presentation.uistate.DeviceParamUiState
import com.example.android.metercollectionapp.presentation.uistate.DeviceParamsUiState
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeviceParamsListViewModel @Inject constructor (private val repository: Repository) : ViewModel() {

    private val _uiState = MutableLiveData(DeviceParamsUiState(isLoading = true))
    val uiState: LiveData<DeviceParamsUiState>
        get() = _uiState

    fun setup() {
        _uiState.value = DeviceParamsUiState(isLoading = true)
        viewModelScope.launch {
            val fromRepo = repository.getAllDeviceParams()
            if (fromRepo.isNotEmpty()) {
                val params = fromRepo.map {
                    DeviceParamUiState(
                        uid = it.uid,
                        measUnit = it.measUnit,
                        name = it.name,
                        status = it.status
                    )
                }
                _uiState.value = DeviceParamsUiState(paramsUiState = params, isLoading = false)
            } else {
                _uiState.value = DeviceParamsUiState(paramsUiState = emptyList(), isLoading = false)
            }
        }
    }

    private val _navigateToAdd = MutableLiveData(false)
    val navigateToAdd: LiveData<Boolean>
        get() = _navigateToAdd

    fun navigateToAddDone() {
        _navigateToAdd.value = false
    }

    fun onAdd() {
        _navigateToAdd.value = true
    }

}

// TODO: добавить автозаполнение остальных полей по uid при наличии связи с сервером
