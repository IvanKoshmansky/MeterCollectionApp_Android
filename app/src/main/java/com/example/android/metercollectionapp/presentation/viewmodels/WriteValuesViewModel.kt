package com.example.android.metercollectionapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.presentation.uistate.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class WriteValuesViewModel @Inject constructor (private val repository: Repository) : ViewModel() {

    // UiState
    private val _uiState = MutableLiveData(WriteValuesUiState())
    val uiState: LiveData<WriteValuesUiState>
        get() = _uiState

    // переменные для двустороннего датабайндинга
    // позиция выбранного параметра в списке параметров
    val selectedParamIndex = MutableLiveData(0)

    // введенное текстовое значение параметра
    val enteredParamValue = MutableLiveData("")

    // переменные для навигации


    // установка начального состояния отображения экрана
    fun setup(objectGuid: Long, objectName: String) {
        // имя объекта + статус загрузки параметров объекта
        var state = WriteValuesUiState(
            objectUiState = ObjectUiState(uid = objectGuid, name = objectName),
            deviceParams = ShortDeviceParamsUiState(isLoading = true)
        )
        _uiState.value = state
        // загрузка списка параметров для объекта
        viewModelScope.launch {
            val paramsFromRepo = repository.getDeviceParamsAssociatedFrom(objectGuid)
            val uiParams = paramsFromRepo.map {
                ShortDeviceParamUiState(
                    uid = it.uid,
                    name = it.name,
                    shortName = it.shortName
                )
            }
            // список параметров загружен
            state = state.copy(deviceParams = ShortDeviceParamsUiState(isLoading = false, params = uiParams))
            _uiState.value = state
        }
    }

    // выбор параметра из списка параметров по индексу
    fun selectParamByIndex(idx: Int) {
        val state = _uiState.value ?: return
        if (idx < state.deviceParams.params.size) {
            val shortName = state.deviceParams.params[idx].shortName
            _uiState.value = state.copy(selectedParamShortName = shortName)
        }
    }
}

// все манипуляции со списком введенных параметров полностью на стороне ViewModel
